pipeline {
    agent any

    environment {
        MAVEN_CMD = './mvnw'
        APP_PORT = '8080'
        BASE_URL = "http://localhost:${APP_PORT}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Baixando código do repositório...'
                checkout scm
            }
        }

        stage('Start Application') {
            steps {
                echo 'Iniciando a aplicação Spring Boot para testes de integração...'
                sh '''
                    cd blue-bank
                    nohup ${MAVEN_CMD} spring-boot:run -Dserver.port=${APP_PORT} > app.log 2>&1 &
                    echo $! > spring-boot.pid
                    
                    echo "Aguardando aplicação subir em ${BASE_URL}..."
                    for i in {1..30}; do
                        if curl -s --fail ${BASE_URL}/actuator/health > /dev/null; then
                            echo "Aplicação está UP!"
                            break
                        fi
                        echo "Tentativa $i/30... ainda não respondeu"
                        sleep 3
                    done
                    
                    # Verificação final
                    curl --fail ${BASE_URL}/actuator/health || exit 1
                '''
            }
        }

        stage('Run Postman/Newman API Tests') {
            steps {
                echo 'Executando testes de API com Newman (Postman)...'
                sh '''
                    # Instala newman + reporter bonito (só na primeira vez, depois é cacheado)
                    npm install -g newman newman-reporter-htmlextra
                    
                    cd blue-bank
                    
                    # Executa a coleção com relatório HTML lindo
                    newman run ../postman/bluebank-collection.json \
                        --env-var baseUrl=${BASE_URL} \
                        --reporters cli,htmlextra \
                        --reporter-htmlextra-export newman-report.html \
                        --reporter-htmlextra-title "BlueBank API - Testes de Integração" \
                        --reporter-htmlextra-browserTitle "BlueBank Tests" \
                        --timeout-request 10000 \
                        --delay-request 500
                '''
            }
            post {
                always {
                    publishHTML(target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'blue-bank',
                        reportFiles: 'newman-report.html',
                        reportName: 'Relatório de Testes de API (Postman)',
                        reportTitles: 'BlueBank API Tests'
                    ])
                    
                    archiveArtifacts artifacts: 'blue-bank/app.log', allowEmptyArchive: true
                }
            }
        }

        stage('Unit Tests') {
            steps {
                echo 'Executando testes unitários...'
                dir('blue-bank') {
                    sh '${MAVEN_CMD} test'
                }
            }
            post {
                always {
                    junit 'blue-bank/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Gerando JAR da aplicação...'
                dir('blue-bank') {
                    sh '${MAVEN_CMD} package -DskipTests'
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: 'blue-bank/target/*.jar', fingerprint: true
                }
            }
        }
    }

    post {
        always {
            echo 'Finalizando pipeline...'
            
            sh '''
                if [ -f blue-bank/spring-boot.pid ]; then
                    kill $(cat blue-bank/spring-boot.pid) || true
                    rm -f blue-bank/spring-boot.pid
                fi
                
                # Mata qualquer processo na porta 8080 (segurança extra)
                lsof -ti:8080 | xargs kill -9 || true
            '''
            
            cleanWs(cleanWhenFailure: true, cleanWhenUnstable: true)
        }
        success {
            echo 'SUCESSO TOTAL! Todos os testes (unitários + API) passaram!'
        }
        failure {
            echo 'FALHA! Veja o relatório de testes de API acima para detalhes.'
        }
    }
}

// Jenkinsfile (na raiz do repositório)
pipeline {
    agent any

    environment {
        MAVEN_CMD = './mvnw'
        APP_PORT = '8080'
        BASE_URL = "http://localhost:${APP_PORT}"
        PROJECT_DIR = 'apibluebank/blue-bank'
        POSTMAN_DIR = 'apibluebank/postman'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Baixando código do repositório...'
                checkout scm
                sh 'chmod +x apibluebank/blue-bank/mvnw'
            }
        }

        stage('Start Application') {
            steps {
                echo 'Iniciando a aplicação Spring Boot...'
                sh '''
                    # Verifica se o diretório existe
                    if [ ! -d "${PROJECT_DIR}" ]; then
                        echo "ERRO: Diretório ${PROJECT_DIR} não encontrado!"
                        pwd
                        ls -la
                        exit 1
                    fi

                    cd ${PROJECT_DIR}

                    # Limpa PID antigo
                    rm -f spring-boot.pid

                    # Inicia a aplicação
                    echo "Iniciando Spring Boot em background..."
                    nohup ${MAVEN_CMD} spring-boot:run -Dserver.port=${APP_PORT} > app.log 2>&1 &
                    echo $! > spring-boot.pid

                    echo "Aplicação iniciada com PID $(cat spring-boot.pid)"
                    echo "Logs em: ${PROJECT_DIR}/app.log"

                    # Aguarda health check (máx 90s)
                    echo "Aguardando ${BASE_URL}/actuator/health..."
                    for i in {1..30}; do
                        if curl -s --fail ${BASE_URL}/actuator/health > /dev/null 2>&1; then
                            echo "APLICAÇÃO ESTÁ NO AR E SAUDÁVEL!"
                            break
                        fi
                        if [ $i -eq 30 ]; then
                            echo "TIMEOUT: aplicação não subiu!"
                            echo "=== LOG DA APLICAÇÃO ==="
                            tail -50 app.log
                            echo "=== FIM DO LOG ==="
                            exit 1
                        fi
                        echo "Tentativa $i/30... aguardando"
                        sleep 3
                    done
                '''
            }
        }

        stage('Run Postman/Newman API Tests') {
            steps {
                echo 'Instalando Node.js + Newman automaticamente...'
                sh '''
                    set -e
                    NODE_VERSION="20.18.0"
                    NODE_DIR="node-v${NODE_VERSION}-linux-x64"

                    rm -rf "${NODE_DIR}" node-v*.tar.*

                    echo "Baixando Node.js ${NODE_VERSION} (.tar.gz - compatível com todos os Jenkins)..."
                    curl -L -o node.tar.gz https://nodejs.org/dist/v${NODE_VERSION}/node-v${NODE_VERSION}-linux-x64.tar.gz
                    
                    echo "Descompactando Node.js..."
                    tar -xzf node.tar.gz
                    rm node.tar.gz

                    export PATH="${WORKSPACE}/${NODE_DIR}/bin:$PATH"

                    node --version
                    npm --version

                    npm install -g newman newman-reporter-htmlextra

                    echo "Executando coleção Postman..."
                    newman run ${POSTMAN_DIR}/bluebank-collection.json \
                        --env-var baseUrl=${BASE_URL} \
                        --reporters cli,htmlextra \
                        --reporter-htmlextra-export newman-report.html \
                        --reporter-htmlextra-title "BlueBank API - Testes de Integração" \
                        --reporter-htmlextra-browserTitle "BlueBank Tests" \
                        --timeout-request 15000 \
                        --delay-request 500 \
                        --bail  # para o teste no primeiro erro (opcional, mas recomendado)

                    echo "Testes de API concluídos!"
                '''
    }
            post {
                always {
                    publishHTML(target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: '.',
                        reportFiles: 'newman-report.html',
                        reportName: 'Relatório de Testes de API (Postman)'
                    ])
                    archiveArtifacts artifacts: "${PROJECT_DIR}/app.log", allowEmptyArchive: true
                }
            }
        }

        stage('Unit Tests') {
            steps {
                dir("${PROJECT_DIR}") {
                    sh '${MAVEN_CMD} test'
                }
            }
            post {
                always {
                    junit '${PROJECT_DIR}/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                dir("${PROJECT_DIR}") {
                    sh '${MAVEN_CMD} package -DskipTests'
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: '${PROJECT_DIR}/target/*.jar', fingerprint: true
                }
            }
        }
    }

    post {
        always {
            echo 'Finalizando pipeline...'
            sh '''
                if [ -f ${PROJECT_DIR}/spring-boot.pid ]; then
                    kill $(cat ${PROJECT_DIR}/spring-boot.pid) || true
                    rm -f ${PROJECT_DIR}/spring-boot.pid
                fi
                lsof -ti:${APP_PORT} | xargs kill -9 || true
            '''
            cleanWs()
        }
        success {
            echo 'SUCESSO TOTAL! Build + testes unitários + API 100% verdes!'
        }
        failure {
            echo 'FALHA! Veja o relatório de testes de API e o log da aplicação.'
        }
    }
}

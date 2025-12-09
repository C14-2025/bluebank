pipeline {
    agent any

    tools {
        jdk 'JDK21'
        nodejs 'Node20'
    }
    environment {
        MAVEN_CMD = './mvnw'
        PROJECT_DIR = 'apibluebank/blue-bank'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Baixando código do repositório...'
                checkout scm
                sh 'chmod +x apibluebank/blue-bank/mvnw'
            }
        }

        stage('Build') {
            steps {
                echo 'Iniciando a aplicação Spring Boot...'
                dir("${PROJECT_DIR}"){
                    sh '${MAVEN_CMD} clean compile '
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
                    junit 'apibluebank/blue-bank/target/surefire-reports/*.xml'
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
                    archiveArtifacts artifacts: 'apibluebank/blue-bank/target/*.jar', fingerprint: true, allowEmptyArchive: false
                }
            }
        }
        stage('API Tests') {
            steps {
                script {
                    dir("${PROJECT_DIR}") {
                        sh '''
                            npm install -g newman newman-reporter-html
                            rm -f app.log spring-boot.log
                            
                            echo "=== INICIANDO APLICAÇÃO SPRING BOOT ==="
                            
                            ${MAVEN_CMD} spring-boot:run \
                                -Dspring-boot.run.profiles=default \
                                -Dserver.port=8081 \
                                -Dspring.datasource.url=jdbc:postgresql://localhost:5432/bluebank_test \
                                -Dspring.datasource.username=postgres \
                                -Dspring.datasource.password=postgres \
                                > spring-boot.log 2>&1 &
                            APP_PID=$!
                            echo "PID do Spring Boot: $APP_PID"
                            
                            echo "=== AGUARDANDO INICIALIZAÇÃO (máx 60 segundos) ==="
                            APP_STARTED=false
                            for i in {1..60}; do
                                if ! kill -0 $APP_PID 2>/dev/null; then
                                    echo "ERRO: Processo Spring Boot morreu!"
                                    cat spring-boot.log
                                    exit 1
                                fi
                                
                                if curl -s -f http://localhost:8081/actuator/health >/dev/null 2>&1; then
                                    echo "✅ Aplicação está respondendo na porta 8081"
                                    APP_STARTED=true
                                    break
                                fi
                                
                                if grep -q "Started.*Application" spring-boot.log || \
                                   grep -q "Tomcat started on port" spring-boot.log || \
                                   grep -q "Netty started on port" spring-boot.log; then
                                    echo "✅ Aplicação iniciou (visto no log)"
                                    APP_STARTED=true
                                    break
                                fi
                                
                                echo "⏳ Aguardando aplicação... ($i/60)"
                                sleep 1
                            done
                            
                            if [ "$APP_STARTED" = false ]; then
                                echo "❌ FALHA: Aplicação não iniciou no tempo esperado"
                                echo "=== ÚLTIMAS LINHAS DO LOG ==="
                                tail -50 spring-boot.log
                                echo "=== MATANDO PROCESSO ==="
                                kill $APP_PID 2>/dev/null || true
                                exit 1
                            fi
                            
                            echo "=== EXECUTANDO TESTES NEWMAN ==="
                            
                            if [ ! -f ../postman/bluebank-collection.json ]; then
                                echo "ERRO: Arquivo de coleção não encontrado!"
                                ls -la ../postman/
                                kill $APP_PID 2>/dev/null || true
                                exit 1
                            fi
                            
                            newman run ../postman/bluebank-collection.json \
                                --env-var "baseUrl=http://localhost:8081" \
                                -r cli,html \
                                --reporter-html-export target/newman-report.html \
                                --timeout-request 10000 \
                                --timeout-script 10000 \
                                --suppress-exit-code
                            
                            NEWMAN_EXIT_CODE=$?
                            echo "Newman exit code: $NEWMAN_EXIT_CODE"
                            
                            echo "=== FINALIZANDO APLICAÇÃO ==="
                            kill $APP_PID 2>/dev/null || true
                            sleep 3
                            
                            # Verificar se processo foi finalizado
                            if kill -0 $APP_PID 2>/dev/null; then
                                echo "Forçando término do processo..."
                                kill -9 $APP_PID 2>/dev/null || true
                            fi
                            
                            exit $NEWMAN_EXIT_CODE
                        '''
                    }
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: "${PROJECT_DIR}/target/newman-report.html", allowEmptyArchive: true
                    archiveArtifacts artifacts: "${PROJECT_DIR}/spring-boot.log", allowEmptyArchive: true 
                    publishHTML([
                        reportDir: "${PROJECT_DIR}/target",
                        reportFiles: 'newman-report.html',
                        reportName: 'API Test Report',
                        reportName: 'API Test Results',
                        alwaysLinkToLastBuild: true,
                        allowMissing: true,
                        keepAll: true
                    ])
                }
            }
        }
    }

    post {
        always {
            echo 'Finalizando pipeline...'
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

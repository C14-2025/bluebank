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
                        ${MAVEN_CMD} spring-boot:run \
                            -Dspring-boot.run.profiles=test \
                            -Dserver.port=0 > app.log 2>&1 &
                        APP_PID=$!
                        for i in {1..40}; do
                            if grep -q "Tomcat started on port" app.log; then
                                break
                            fi
                            sleep 1
                        done
                        ERVER_PORT=$(grep -o 'Tomcat started on port.*([0-9]\\+)' app.log | tail -1 | grep -o '[0-9]\\+')
                        [ -n "$SERVER_PORT" ] || (echo "Port not found" && kill $APP_PID && exit 1)
                        newman run ../postman/bluebank-collection.json \
                            --env-var baseUrl=http://localhost:$SERVER_PORT \
                            -r cli,html --reporter-html-export target/newman-report.html
                        kill $APP_PID || true
                        '''
                    }
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: "${PROJECT_DIR}/target/newman-report.html", allowEmptyArchive: true
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

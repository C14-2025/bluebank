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
                        # Install newman (Postman CLI)
                        npm install -g newman

                        # Start the app in background with test profile (uses H2 in-memory DB)
                        ${MAVEN_CMD} spring-boot:run -Dspring.profiles.active=test &
                        APP_PID=$!

                        # Wait for the app to start (adjust sleep if needed)
                        sleep 30

                        # Run Postman collection with newman (outputs to CLI and HTML report)
                        newman run ../../postman/bluebank-collection.json -r cli,html --reporter-html-export target/newman-report.html

                        # Kill the app process
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

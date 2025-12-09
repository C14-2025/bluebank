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

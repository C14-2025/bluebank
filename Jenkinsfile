pipeline {
    agent any

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
        stage('API Tests') {
            steps {
                script {
                    sh '''
                        curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
                        sudo apt-get install -y nodejs
                    '''
                    sh 'node --version'
                    sh 'npm --version'

                    dir("${PROJECT_DIR}") {
                        sh 'nohup ${MAVEN_CMD} spring-boot:run > app.log 2>&1 &'
                        echo "Aplicação iniciada em background"
                    }
                    
                    sleep(time: 30, unit: 'SECONDS')
                    
                    sh 'curl -f http://localhost:8080/actuator/health || echo "Aplicação não respondeu"'
            
                    dir("apibluebank/postman") {
                        sh 'npm install -g newman'
                        sh 'newman run bluebank-collection.json --reporters cli,junit --reporter-junit-export newman-report.xml'
                    }
            
                    sh 'pkill -f "spring-boot:run" || true'
                }
            }
            post {
                always {
                    junit 'apibluebank/postman/newman-report.xml'
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

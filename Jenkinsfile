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
                        curl -L -o node-v18.17.0-linux-x64.tar.xz https://nodejs.org/dist/v18.17.0/node-v18.17.0-linux-x64.tar.gz
                        tar -xzf node-v18.17.0-linux-x64.tar.gz
                        export PATH="$PWD/node-v18.17.0-linux-x64/bin:$PATH"
                        node --version
                        npm --version
                    '''
                    
                    dir("${PROJECT_DIR}") {
                        sh 'nohup ${MAVEN_CMD} spring-boot:run > app.log 2>&1 &'
                        echo "Aplicação iniciada em background"
                    }
                    
                    sleep(time: 35, unit: 'SECONDS')
                    
                    sh '''
                        npm install -g newman
                        
                        newman run apibluebank/postman/bluebank-collection.json \
                            --reporters cli,junit \
                            --reporter-junit-export apibluebank/postman/newman-report.xml \
                            --timeout-request 5000
                    '''
                    
                    sh 'pkill -f "spring-boot:run" 2>/dev/null || true'
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

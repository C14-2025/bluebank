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
    
        stage('Code Quality') {
            steps {
                script {
                    sh '''
                        ${MAVEN_CMD} checkstyle:check
                        ${MAVEN_CMD} pmd:check

                        // verifica dependencias vulneraveis
                        ${MAVEN_CMD} org.owasp:dependency-check-maven:check
                    '''
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

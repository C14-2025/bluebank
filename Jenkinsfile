pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
        nodejs 'NodeJS'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Baixando código do GitHub...'
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                echo 'Buildando Backend (Spring Boot)...'
                dir('apibluebank/blue-bank') {
                    sh 'mvn clean compile'
                }
            }
        }

        stage('Test Backend') {
            steps {
                echo 'Testando Backend...'
                dir('apibluebank/blue-bank') {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit 'apibluebank/blue-bank/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package Backend') {
            steps {
                echo 'Gerando JAR do Backend...'
                dir('apibluebank/blue-bank') {
                    sh 'mvn package -DskipTests'
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: 'apibluebank/blue-bank/target/*.jar'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                echo 'Buildando Frontend (React/Vite)...'
                dir('bluebankfront') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Test Frontend') {
            steps {
                echo 'Testando Frontend (se tiver testes)...'
                dir('bluebankfront') {
                    sh 'npm test'
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finalizada!'
            cleanWs()
        }
        success {
            echo 'Tudo verde! Projeto buildado com sucesso.'
        }
        failure {
            echo 'Algo deu errado. Veja os logs.'
        }
    }
}

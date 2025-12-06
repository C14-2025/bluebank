pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
        nodejs 'Node20'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Baixando código do GitHub...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Buildando Backend (Spring Boot)...'
                dir('apibluebank/blue-bank') {
                    sh 'mvn clean compile'
                }
            }
        }

        stage('Test') {
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

        stage('Package') {
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

pipeline {
    agent any  // Roda em qualquer máquina (o Jenkins local)

    tools {
        jdk 'JDK21'  // Usa o JDK que configuramos
        maven 'Maven3'  // Para backend
        nodejs 'Node18'  // Para frontend
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Baixando código do GitHub...'
                checkout scm  // Puxa do repo
            }
        }

        stage('Build Backend') {
            steps {
                echo 'Buildando Backend (Spring Boot)...'
                dir('apibluebank/blue-bank') {  // Entra na pasta do backend
                    sh 'mvn clean compile'  // Ou ./mvnw se preferir o wrapper
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
                    junit 'apibluebank/blue-bank/target/surefire-reports/*.xml'  // Relatórios de testes
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
                    archiveArtifacts artifacts: 'apibluebank/blue-bank/target/*.jar'  // Salva o JAR
                }
            }
        }

        stage('Build Frontend') {
            steps {
                echo 'Buildando Frontend (React/Vite)...'
                dir('frontbluebank/bluebankfront') {  // Entra na pasta do frontend
                    sh 'npm install'  // Instala dependências
                    sh 'npm run build'  // Builda para produção
                }
            }
        }

        stage('Test Frontend') {
            steps {
                echo 'Testando Frontend (se tiver testes)...'
                dir('frontbluebank/bluebankfront') {
                    sh 'npm test'  // Se você tiver testes configurados
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finalizada!'
            cleanWs()  // Limpa workspace
        }
        success {
            echo 'Tudo verde! Projeto buildado com sucesso.'
        }
        failure {
            echo 'Algo deu errado. Veja os logs.'
        }
    }
}

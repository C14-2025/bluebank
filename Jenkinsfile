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
                echo '🔨 Compilando projeto...'
                dir("${PROJECT_DIR}"){
                    sh '${MAVEN_CMD} clean compile '
                }
            }
        }
        stage('Unit Tests') {
            steps {
                echo '🧪 Executando testes unitários...'
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
        stage('Quality Checks') {
            steps {
                echo '📊 Verificando qualidade do código...'
                dir("${PROJECT_DIR}") {
                    sh "${MVNW_CMD} checkstyle:check || true"
                    sh "${MVNW_CMD} spotbugs:check || true"
                }
            }
        }
        stage('Package') {
            steps {
                echo '📦 Gerando pacote...'
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
            echo '🎉 SUCESSO TOTAL! Pipeline executada com sucesso!'
        }
        failure {
            echo '❌ FALHA! Verifique os logs para mais detalhes.'
        }
    }
}

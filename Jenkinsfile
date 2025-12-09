pipeline {
    agent any
    
    tools {
        jdk 'JDK21'
        nodejs 'Node20'
    }
    
    environment {
        MAVEN_CMD = './mvnw'
        PROJECT_DIR = 'apibluebank/blue-bank'        
        POSTMAN_DIR = 'apibluebank/postman'       
        POSTMAN_COLLECTION = '${POSTMAN_DIR}/bluebank-collection.json'
        NEWMAN_REPORT_DIR = 'newman-reports'
        DB_NAME = 'bluebank'
        DB_USER = 'postgres'
        DB_PASSWORD = 'postgres'
        DB_HOST = 'localhost'
        DB_PORT = '5432'
        SQL_SCRIPT = '${PROJECT_DIR}/sql-scripts.txt'  
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '📦 Baixando código do repositório...'
                checkout scm
                dir("${PROJECT_DIR}") {
                    sh 'chmod +x mvnw'
                }
            }
        }
        
        stage('Setup Database') {
            steps {
                echo '🗄️ Configurando banco de dados PostgreSQL...'
                script {
                    try {
                        // Tenta criar e configurar o banco
                        sh """
                            # Verifica se PostgreSQL está disponível
                            if ! command -v psql &> /dev/null; then
                                echo "⚠️  PostgreSQL não está instalado. Tentando instalar..."
                                apt-get update && apt-get install -y postgresql-client || true
                            fi
                            
                            # Cria banco de dados
                            PGPASSWORD=${DB_PASSWORD} psql -h ${DB_HOST} -p ${DB_PORT} -U ${DB_USER} -c "CREATE DATABASE ${DB_NAME};" 2>/dev/null || echo "Banco já existe"
                            
                            # Executa scripts SQL
                            if [ -f "${SQL_SCRIPT}" ]; then
                                echo "Executando script SQL..."
                                PGPASSWORD=${DB_PASSWORD} psql -h ${DB_HOST} -p ${DB_PORT} -U ${DB_USER} -d ${DB_NAME} -f "${SQL_SCRIPT}"
                            else
                                echo "Arquivo SQL não encontrado: ${SQL_SCRIPT}"
                            fi
                        """
                    } catch (Exception e) {
                        echo "⚠️  AVISO: Problema ao configurar banco de dados: ${e.message}"
                        echo "ℹ️  Certifique-se que o PostgreSQL está rodando em ${DB_HOST}:${DB_PORT}"
                        echo "ℹ️  Você pode configurar manualmente com:"
                        echo "    psql -U postgres -f ${SQL_SCRIPT}"
                    }
                }
            }
        }
        
        stage('Build') {
            steps {
                echo '🔨 Compilando projeto...'
                dir("${PROJECT_DIR}") {
                    sh '${MAVEN_CMD} clean compile -DskipTests'
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
                    junit "${PROJECT_DIR}/target/surefire-reports/*.xml"
                    archiveArtifacts artifacts: "${PROJECT_DIR}/target/surefire-reports/*.xml", fingerprint: true
                }
            }
        }
        
        stage('Start Application') {
            steps {
                echo '🚀 Iniciando aplicação Spring Boot...'
                dir("${PROJECT_DIR}") {
                    // Inicia aplicação em background
                    sh '''
                        # Para qualquer instância anterior
                        pkill -f "bluebank" || true
                        
                        # Inicia nova instância
                        nohup ${MAVEN_CMD} spring-boot:run \
                            -Dspring-boot.run.profiles=test \
                            -Dspring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME} \
                            -Dspring.datasource.username=${DB_USER} \
                            -Dspring.datasource.password=${DB_PASSWORD} \
                            > application.log 2>&1 &
                        echo $! > app.pid
                        
                        echo "PID da aplicação: $(cat app.pid)"
                    '''
                }
                
                // Aguarda aplicação iniciar
                sleep 30
                
                // Verifica saúde da aplicação
                script {
                    def retries = 5
                    def delay = 10
                    
                    for (int i = 0; i < retries; i++) {
                        try {
                            sh "curl -s -f http://localhost:8080/actuator/health || curl -s -f http://localhost:8080/"
                            echo "✅ Aplicação está rodando!"
                            break
                        } catch (Exception e) {
                            echo "⏳ Aguardando aplicação iniciar... (tentativa ${i + 1}/${retries})"
                            if (i == retries - 1) {
                                // Mostra log da aplicação em caso de falha
                                dir("${PROJECT_DIR}") {
                                    sh '''
                                        echo "=== ÚLTIMAS LINHAS DO LOG ==="
                                        tail -50 application.log
                                        echo "============================"
                                    '''
                                }
                                error "❌ Falha ao iniciar aplicação"
                            }
                            sleep delay
                        }
                    }
                }
            }
        }
        
        stage('Install Newman') {
            steps {
                echo '📦 Instalando Newman e dependências...'
                sh '''
                    npm install -g newman
                    npm install -g newman-reporter-html
                    npm install -g newman-reporter-htmlextra
                    npm install -g newman-reporter-junitfull
                '''
            }
        }
        
        stage('API Tests') {
            steps {
                echo '🔍 Executando testes de API com Newman...'
                script {
                    // Cria diretório para relatórios
                    sh "mkdir -p ${NEWMAN_REPORT_DIR}"
                    
                    // Verifica se a collection existe
                    sh """
                        if [ ! -f "${POSTMAN_COLLECTION}" ]; then
                            echo "❌ Collection não encontrada: ${POSTMAN_COLLECTION}"
                            exit 1
                        fi
                        echo "✅ Collection encontrada: ${POSTMAN_COLLECTION}"
                    """
                    
                    // Executa collection do Postman com HTML report aprimorado
                    sh """
                        newman run "${POSTMAN_COLLECTION}" \
                            --reporters cli,html,htmlextra,junit \
                            --reporter-html-export "${NEWMAN_REPORT_DIR}/newman-report.html" \
                            --reporter-htmlextra-export "${NEWMAN_REPORT_DIR}/newman-report-details.html" \
                            --reporter-junit-export "${NEWMAN_REPORT_DIR}/newman-report.xml" \
                            --delay-request 1000 \
                            --timeout 30000
                    """
                }
            }
            post {
                always {
                    // Arquiva relatórios
                    archiveArtifacts artifacts: "${NEWMAN_REPORT_DIR}/*", fingerprint: true
                    
                    // Publica relatório JUnit
                    junit "${NEWMAN_REPORT_DIR}/newman-report.xml"
                    
                    // Publica relatório HTML
                    publishHTML(target: [
                        reportDir: NEWMAN_REPORT_DIR,
                        reportFiles: 'newman-report-details.html',
                        reportName: 'API Test Report (Newman)',
                        keepAll: true
                    ])
                }
            }
        }
        
        stage('Stop Application') {
            steps {
                echo '🛑 Parando aplicação...'
                dir("${PROJECT_DIR}") {
                    script {
                        try {
                            sh '''
                                if [ -f app.pid ]; then
                                    echo "Parando aplicação com PID: $(cat app.pid)"
                                    kill -9 $(cat app.pid) 2>/dev/null || true
                                    rm -f app.pid
                                fi
                                # Limpa qualquer processo Java residual
                                pkill -f "java.*bluebank" 2>/dev/null || true
                            '''
                        } catch (Exception e) {
                            echo "⚠️  Não foi possível parar a aplicação: ${e.message}"
                        }
                    }
                }
            }
        }
        
        stage('Package') {
            steps {
                echo '📦 Empacotando aplicação...'
                dir("${PROJECT_DIR}") {
                    sh '${MAVEN_CMD} package -DskipTests'
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: "${PROJECT_DIR}/target/*.jar", fingerprint: true, allowEmptyArchive: false
                }
            }
        }
    }
    
    post {
        always {
            echo '📊 Coletando métricas finais...'
            script {
                // Salva logs para análise
                dir("${PROJECT_DIR}") {
                    sh '''
                        mkdir -p ../logs
                        cp application.log ../logs/application-${BUILD_NUMBER}.log 2>/dev/null || true
                        cp target/*.log ../logs/ 2>/dev/null || true
                    '''
                }
                
                // Lista arquivos gerados
                sh '''
                    echo "=== ARQUIVOS GERADOS ==="
                    find . -name "*.jar" -o -name "*.xml" -o -name "*.html" -o -name "*.log" | head -20
                    echo "======================="
                '''
            }
            
            // Limpa workspace, mas preserva artefatos
            cleanWs(cleanWhenFailure: false, cleanWhenSuccess: true, cleanWhenAborted: true)
        }
        success {
            echo '🎉 SUCESSO TOTAL! Pipeline concluída com sucesso!'
            echo "📈 Relatórios disponíveis:"
            echo "   - Testes unitários: ${env.BUILD_URL}testReport/"
            echo "   - Testes de API: ${env.BUILD_URL}Newman_20Report/"
        }
        failure {
            echo '❌ FALHA! Verifique os logs para mais detalhes.'
            script {
                // Mostra erros críticos
                sh '''
                    echo "=== ERROS ENCONTRADOS ==="
                    grep -i "error\|exception\|fail" apibluebank/blue-bank/application.log 2>/dev/null | tail -20 || echo "Nenhum log disponível"
                    echo "========================"
                '''
            }
        }
    }
}

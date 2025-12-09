pipeline {
    agent any

    tools {
        jdk 'JDK21'
        nodejs 'Node20'
    }
    environment {
        MAVEN_CMD = './mvnw'
        PROJECT_DIR = 'apibluebank/blue-bank'
        DOCKER_COMPOSE = 'docker-compose-test.yml'
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
                echo 'Compilando aplicação...'
                dir("${PROJECT_DIR}"){
                    sh '${MAVEN_CMD} clean compile'
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
        
        stage('Setup Test Environment') {
            steps {
                script {
                    echo '=== CONFIGURANDO AMBIENTE DE TESTE ==='
                    
                    // Criar docker-compose para PostgreSQL
                    writeFile file: 'docker-compose-test.yml', text: '''
version: '3.8'
services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: bluebank_test
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5433:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 5s
      timeout: 5s
      retries: 5
'''
                    
                    echo 'Iniciando PostgreSQL em container Docker...'
                    sh '''
                        # Verificar se Docker está disponível
                        docker --version || echo "Docker não disponível"
                        
                        # Iniciar PostgreSQL
                        docker-compose -f docker-compose-test.yml up -d
                        
                        # Aguardar PostgreSQL ficar pronto
                        echo "Aguardando PostgreSQL iniciar..."
                        sleep 10
                        
                        # Verificar se está rodando
                        docker-compose -f docker-compose-test.yml ps
                    '''
                }
            }
        }
        
        stage('API Tests with Real PostgreSQL') {
            steps {
                script {
                    dir("${PROJECT_DIR}") {
                        sh '''
                            echo "=== INSTALANDO NEWMAN ==="
                            npm install -g newman newman-reporter-html
                            
                            echo "=== CONFIGURANDO APLICAÇÃO ==="
                            # Exportar variáveis de ambiente para a aplicação
                            export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/bluebank_test
                            export SPRING_DATASOURCE_USERNAME=postgres
                            export SPRING_DATASOURCE_PASSWORD=postgres
                            export SERVER_PORT=8081
                            
                            echo "=== INICIANDO APLICAÇÃO SPRING BOOT ==="
                            
                            # Iniciar app com as variáveis de ambiente
                            ./mvnw spring-boot:run \
                                -Dserver.port=${SERVER_PORT} \
                                -Dspring.datasource.url=${SPRING_DATASOURCE_URL} \
                                -Dspring.datasource.username=${SPRING_DATASOURCE_USERNAME} \
                                -Dspring.datasource.password=${SPRING_DATASOURCE_PASSWORD} \
                                -Dspring.jpa.hibernate.ddl-auto=create-drop \
                                > app.log 2>&1 &
                            
                            APP_PID=$!
                            echo "PID da aplicação: $APP_PID"
                            
                            echo "=== AGUARDANDO STARTUP (60 segundos) ==="
                            # Aguardar com verificações
                            for i in {1..30}; do
                                if curl -s http://localhost:${SERVER_PORT}/actuator/health 2>/dev/null | grep -q "UP"; then
                                    echo "✅ Aplicação rodando!"
                                    break
                                fi
                                
                                if [ $i -eq 30 ]; then
                                    echo "❌ Timeout - App não iniciou"
                                    echo "=== LOG ==="
                                    tail -100 app.log
                                    exit 1
                                fi
                                
                                echo "⏳ Aguardando... ($i/30)"
                                sleep 2
                            done
                            
                            echo "=== EXECUTANDO TESTES NEWMAN ==="
                            # Executar testes
                            newman run ../postman/bluebank-collection.json \
                                --env-var "baseUrl=http://localhost:${SERVER_PORT}" \
                                -r cli,html \
                                --reporter-html-export target/newman-report.html \
                                --reporter-html-title "BlueBank API Tests - PostgreSQL" \
                                --delay-request 500 \
                                --suppress-exit-code
                            
                            echo "✅ Testes completos!"
                            
                            # Parar aplicação
                            kill $APP_PID 2>/dev/null || true
                            sleep 3
                        '''
                    }
                }
            }
            post {
                always {
                    script {
                        archiveArtifacts artifacts: "${PROJECT_DIR}/target/newman-report.html", allowEmptyArchive: true
                        archiveArtifacts artifacts: "${PROJECT_DIR}/app.log", allowEmptyArchive: true
                        
                        publishHTML([
                            reportDir: "${PROJECT_DIR}/target",
                            reportFiles: 'newman-report.html',
                            reportName: 'API Test Report (PostgreSQL)',
                            alwaysLinkToLastBuild: true,
                            allowMissing: true,
                            keepAll: true
                        ])
                    }
                }
            }
        }
        
        stage('Cleanup') {
            steps {
                sh '''
                    echo "=== LIMPANDO AMBIENTE ==="
                    # Parar containers Docker
                    docker-compose -f docker-compose-test.yml down 2>/dev/null || true
                    
                    # Matar processos Spring Boot pendentes
                    pkill -f "spring-boot:run" 2>/dev/null || true
                    
                    echo "Ambiente limpo!"
                '''
            }
        }
    }

    post {
        always {
            echo 'Pipeline finalizada!'
            cleanWs()
        }
        success {
            echo '✅ SUCESSO TOTAL! Pipeline com PostgreSQL real funcionando!'
        }
        failure {
            echo '❌ Algum estágio falhou. Verifique os logs.'
        }
    }
}

#!/bin/bash
# scripts/setup-db.sh
set -e

echo "🔧 Configurando banco de dados PostgreSQL..."

# Verifica se o psql está disponível
if ! command -v psql &> /dev/null; then
    echo "❌ PostgreSQL client não encontrado. Instale com:"
    echo "   sudo apt-get install postgresql-client"
    exit 1
fi

# Aguarda PostgreSQL ficar disponível
echo "⏳ Aguardando PostgreSQL em localhost:5432..."
until PGPASSWORD=postgres psql -h localhost -p 5432 -U postgres -c '\q' 2>/dev/null; do
    echo "📡 PostgreSQL não disponível, tentando novamente..."
    sleep 2
done

echo "✅ PostgreSQL está disponível!"

# Cria banco se não existir
echo "🗄️  Criando banco 'bluebank'..."
PGPASSWORD=postgres psql -h localhost -p 5432 -U postgres -c "CREATE DATABASE bluebank;" 2>/dev/null || echo "ℹ️  Banco já existe"

# Executa scripts SQL
SQL_FILE="apibluebank/blue-bank/sql-scripts.txt"
if [ -f "$SQL_FILE" ]; then
    echo "📜 Executando script SQL..."
    PGPASSWORD=postgres psql -h localhost -p 5432 -U postgres -d bluebank -f "$SQL_FILE"
    echo "✅ Script SQL executado com sucesso!"
else
    echo "❌ Arquivo SQL não encontrado: $SQL_FILE"
    exit 1
fi

echo "🎉 Configuração do banco concluída!"

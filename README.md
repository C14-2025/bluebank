# BlueBank — Sistema Bancário Web

Aplicação desenvolvida para a disciplina de Engenharia de Software. O projeto consiste em uma API bancária construída com **Spring Boot** e um **frontend em React + TypeScript + Vite**, permitindo o cadastro de clientes, criação de contas, realização de transações e visualização de dados.

---

## 👥 Integrantes do Grupo

* **Bruno Ferreira Fernandes Ribeiro**
* **Douglas Hideaki De Almeida Otani**
* **Marcelo Froes Padilha Filho**
* **Miguel Vianna Streva**

## 🤝 Contribuições da Equipe

* **Miguel Vianna Streva** — Desenvolvimento principal da **API** (arquitetura, regras de negócio, JPA/Hibernate, DTOs, validações, mapeamentos e lógica de transações), além de suporte conceitual ao restante do projeto.
* **Marcelo Froes Padilha Filho** — Colaboração direta no desenvolvimento da **API**, contribuindo com decisões técnicas, revisão de ideias, apoio no desenho das funcionalidades e participação ativa no processo.
* **Douglas Hideaki De Almeida Otani** — Desenvolvimento completo do **frontend** em React, TypeScript e Tailwind, incluindo rotas, formulários, máscaras, validações, comunicação com a API e construção de todas as telas.
* **Bruno Ferreira Fernandes Ribeiro** — Responsável pela **pipeline do Jenkins**, automações de build, testes e empacotamento do projeto.

## 🚀 Tecnologias Utilizadas

### **Backend (API)**

* **Java 21**
* **Spring Boot** (Web, Validation, JPA)
* **PostgreSQL**
* **Hibernate**
* **ModelMapper**
* **Google libphonenumber**
* **Maven**

### **Frontend**

* **React**
* **TypeScript**
* **Vite**
* **Tailwind CSS**
* **Formik + Yup**
* **React Router**
* **React Input Mask**

### **CI/CD**

* **Jenkins** com pipeline declarativa

---

## 📦 Estrutura do Projeto

O projeto foi organizado em formato de **monorepo**, contendo:

* Uma pasta dedicada para o **backend (API)**
* Uma pasta dedicada para o **frontend**

---

## 🛠️ Como Instalar e Executar

### **1. Backend**

Pré‑requisitos:

* Java 17
* Maven
* PostgreSQL

#### **Configurar banco de dados

Crie uma database no PostgreSQL:

```sql
CREATE DATABASE bluebank;
```

Os scripts para criação de todas as tabelas estão no arquivo **sql-scripts** na raiz do projeto.
Configure as credenciais no arquivo `application.yml`.

```sql
CREATE DATABASE bluebank;
```

Configure as credenciais no arquivo `application.yml`.

#### **Executar o backend**

```bash
mvn spring-boot:run
```

O servidor subirá em: `http://localhost:8080`

---

### **2. Frontend**

Pré‑requisitos:

* Node.js 18+

#### **Instalar dependências**

```bash
npm install
```

#### **Rodar o servidor de desenvolvimento**

```bash
npm run dev
```

O frontend iniciará em: `http://localhost:5173`

---

## 🔄 Pipeline no Jenkins

*(Pipeline desenvolvida pelo integrante **Bruno Ferreira Fernandes Ribeiro**)*
A pipeline foi configurada da seguinte forma:

1. **Checkout** — Clona o código do GitHub
2. **Build Backend** — Compila o backend com Maven
3. **Test Backend** — Executa `mvn test` e publica os relatórios
4. **Package Backend** — Gera o arquivo `.jar` e o disponibiliza no Jenkins
5. **Build Frontend** — Executa `npm install` e `npm run build`

---

## 📚 Funcionalidades Principais

### **Clientes**

* Cadastro de cliente com validações (nome, email, telefone, documento)
* Máscara e validação de telefone com Google libphonenumber
* Visualização de clientes cadastrados
* Perfil individual

### **Contas Bancárias**

* Criação de conta vinculada a um cliente
* Relação **OneToOne** entre `Customer` e `Account`
* Regras especiais para evitar carregamento incorreto no Hibernate

### **Transações**

* Criação de transações entre contas
* Registro simultâneo em **payer** e **payee**
* Exibição de mensagens de erro tratadas pela API

---

## 🧠 Problemas Técnicos Enfrentados (e Soluções)

### **1. Relação OneToOne Customer–Account**

* O Hibernate não diferenciava corretamente o dono da relação
* Tentativas de carregar contas inexistentes geravam erros
* **Solução:** Criação de queries customizadas no Repository e ajustes nos mapeamentos

### **2. Remoção de Account**

* Remover uma conta exigia desvincular o campo `account` do `Customer`
* **Solução:** Setar manualmente o campo como `null` antes do delete

### **3. Armazenamento de Telefone + Country Code**

* Dificuldade em armazenar ambos em um único campo no PostgreSQL
* **Solução:** Processamento via DTO + ModelMapper + libphonenumber

### **4. Mapeamento de DTOs e Entidades**

* Necessidade de mapear corretamente account, phone e transações
* **Solução:** Configurações específicas no ModelMapper

### **5. Frontend (Douglas)**

* Criação de projeto com Vite + React + TS
* Configuração de rotas (React Router)
* Implementação de formulários com Formik/Yup
* Integração com a API
* Problemas de CORS

  * **Solução:** Bean de configuração no Spring liberando rotas locais
* Telas feitas:

  * Home
  * Header
  * Clientes (cadastro, listagem, validações, máscaras)
  * Perfil do cliente
  * Criação de conta
  * Transações (exibição de erros da API)

---

## 🏗️ Arquitetura da API

Fluxo geral:

1. **Controller** recebe o DTO e faz validações iniciais
2. **Service** aplica regras de negócio
3. **Validators** (quando necessário)
4. **Repository** executa operações no banco de dados

---

## ▶️ Como Usar

1. Cadastrar um cliente pelo frontend
2. Criar uma conta para esse cliente
3. Realizar transações entre contas
4. Visualizar dados do cliente e suas operações

---

## 📄 Licença

Projeto acadêmico — uso livre para estudos.

---

## 📞 Contato

Em caso de dúvidas, fale com qualquer integrante do grupo ou abra uma issue no repositório.


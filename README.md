# Desafio Técnico - Sistema de Cadastro de Clientes

## 🧾 Descrição

Este projeto é uma solução para o desafio de criar um sistema de cadastro de clientes, permitindo gerenciar informações como nome, e-mail, logotipo e múltiplos logradouros. A aplicação é composta por um **frontend em JSF + PrimeFaces** e um **backend em Spring Boot**. O banco de dados utilizado é o **SQL Server 2022**, rodando via container Docker.

---

## 📐 Arquitetura da Solução

### Frontend
- **JSF (Java Server Faces)**
- **PrimeFaces**
- HTML + CSS + JavaScript
- Servidor de aplicação: **WildFly 26**
- Comunicação com backend via chamadas REST

### Backend
- **Spring Boot (Java 8)**
- API RESTful
- JPA (Hibernate)
- SQL Server 2022 (via Docker)
- Armazenamento de imagem (logotipo) em formato binário no banco de dados

---

## 📋 Requisitos Atendidos

- [x] CRUD completo para Cliente
- [x] CRUD completo para Logradouro
- [x] Upload e armazenamento de logotipo
- [x] Validação de e-mail único
- [x] Relacionamento 1:N entre Cliente e Logradouro
- [x] API com autenticação e autorização (JWT ou Basic Auth)
- [x] Otimizações para performance

---

## 💾 Modelagem de Dados

```sql
CREATE TABLE Cliente (
    id_cliente INT PRIMARY KEY IDENTITY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    logotipo VARBINARY(MAX)
);

CREATE TABLE Logradouro (
    id_logradouro INT PRIMARY KEY IDENTITY,
    id_cliente INT,
    logradouro VARCHAR(255) NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
);

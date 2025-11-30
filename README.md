# Projeto — Backend (Spring Boot) + Frontend (Angular)

Este projeto é composto por duas partes principais:

- **Backend:** Spring Boot 
- **Frontend:** Angular

---

##  Pré-requisitos

Para rodar o projeto, você precisa ter instalado:

- Um editor de código, como VSCode
- **Spring Boot Extension Pack** (Se usar VSCode) 
- **Angular CLI**

  ```bash
  npm install -g @angular/cli
  ```
- **MySQL** (De preferencia MySQLWorkbench)


# Como rodar no Backend (Node.js)

Antes de rodar o backend, é obrigatório editar o arquivo:

  ```bash
  func4_backend\src\main\resources\application.properties
  ```
Configure os dados de conexão com o banco:

Mude nome_do_banco e password

  ```bash
  spring.application.name=func4_backend

  #dados obrigatorios de conexão com o banco de dados
  spring.datasource.url=jdbc:mysql://localhost:3306/nome_do_banco
  spring.datasource.username=root
  spring.datasource.password=root
  
  
  #permite ao hibernate criar/atualizar as tabelas do banco de dados
  spring.jpa.hibernate.ddl-auto=update

  #opcionais
  #exibe no terminal comandos SQL executados
  spring.jpa.show-sql=true
  
  #exibe as msgs coloridas 
  spring.output.ansi.enabled=always
  ```

Usando o Spring Boot Dashboard clique em Run
O servidor iniciará na porta configurada (padrão: 8080).

# Como rodar o Frontend (Angular)

No terminal, dentro da pasta frontend, execute:
 ```bash
ng serve
```
A aplicação estará disponível em:
 ```bash
http://localhost:4200
```

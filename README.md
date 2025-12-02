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


# Como rodar no Backend (Spring Boot)

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

Usando o Spring Boot Dashboard clique em Run:

O servidor iniciará na porta configurada (padrão: 8080).

Depois, no banco de dados, use:
```bash
  INSERT INTO produtos (nome, descricao, preco, estoque, ativo) VALUES
  ('Coca-Cola Lata 350ml', 'Refrigerante Coca-Cola tradicional', 4.50, 100, 1),
  
  ('Pepsi Lata 350ml', 'Refrigerante Pepsi tradicional', 4.00, 80, 1),
  
  ('Batata Frita 200g', 'Porção de batata frita crocante', 12.90, 50, 1),
  
  ('Hambúrguer Artesanal', 'Hambúrguer artesanal 180g com pão brioche', 22.50, 30, 1),
  
  ('Pizza Calabresa Média', 'Pizza de calabresa média 35cm', 34.90, 20, 1),
  
  ('Pizza Marguerita Média', 'Pizza marguerita média 35cm', 32.90, 18, 1),
  
  ('Água Mineral 500ml', 'Água mineral sem gás', 2.50, 200, 1),
  
  ('Suco de Laranja 300ml', 'Suco natural de laranja', 7.90, 40, 1),
  
  ('Chocolate 90g', 'Barra de chocolate ao leite 90g', 6.50, 60, 1),
  
  ('Sorvete 2 Litros', 'Sorvete sabor creme 2L', 19.90, 25, 1);

```

Para inserir produtos de exemplo.

Logo após, usando progamas como thunder client no vscode, faça um método post no endereço:
 ```bash
  http://localhost:8080/api/orders
```

Com o seguinte body: (pode mudar o nome a vontade, copie o cole o produto e mude seu id ou quantidade para produtos diferentes)
```bash
  {
  "clienteNome": "Lucas",
  "itens":[
    { "produto": { "id": 1 }, "quantidade": 2 }
  ],
  "status": "PENDENTE"
}
```

# Como rodar o Frontend (Angular)

No terminal, dentro da pasta frontend, execute:
 ```bash
ng serve
```
A aplicação estará disponível em:
 ```bash
http://localhost:4200
```
# Colaboradores
## Frontend
Hugo e Thiago

## Backend
Thiago

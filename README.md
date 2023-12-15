# Car System
API RESTful para Sistema de Usuários de Carros com Java e Spring Boot. 

# Descrição

**Rotina de Transações**
API RESTful de criação de usuários e carros com login.

# Endpoints 

**1. POST /api/users**

***Exemplo de JSON para criação de usuário***

***Request body:***

```
{
    "firstName": "Hello",
    "lastName": "World",
    "email": "hello@world.com",
    "birthday": "1990-05-01",
    "login": "hello.world",
    "password": "h3ll0",
    "phone": "988888888",
    "role": "USER",
    "cars": [
            {
                "year": 2018,
                "licensePlate": "PDV-0625",
                "model": "Audi",
                "color": "White"
            }
        ]  
}
```
# Atributos do usuário:

![atributos_usuario.png](images%2Fatributos_usuario.png)

# Atributos do carro:

![atributos_carro.png](images%2Fatributos_carro.png)
```
a) Rotas que NÃO exigem autenticação
```
![erro_users.png](images%2Ferro_users.png)


```
b) Rotas que exigem autenticação

obs: Todas as rotas abaixo esperam o token de acesso da API (JWT) via header Authorization
```
![erro_cars.png](images%2Ferro_cars.png)

**2. POST /cars/**

***Exemplo de JSON para criação de carro***

***Response body***
```
{
    "year": 2018,
    "licensePlate": "PDV-0625",
    "model": "Audi",
    "color": "White"
}
```

# Tecnologias utilizadas

-- Backend

- Java (17)
- Spring Boot (3.1.5)
- Spring JPA
- Spring Validation
- Spring Boot Web
- H2 (Virtual DB)
- Maven (8.4)
- Lombok (1.18.30)
- Swagger (OpenApi)

-- Frontend

- npm (8.19.4)
- node (v16.20.2)
- template (admin-lte)

### Como construir a aplicação backend

backend -> mvn clean install

frontend -> ng clear
frontend -> ng build

### Como executar os testes da aplicação

backend -> mvn test

### Como executar a aplicação

backend -> mvn spring-boot:run

frontend -> ng serve

### Como executar o swagger da aplicação

http://localhost:8080/swagger-ui.html

### Como executar o frontend

http://localhost:4200/

### Console do H2

http://localhost:8080/h2-console

### Docker compose

docker-compose up
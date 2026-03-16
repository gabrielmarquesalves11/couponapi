# Coupon API

API REST desenvolvida em **Java 17 + Spring Boot 3** para gerenciamento
de cupons de desconto.\
O projeto implementa regras de negócio como **sanitização de código,
validação de expiração e soft delete**, seguindo boas práticas de
arquitetura e organização de código.

------------------------------------------------------------------------

# Arquitetura adotada

O projeto segue uma abordagem inspirada em **DDD (Domain Driven Design)
simplificado**, com separação clara de responsabilidades.

    controller/
    service/
    domain/
    repository/
    dto/
    exception/
    config/

### Camadas

**Controller** - Responsável apenas pela exposição dos endpoints REST. -
Não contém regras de negócio.

**Service** - Responsável pela orquestração da aplicação. - Coordena
domínio e persistência.

**Domain** - Camada central da aplicação. - Contém a entidade `Coupon` e
suas regras de negócio. - Implementa validações e comportamentos como
criação e exclusão.

**Repository** - Interface de persistência usando Spring Data JPA.

**DTO** - Objetos usados para comunicação da API. - Evitam exposição
direta das entidades do domínio.

**Exception** - Tratamento centralizado de exceções da aplicação.

------------------------------------------------------------------------

# Decisões técnicas

### Spring Boot 3

Escolhido por ser o padrão moderno do ecossistema Java para APIs REST.

### Java 17

Versão LTS amplamente utilizada em ambientes corporativos.

### Domain Logic Encapsulation

As regras de negócio foram encapsuladas na entidade `Coupon`, evitando
lógica procedural em serviços ou controllers.

Exemplo:

    Coupon.create(...)
    coupon.delete()

### Soft Delete

Ao invés de remover registros fisicamente do banco, utilizamos:

    deleted = true

Com filtro automático:

    @Where(clause = "deleted = false")

Isso evita perda de dados históricos.

### Sanitização do código do cupom

Caracteres especiais são removidos utilizando regex:

    code.replaceAll("[^a-zA-Z0-9]", "")

Garantindo um código final com **6 caracteres alfanuméricos**.

### Banco H2 em memória

Utilizado para simplificar execução local e testes.

### Swagger (OpenAPI)

Documentação automática da API.

### Lombok

Redução de boilerplate de getters, builders e construtores.

------------------------------------------------------------------------

# Regras de negócio implementadas

### Criação de cupom

-   `code` obrigatório
-   `description` obrigatório
-   `discountValue >= 0.5`
-   `expirationDate` não pode ser passada
-   código sanitizado automaticamente
-   cupom pode ser criado já publicado

### Exclusão

-   Exclusão lógica (soft delete)
-   não é possível deletar um cupom já deletado

------------------------------------------------------------------------

# Como rodar localmente

## Pré-requisitos

-   Java 17
-   Maven 3.9+

## Clonar o repositório

    git clone https://github.com/gabrielmarques11/couponapi.git

## Compilar o projeto

    mvn clean install

## Rodar a aplicação

    mvn spring-boot:run

Aplicação disponível em:

    http://localhost:8080

------------------------------------------------------------------------

# Console do banco H2

    http://localhost:8080/h2-console

Configuração:

    JDBC URL: jdbc:h2:mem:testdb
    User: sa
    Password:

------------------------------------------------------------------------

# Como rodar com Docker

## Build da imagem

    docker build -t couponapi .

## Executar container

    docker run -p 8080:8080 couponapi

Aplicação disponível em:

    http://localhost:8080

------------------------------------------------------------------------

# Rodando com Docker Compose

    docker-compose up --build

------------------------------------------------------------------------

# Documentação da API (Swagger)

Após iniciar a aplicação, acessar:

    http://localhost:8080/swagger-ui.html

ou

    http://localhost:8080/swagger-ui/index.html

------------------------------------------------------------------------

# Endpoints da API

## Criar Cupom

POST

    /coupon

Exemplo request:

``` json
{
  "code": "123456@",
  "description": "Exemplo de coupon",
  "discountValue": 1,
  "expirationDate": "2026-11-04T17:14:45.180Z",
  "published": true
}
```

Exemplo response:

``` json
	
Response body
Download
{
  "id": "7015b9e6-d5fd-43d9-98fc-d94c9826cf18",
  "code": "123456",
  "description": "Exemplo de coupon",
  "discountValue": 1,
  "expirationDate": "2026-11-04T17:14:45.18",
  "status": "ACTIVE",
  "published": true,
  "redeemed": false
}
```

------------------------------------------------------------------------

## Buscar Cupom

GET

    /coupon/{id}

------------------------------------------------------------------------

## Deletar Cupom (Soft Delete)

DELETE

    /coupon/{id}

------------------------------------------------------------------------

# Testes

O projeto utiliza:

-   **JUnit 5**
-   **Spring Boot Test**
-   **Mockito**

Os testes cobrem:

-   criação de cupons
-   sanitização do código
-   validação de expiração
-   validação de valor mínimo de desconto
-   exclusão lógica

Executar testes:

    mvn test

------------------------------------------------------------------------

# Cobertura de testes

Objetivo:

    >= 80% de cobertura

Principal foco nos **testes de domínio**, garantindo que as regras de
negócio estejam corretas.

------------------------------------------------------------------------

# Autor

Projeto desenvolvido como **desafio técnico backend Java** demonstrando
práticas de:

-   Clean Code
-   Domain Driven Design
-   Arquitetura em camadas
-   Testes automatizados
-   Containerização

[![Java CI with Gradle](https://github.com/rapesil/rest-api-for-tests/actions/workflows/ci.yml/badge.svg)](https://github.com/rapesil/rest-api-for-tests/actions/workflows/ci.yml)

# REST-API-FOR-TESTS

API simples para:

* Treinar minhas habilidades de desenvolvimento;
* Ter um projeto que ajude testadores que trabalhem com Java e Spring.

## Pré-requisitos

* Java 11+
* Docker-Compose

## Executando o projeto

Apesar do meu foco com esse projeto ser trabalhar com Java, você pode utilizar essa API para treinar testes utilizando 
qualquer outra linguagem/ferramenta. 

Para criar uma nova imagem, primeiro crie um novo jar:

Você pode subir a aplicação digitando:

```
docker-compose up
```

Esse comando irá subir um container do `MySql` e também a aplicação na porta `8080`.

Pronto! Você pode apontar seus testes para `http://localhost:8080`. 

Você também pode verificar a documentação da API em `http://localhost:8080/swagger-ui/`





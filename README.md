[![Java CI with Gradle](https://github.com/rapesil/rest-api-for-tests/actions/workflows/ci.yml/badge.svg)](https://github.com/rapesil/rest-api-for-tests/actions/workflows/ci.yml)
# REST-API-FOR-TESTS

Inspirado no projeto [ServeRest do Paulo Gonçalves](), eu decidi criar desenvolver uma api. A ideia é conseguir desenvolver um projeto em Java que sirva para dois objetivos:

* Treinar minhas habilidades de desenvolvimento;
* Ter um projeto que ajude testadores que trabalhem com Java e Spring.

## Pré-requisitos

* Java 11+
* Docker-Compose

## Executando o projeto

Apesar do meu foco com esse projeto ser trabalhar com Java, você pode utilizar essa API para treinar testes utilizando qualquer outra linguagem/ferramenta. 

Para subir e executar o projeto, primeiro é necessário subir o banco. Esse projeto utiliza o `MySql` e você executá-lo através do `docker-compose`. Na pasta raiz do projeto abra um terminal e digite:

```
docker-compose up
```

Abra uma nova aba do terminal e digite:

```
./gradlew bootRun
``` 

O projeto será inicializado na porta `8080`. Caso deseje inicializar em outra porta, basta alterar a propriedade `port` em `src/main/resources/application.yml`.

Pronto! Você pode apontar seus testes para `http://localhost:8080`. 





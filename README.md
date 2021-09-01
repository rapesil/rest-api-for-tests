[![Java CI with Gradle](https://github.com/rapesil/rest-api-for-tests/actions/workflows/ci.yml/badge.svg)](https://github.com/rapesil/rest-api-for-tests/actions/workflows/ci.yml)

# REST-API-FOR-TESTS

API Rest desenvolvida em Java para estudo de testes.

## Pré-requisitos

- Java 11 (para execução sem Docker)
- Docker e docker-compose

## Features Disponíveis 

* Verbos GET, POST, PUT e DELETE
* Comunicação com banco de dados MySql (através de container Docker)
* Autenticação Basic
* Testes Unitários
* Testes de Integração
* Testes de Mutantes (com PITEST)
* Alguns exemplos de testes de unidade com SpockFramework

## Subindo aplicação com Docker para teste local

Com um comando você consegue subir a aplicação e o banco de dados.

```
docker-compose up
```

A aplicação sobe na porta `8080`. 

Caso deseje criar seus testes (manualmente ou automatizado) basta apontar para `http://localhost:8080`.

Você ainda pode conferir a documentação da API (swagger) em: `http://localhost:8080/swagger-ui/`.

Caso deseje fazer queries diretamente no banco, você pode utilizar um SGBD de sua preferência que tenha suporte para
`MySql`, como o Workbench, por exemplo. 

O banco fica disponível em `localhost:3306`. Usuário e senha: `root`.

## Executando os testes contidos neste projeto

Ao clonar este projeto você poderá executar todos os testes contidos aqui. 

* Testes de unidade (src/test/java):

```
./gradlew test
```

Reports em: `build/reports/tests`
Report de cobertura: `build/reports/jacoco`

* Testes de integração (src/integrationTest/java):

```
./gradlew integrationTest
```

Reports: ainda não disponível

* Testes de Mutantes:

```
./gradlew pitest
```

Reports em: `build/reports/pitest`

## Gerando nova versão da API

Caso você deseje fazer alguma alteração na API e subir uma nova imagem no docker, siga os seguintes passos:

Gere um novo Jar

```
./gradlew clean build 
```

> Ao rodar o build, os testes de unidade e integração serão executados. É feito também uma verificação com checkstyle. 
>Caso qualquer um dessas etapas falhe, não será gerado um novo .jar
>

Construa uma nova imagem:

```
docker build --tag=rest-service:latest .   
```

Com a nova imagem pronta, basta rodar:

```
docker-compose up
```









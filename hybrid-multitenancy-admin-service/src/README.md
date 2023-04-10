# Hybrid Multitenancy Tenant Admin Service

## Overview  

This app provides a simple rest interface for dynamically adding tenants.

## Running the Tenant Admin Service

Build the Tenant Admin Service executable:

```
mvn package
```

then start it via maven

```
mvn spring-boot:run
```

## Testing the Tenant Admin Service

Set up tenants for all three models:

Schema per tenant:
* schematenant1
* schematenant2

Database per tenant:
* dbtenant1
* dbtenant2

Discriminator column (shared database, shared schema) per tenant:
* abc
* xyz

```
curl -X POST "localhost:8088/tenants?tenantId=schematenant1&isolationType=SCHEMA&dbOrSchema=schematenant1&userName=schematenant1&password=postgre"
curl -X POST "localhost:8088/tenants?tenantId=schematenant2&isolationType=SCHEMA&dbOrSchema=schematenant2&userName=schematenant2&password=postgre"
curl -X POST "localhost:8088/tenants?tenantId=databasetenant1&isolationType=DATABASE&dbOrSchema=databasetenant1&userName=databasetenant1&password=postgre"
curl -X POST "localhost:8088/tenants?tenantId=databasetenant2&isolationType=DATABASE&dbOrSchema=databasetenant2&userName=databasetenant2&password=postgre"
curl -X POST "localhost:8088/tenants?tenantId=abc&isolationType=DISCRIMINATOR&dbOrSchema=public&userName=postgres&password=postgres"
curl -X POST "localhost:8088/tenants?tenantId=xyz&isolationType=DISCRIMINATOR&dbOrSchema=public&userName=postgres&password=postgres"
```


## Configuration

Change default port value and other settings in src/main/resources/application.yml.

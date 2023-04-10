# Customer Service with Hybrid Multitenancy

## Running the Service

Build the Service executable:

```
mvn package
```

then start it via maven
```
mvn spring-boot:run
```

## Testing the Service

Insert test data for different tenants per three models:

Discriminator column (shared database, shared schema) per tenant:
* abc
* xyz

Schema per tenant:
* schematenant1
* schematenant2

Database per tenant:
* dbtenant1
* dbtenant2

```
curl -H "X-TENANT-ID: abc" -H "Content-Type: application/json" -X POST -d '{"firstName":"abc","lastName":"last"}' localhost:8500/customers

curl -H "X-TENANT-ID: xyz" -H "Content-Type: application/json" -X POST -d '{"firstName":"xyz","lastName":"last"}' localhost:8500/customers

curl -H "X-TENANT-ID: schematenant1" -H "Content-Type: application/json" -X POST -d '{"firstName":"schematenant1","lastName":"last"}' localhost:8500/customers

curl -H "X-TENANT-ID: schematenant2" -H "Content-Type: application/json" -X POST -d '{"firstName":"schematenant2","lastName":"last"}' localhost:8500/customers

curl -H "X-TENANT-ID: databasetenant1" -H "Content-Type: application/json" -X POST -d '{"firstName":"databasetenant1","lastName":"last"}' localhost:8500/customers

curl -H "X-TENANT-ID: databasetenant2" -H "Content-Type: application/json" -X POST -d '{"firstName":"databasetenant2","lastName":"last"}' localhost:8500/customers

```

Then query for the data, and verify that the data is properly isolated between tenants:

```
curl -H "X-TENANT-ID: abc" localhost:8500/customers
curl -H "X-TENANT-ID: xyz" localhost:8500/customers
curl -H "X-TENANT-ID: schematenant1" localhost:8500/customers
curl -H "X-TENANT-ID: schematenant2" localhost:8500/customers
curl -H "X-TENANT-ID: databasetenant1" localhost:8500/customers
curl -H "X-TENANT-ID: databasetenant2" localhost:8500/customers
```

## Configuration

Change default port value and other settings in src/main/resources/application.yml.

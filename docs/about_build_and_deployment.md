Requirements: 
* JDK8+
* mvn3
* az cli (azure command line app)
* azure account

### Build
Java target level needs to be maximum Java8 as currently (anno march 2020), Azure functions is only supporting Java8 as runtime.

```
mvn clean install
```

## Deployment to Azure

```
az login
mvn -pl azure-packing azure-functions:deploy
```

Update CORS settings

In Azure portal
pbo-jeugdcupranking -> Platform features -> CORS
Set Allowed Origins to "*", remove all others
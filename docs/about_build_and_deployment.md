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

### Run mvn plugin
```
az login
mvn -pl azure-packing azure-functions:deploy
```

#### Update CORS settings using Azure portal
In Azure portal
pbo-jeugdcupranking -> Platform features -> CORS
Set Allowed Origins to "*", remove all others
(Or just allow https://badminton-pbo.github.io)

#### Fetch security key needed to call the function
Using Azure portal
"Resource Group = pbo-jeugdcupranking" > "App service = pbo-jeugdcupranking" > "functions = pboJeugdcupRanking" > function keys
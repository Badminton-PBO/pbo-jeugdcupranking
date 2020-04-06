# PBO Jeugdcup ranking

## Problem statement
After every PBO jeugdcup tournament, a responsible person at PBO needs to update the PBO Jeugdcupranking based on the 
tournament results. Meaning for every player, the best cross-discipline result needs to be calculated.  As this is 
rather time consuming (and needs to be done shortly after the tournament itself, so no flexibility there), a software 
utility can help with this process.

## Software solution
A Java8 app that runs in AzureCloud using AzureFunctions that is processing a single TP file 
and returns the PBO Jeugdcup points earned for every player in the tournament.

## Usage when deployent in AzureFunctions
```
# Example usage for first two tournaments where A/B reeks is not being used
curl --request POST --header "Content-Type:application/octet-stream" "https://pbo-jeugdcupranking.azurewebsites.net/api/pboJeugdcupRanking?isAlwaysUsingDoubleSchemes=true&code=<FUNCTION_KEY>" --data-binary "@PBO-Jeugdcuptour-VLABAD-2020.tp"
``` 

Or use the [upload page](https://badminton-pbo.github.io/pbo-jeugdcupranking/upload.html) available on github pages.



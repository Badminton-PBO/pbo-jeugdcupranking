# Implement QualificationScheme
* niet in PBO draaiboek, maar wordt blijkbaar (voor 't gemak?) wel eens gebruikt, bv Vlabad 2020
* QualificationScheme ombouwen naar set van EliminationScheme 
--> done 2020/03/21

# Support A/B reeks
* detect A/B reeks + modeleren
* PointService verbeteren om verschillende punten voor A/B reeks te maken

# Eerste twee toernooien altijd zonder A/B reeks
* add to configuration

# Speciale gevallen
* indien speler uiteindelijk niet speelt (bv. ziekte), dan krijgt hij ook geen punten (ik dacht dat ik ergens gelezen had.. of was dat bij reguliere competitie)
bv LeonD, JEU13, Vlabad 2020 

# Packaging
* JAVA 11 application as exec?
* Azure functions
    * uitgeprobeerd, binary uploads werken zeker tot 13MB, lijkt voldoende
    * deployment via "az login  & mvn azure-functions:deploy"
--> ongoing

# CI/CD

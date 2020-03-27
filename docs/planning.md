# Implement QualificationScheme
* niet in PBO draaiboek, maar wordt blijkbaar (voor 't gemak?) wel eens gebruikt, bv Vlabad 2020
* QualificationScheme ombouwen naar set van EliminationScheme 
--> done 2020/03/21

# Support A/B reeks
* detect A/B reeks + modeleren
* PointService verbeteren om verschillende punten voor A/B reeks te maken

# Eerste twee toernooien altijd zonder A/B reeks
* add to configuration
---> done, 2020/03/22, part of query-request

# Speciale gevallen
* indien speler uiteindelijk niet speelt (bv. ziekte), dan krijgt hij ook geen punten (ik dacht dat ik ergens gelezen had.. of was dat bij reguliere competitie)
bv LeonD, JEU13, Vlabad 2020 

# Packaging
* JAVA 11 application as exec?
* Azure functions
    * uitgeprobeerd, binary uploads werken zeker tot 13MB, lijkt voldoende
    * deployment via "az login  & mvn azure-functions:deploy"
--> done 2020/03/22

# CI/CD



# Meetings
## Call 2020/03/24
Q: Spelers die niet gespeeld hebben -> geen punten? Artikel in het reglement.
A: Mogelijks niet in huidig regelment maar we willen idd zo houden. Niet spelen => 0 punten.

Detectie AgeCategory
* Denderleeuw 2020 -> JE U11-U13 A & JE U11-U13 B
* U17=U19 -> "U17-U19"  (nieuw category) 
* done 2020/03/25

Detectie A/B reeks
* aparte events
* naam eindigt, na trimming, ignore case 
    * A
    * B
    * A-reeks
    * B-reeks
    * A reeks
    * B reeks



Verwerking in excel: volgend idee uitwerken
* 1 sheet met alle toernooi data los van leeftijdscategorie = data
* 1 sheet om nieuwe toernooi toe te voegen + vlookup vanuit data
* 1 sheet met leeftijdscategory: vblId - leeftijdscategory (+ naam)
* aparte sheets per leeftijdscategory met filter op "data"-sheet
Q: nieuwe speler
Q: onbekende leeftijdscategory
                
TODO
* spelers die niet gespeeld hebben -> geen punten, TDE, done 2020/03/27
    bv. Renske De Backer, ME U15B, Denderleeuw 2020
* A/R reeks -> TDE, done 2020/03/25
* U17-19 category: TDE, done 2020/03/25
* excel -> TDE, Melanie
* uploadPagina voor in browser: TDE, prio2 
* hosting bij Azure onder naam van PBO: Stefan, Nathan

Next call: dinsdag 31 maart, 20:30 
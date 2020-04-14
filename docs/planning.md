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
* A/B reeks -> TDE, done 2020/03/25
* U17-19 category: TDE, done 2020/03/25
* excel -> TDE, Melanie
* uploadPagina voor in browser: TDE, prio2 
* hosting bij Azure onder naam van PBO: Stefan, Nathan

Next call: dinsdag 31 maart, 20:30 

Call 31 maart
* check if isAlwaysUsingDoubleSchemes is still required... A/B reeks will never be used
  * A: ALS ze in eerste twee tournooien toch met A/B reeks zouden spelen zou dit ook zo gedetecteerd worden en worden puten volgens A/B gemaakt... natuurlijk een grote ALS
  * A: Nathan, leave the flag. You never know
* NP op witwit , done 2020/03/31
* excel, not always having VBL id --> formules more defensive, done 2020/03/31
* excel, strict tournament ordering, done 2020/03/31
  * by adding "01" prefix manually in import + sorting order fixed on tournament
* check point
Sander Vandendriessche	Badmintonclub Deinze	50566296, U13, 5de uit poule = 50 punten, WitWit
Lander Bogaert	WIT-WIT BC	2, U13, vierde uit poule = 55punten ipv. 50, WitWit
Thomas Devloo	Badmintonclub Deinze	50790927, vierde uit poule, 55 punten ipv. 50
Iñaki Mahy	Badmintonclub Deinze	51200414, vijfde uit de poule = 50 punten ipv. 45
Jules Moerman	WIT-WIT BC	51742527, vijfde uit de poule = 50 punten ipv. 45

dinsdag 7 april, 20:30
* tournooi naam (+ prefix nummer) meegeven in request, tournooi datum uit TP file halen, done 2020/04/07
* deployment AZ TODO Thomas
* ranking check TODO Nathan
* bugfix NP team1=null op QualificationScheme

14 april, 20:30
* hertesten 2019 na bugfix Nathan
* geen lidnummer => 99041412  999 + "month/day"-TP file + uniek 3 cijferig nummer binnen TPfile, Thomas, done 2020/04/14

next call 21 april, 20:30


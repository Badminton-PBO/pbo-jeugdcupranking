* Tables
    * Club
        * id
        * name: PLUIMPLUKKERS BC
        * clubid: 30027
        
    * Player
        * id
        * firstName
        * lastName
        * club = Club.id
        * gender: 1= jongen, 2=meisjes
        * memberid: vbl id
 
    * PlayerLevel
        * id
        * name: A, B1, B2, C1, C2, D, J

    * PlayerLevelEntry (klassementen per speler)
        * id
        * playerid
        * level1: PlayerLevel.id  enkel?
        * level2: PlayerLevel.id  dubbel?  
        * level3: PlayerLevel.id  mix?
        
    * Event
        * id
        * name: example JD U11, JE U17, Minibad
        * gender: 4=Jongens, 5=Meisjes, 6=minibad 
        * eventtype: 1= enkel, 2= dubbel, 3 ?= mix  

    * Entry (~inschrijving van team/speler op een event)
        * id 
        * Event: Event.id
        * player1: Player.id from first/only player
        * player2: Player.id from second player   

    * Draw (indeling van event in poules en afvalschema's)
        * id
        * name: JE U11 Poule A, JD U11 Poule 11, JE U11 1-2, ME U11
            * Reeks > Groep (soms Poule genoemd)
            * A-B-reeks kan je enkel afleiden obv. naam + drawgroup=1

            * A-reeks en B-reeks spelen nooit tegen elkaar ... zie bv. ME U17 in Lokerse
            * "U10" = "Minibad" 
                https://www.toernooi.nl/sport/events.aspx?id=9F8654A4-805B-428F-BE85-3FCE36EDD58D
        * event: Event.Id
        * drawtype: 2= poule, 1= afvalschema
        * drawsize: # spelers in pot
        * ?drawcolumns: # kolommen nodig inboomstructuur tussen beginnende teams &  winnaar, bij afvalschema met meer dan 2 spelers 
        * drawgroup:
            * alle poules binnen eenzelfde reeks hebben zelfde nummer die verschilt van andere reeks binnen zelfde event
            * alle afvalschema binnen eenzelfde reeks, volgend op een poulefasen  hebben zelfde nummer die verschilt van andere afvalschema binnen zelfde event
        * ?position
     
    * Link
        * id
        * ?src_draw: Draw.id
        * ?src_pos
        * ?intsrc_pos
        * name
        
    * PlayerMatch (matchen)
        * records met van1 & van2 != null && != 0: te spelen matchen
            * id
            * entry: null
            * van1 = PlayerMatch.planning team1
            * van2 = PlayerMatch.planning team2
            * winner: 1=van1 winner, 2=van2 winner
            * reversehomeaway: true/false , records komen dubbel voor met van1 en van2 geswitched
            * ?stage
            * ?matchnr
            * ?roundnr
        * records van1/van2 == null of 0: links naar beide team
            * id
            * entry: Entry.id  (link naar players)
            
Problems
   * Voorgeschreven indeling wordt niet altijd gevolgd
        bv. 12 inschrijvingen maar geen A/B reeks, JE U15 en JE U13
            https://www.toernooi.nl/sport/event.aspx?id=51A99A99-D53B-4F30-A439-558B0E605F5C&event=7 (Vlabad)
        ook met 2 of drie inschrijvingen wordt gespeeld, worden er dan punten uitgedeeld?
            https://www.toernooi.nl/sport/events.aspx?id=7C6F97EE-6231-4F6E-8955-8DADFE6E571A
   * Soms aparte event per A/B reeks
        https://www.toernooi.nl/sport/events.aspx?id=0E933C89-C4C7-4500-AEFD-0B0003133DD2 (GentseBC)
        https://www.toernooi.nl/sport/events.aspx?id=E5AE0813-148F-444F-9F25-FC445184B2A0 (Oudegem BC)
        https://www.toernooi.nl/sport/events.aspx?id=953E3DC3-E89C-4E34-9195-3A6AEDE7197F (Drive)
   * ...soms niet
        https://www.toernooi.nl/sport/events.aspx?id=9F8654A4-805B-428F-BE85-3FCE36EDD58D (Lokerse)
   * Hoe reeks afleiden?
                   * JE U13 B-Reeks Groep A
                   * JE U15 REEKS B - JE U15 REEKS B 1 - 4
                   * "Groep 1" / "Groep 2"
        Draw name U10 == "Minibad"
   * Poule grootte != aantal spelers 
        "Bye" speler ? https://www.toernooi.nl/sport/draw.aspx?id=A8C1CBA3-3549-4354-A673-6694714F61B2&draw=30
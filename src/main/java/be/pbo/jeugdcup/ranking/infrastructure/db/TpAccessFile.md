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
            * A-reeks en B-reeks spelen nooit tegen elkaar ... ME U17 in Lokerse
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
        * records met van1 & van2: te spelen matchen
            * id
            * entry: null
            * van1 = PlayerMatch.planning team1
            * van2 = PlayerMatch.planning team2
            * winner: 1=van1 winner, 2=van2 winner
            * reversehomeaway: true/false , records komen dubbel voor met van1 en van2 geswitched
            * ?stage
            * ?matchnr
            * ?roundnr
        * records zonder van1 & van2: links naar beide team
            * id
            * entry: Entry.id  (link naar players)
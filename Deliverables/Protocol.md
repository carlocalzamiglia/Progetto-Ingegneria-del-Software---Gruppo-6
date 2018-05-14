# PROTOCOL FOR SOCKET

### LOGIN PART:
##### Client:
@NICK-user
##### Server:
@USEROK-true/false

### GAME PREP:
##### Client:
@READYTOPLAY
##### Server:
@WAIT-waiting for more users


### REAL GAME:
##### Server:
@START-game started
@TURN-your turn
@ENDTURN-your turn is ended


### GAME TOOLS:
##### Server:
@GC-greencarpetstring		(include dices, public goals, rounds)
@SCH@USER-schemestring		(print other player scheme)
@PLAYER.playerstring		(include scheme, name, markers, private goals)
##### Client:
@GAME@PASS
@GAME@CHOOSEDICE@DiceAndCoordinates
@GAME@USETOOLCARD@ToolCardNumber@ToolCardUtilization


### GAME END:
##### Server:
@RANK@USER-POINTS@USER-POINTS@USER-POINTS@USER-POINTS
@NEWMATCH


### CONNECTION/DISCONNECTION PART:
##### Client:
@DISCONNECTION
##### Server:
@CHECKCONN










# PROTOCOL FOR SOCKET

### LOGIN PART:
##### Client:
###### @NICK-user
##### Server:
###### @USEROK-true/false




### GAME:
##### Server:
###### @SCHEME-scheme1-scheme2-scheme3-scheme4
###### @YOURTURN-true/false
###### @SHOWSCORE-score
###### @PRINTALL-greencarpetjson-playerjson
###### @CHOOSEACTION
###### @ERROR-Error
###### @PLACEDICE
###### @ERRORPLACEDICE-Message
###### @AFTERTOOLUPDATE-greencarpet-player
###### @USETOOL
##### Client:
###### @SCHEME-numofthescheme
###### @DICEPLACED-numofdice-row-col
###### @ACTIONCHOSE-action


### TOOL GAME
##### Server:
###### @TOOL-1
###### @TOOL-2
###### @TOOL-3
###### @TOOL-4
###### @TOOL-5
###### @TOOL-6
###### @TOOL-61
###### @TOOL-7
###### @TOOL-8
###### @TOOL-91
###### @TOOL-92
###### @TOOL-1
##### Client:
###### @TOOLUSED1-dicerow-dicecol-newrow-newcol
###### @TOOLUSED2-
###### @TOOLUSED3-
###### @TOOLUSED4-dice-choice
###### @TOOLUSED5-dice-dicerow-dicecol
###### @TOOLUSED6-dice
###### @TOOLUSED61-dicerow-dicecol
###### @TOOLUSED7-1
###### @TOOLUSED8-dice-dicerow-dicecol
###### @TOOLUSED91-value
###### @TOOLUSED92-value1-value2



### TIMER:
###### @TIMEROUT


### GAME END:
##### Server:
###### @ENDGAMEACTION
##### Client:
###### @ENDGAMEACTION-true/false



### CONNECTION/DISCONNECTION PART:
##### Client:
###### @DEAD
##### Server:
###### @CONNDISC-userconnected/disc










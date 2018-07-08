<p align="center"> 
<img src="polimi_logo.png">
</p>

# PROGETTO DI INGEGNERIA DEL SOFTWARE - GRUPPO 6

### COMPONENTI:
- Calzamiglia Carlo
- Cecini Matteo
- Cicciarelo Alberto

## CONTENUTO:

##### I file jar si trovano nella cartella "Deliverables" -> "Jar" -> "Client" / "Server".
##### Si trovano nella cartella "src" -> "main" -> "java" i file "server_config.txt" e "client_config.txt" (rispettivamente in ogni cartella dei jar)

### SERVER_CONFIG:
##### All'interno del file sono contenuti:
- Porta connessione socket
- Porta connessione rmi
- Durata del turno (questo valore diviso tre volte restituisce anche la durata del matchmaking)

### CLIENT_CONFIG
##### All'interno del file sono contenuti:
- indirizzo ip server:porta (per connessione socket)
- indirizzo ip server:porta del server:ip del client:porta export client (per connessione rmi)


## ESECUZIONE DEI FILE JAR:
##### I file devono essere eseguiti aggiungendo alcuni parametri:
- Socket: file.jar gui/cli socket
- Rmi: file.jar gui/cli rmi

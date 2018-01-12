tweet-pulbisher
=========

Ce projet permet d'envoyer des tweets lu depuis un fichier ligne par ligne

Utilisation
--------------
``` shell
java -jar tweet-publisher -s "server1:port1 server2:port2... " -t <topic-name> -f <file-path> [-d <delay>] [-l]
```

les paramettres  utilisé sont:
- **-s:** l'ensemble des serveurs kafka sous la forme : serveur1:port1 serveur2:port2...
- **-t:** le topic vers où envoyer les messages
- **-f:** le chemin vers le fichier à lire (un message par ligne sera envoyé)
- **-d:** le temps d'attente entre deus envoi
- **-l:** boucler indefiniment

Exemple
----------------
``` shell
java -jar tweet-publisher -s "localhost:9092" -t "test topic" -f /tmp/tweet.json -d 2000 -l
```

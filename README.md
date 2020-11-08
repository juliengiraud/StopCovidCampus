# MIF03-TP

Un dépôt de qualité

## Membres du binôme

- 11702137 Jérémy Thomas
- 11704709 Julien Giraud

### TP 3 : Design patterns côté serveur en Java

#### Utilisation des en-têtes HTTP de date

La date recupérée dans l'en-tête If-Modified-Since est moins précise que la date stockée par le serveur par dans la variable d'instance de type Date(). En effet, les dates HTTP n'incluent pas les millisecondes alors que la méthode java.util.Date.getTime() retourne combien de  millisecondes se sont écoulées depuis le 1er Janvier 1970 à 00:00:00 GMT.

Afin de comparer les deux valeurs, nous avons donc décidé d'arrondir les valeurs à la seconde près en les divisant puis en les multipliant par 1000.
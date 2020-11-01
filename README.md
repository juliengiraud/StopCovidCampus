# MIF03-TP

Un dépôt de qualité

## Membres du binôme

- 11702137 Jérémy Thomas
- 11704709 Julien Giraud

### TP 2 : Programmation Java côté serveur

#### Bibliothèques supplémentaires utilisées

Afin d'obtenir une meilleure mise en forme, nous avons choisis d'utiliser la bibliothèque Bootstrap.

#### Choix d'implémentation

##### 1.5 Amélioration des fonctionnalités

###### Cas contacts

Pour savoir quels utilisateurs étaient dans la même salle qu'un autre utilisateur U en même temps, nous regardons pour chaque passage de l'utilisateur U si un autre utilisateur a été dans la même salle. Si oui, nous regardons s'ils se sont croisés à l'aide d'une comparaison d'intervalle en comparant leurs dates d'arrivée et de départ. Si l'utilisateur U était dans la salle durant un intervalle [a, b] et que l'utilisateur U2 était dans la salle durant un intervalle [c, d], alors U et U2 se sont croisés si a est inclu dans [c, d] ou si b est inclu dans [c, d], ou si c est inclu dans [a, b] ou si d inclus dans [a, b].
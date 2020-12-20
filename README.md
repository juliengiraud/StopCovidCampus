# MIF03-TP

Un dépôt de qualité

## Membres du binôme

- 11702137 Jérémy Thomas
- 11704709 Julien Giraud

### TP 5 : Programmation côté client (requêtage asynchrone)
#### Choix de l'API

Nous avons décidé pour ce TP d'utiliser l'API que nous avons développé au TP4 car elle fonctionnelle. (Attention : la requête GET sur /salles/{salleId} renvoie un objet avec pour attribut "nom" au lieu de "nomSalle")

#### Ce n'est pas un bug, c'est une fonctionnalité
Lors du TP4, l'API développée ne permettait qu'aux utilisateurs administrateurs de récupérer la liste des salles. Les utilisateurs non administrateurs doivent donc "deviner" les salles qui existent car ils n'ont pas accès à la liste (il n'est pas donc pas possible d'utiliser un élément datalist sur le champs input de salle lors de la saisie d'un passage). En réalité, cela ne poserait pas un problème majeur car si on rentre dans une salle, c'est qu'on connait le nom de cette salle.

#### Ca par contre, c'est un bug
L'application n'est accessible qu'à l'adresse http://192.168.75.76:8080/client. Pour une raison inconnue, nous n'arrivons pas à requêter notre API depuis l'adresse  https://192.168.75.76/api/client. Chaque requête renvoie une erreur 403, y compris la requête vers /users/login.

Nous avons pourtant réussi à requêter notre api sur https://192.168.75.76/api/v3/ avec postman.

Il n'est pas possible de créer d'utilisateur ou de salle avec un espace dans le nom. En effet, lors d'une requête get vers /users/{userId} ou /salles/{salleId}, nous n'avions pas pensé à décoder l'url. Lors d'une requête GET vers l'utilisateur "toto titi" par exemple, l'url sera /users/toto%20titi, et l'API cherchera l'utilisateur ayant pour login toto%20titi, mais il n'existe pas. Idem pour les salles.
# MIF03-TP

Un dépôt de qualité

## Membres du binôme

- 11702137 Jérémy Thomas
- 11704709 Julien Giraud

## TP 7 : Optimisation d'une Single-Page Application

### Script utilisé pour la réalisation des mesures 

```javascript
console.log("Temps de chargement de la page HTML initiale :", window.performance.timing.responseEnd - window.performance.timeOrigin, " ms") ; 
console.log("Temps d'affichage de l'app shell : ", window.performance.timing.domInteractive - window.performance.timing.domLoading, " ms");
console.log("Temps d'affichage du chemin critique de rendu (CRP) : ", window.performance.timing.domComplete - window.performance.timing.domLoading, " ms");
```

## 1. Analyse de l'état initial de l'application (déploiement sur Tomcat)

Mesure | Temps de chargement de la page HTML initiale (en ms) | Temps d'affichage de l'app shell (en ms) | Temps d'affichage du chemin critique de rendu (CRP) (en ms)
:---:|:---: | :---: | :---:
1 | 57 | 292 | 310
2 | 47 | 237 | 262
3 | 48 | 363 | 373
4 | 58 | 432 | 415
5 | 45 | 426 | 429
6 | 52 | 366 | 378
7 | 56 | 543 | 533
8 | 42 | 357 | 368
9 | 51 | 437 | 442
10 | 49 | 575 | 221
Moyenne | 50.4 | 402.8 | 407.3

## 2. Déploiement des fichiers statiques sur nginx (déploiement sur nginx)

Mesure | Temps de chargement de la page HTML initiale (en ms) | Temps d'affichage de l'app shell (en ms) | Temps d'affichage du chemin critique de rendu (CRP) (en ms)
:---:|:---: | :---: | :---:
1 | 32 | 159 | 200
2 | 39 | 247 | 297
3 | 32 | 236 | 289
4 | 34 | 186 | 225
5 | 38 | 205 | 265
6 | 35 | 219 | 275
7 | 31 | 163 | 199
8 | 42 | 247 | 322
9 | 40 | 202 | 261
10 | 34 | 220 | 27
Moyenne | 35.7 | 208.4 | 236
Pourcentage d'amélioration | 29.2% | 48.3% | 42.1%

## 3. Optimisation de votre application
### Premier rapport d'audit, avant toute modification.
![Premier rapport](./rapports_audit/Rapport_Init.png)

### Optimisation pour les moteurs de recherche (SEO)
- Rapport après ajout d'une balise ```<meta name="viewport">``` pour contrôler la mise en page sur les navigateurs mobiles.
![Rapport après ajout d'une balise meta viewport pour contrôler la mise en page sur les navigateurs mobiles.](./rapports_audit/Rapport_SEO_MetaViewport.png)

- Rapport après ajout d'une balise ```<meta name="description">``` pour contrôler la mise en page sur les navigateurs mobiles.
![Rapport après ajout d'une balise meta description pour ajouter une courte description à une page web, utilisée par les navigateurs.](./rapports_audit/Rapport_SEO_MetaDescription.png)

### Optimisation de la performance
- ```Utilisation de CDN```
	- Notre utilise déjà des CDN pour Bootstrap, Mustache et jQuery.
		![Utilisation de CDN.](./rapports_audit/Rapport_CDN.png)

- ```Utilisation d'attributs async et/ou defer pour décaler le chargement de scripts non nécessaires au CRP```
	- Pas de modifications : Notre script utilise déjà des appels ajax asynchrones (par défaut, la valeur de l'option async de jQuery.ajax() est true et nous ne l'avons pas modifié).

- ```Minification réduction du nombre de ressources critiques```
	- Les balises ```link``` sont déjà placées en tête du document et le seul code CSS que nous avons écrit est à également en tête du document html. 
	- Modifications : Les scripts js sont désormais placés en fin de page et ajout de l'attribut ```javascript async``` pour les scrips que nous avons programmé. Les CDN (sauf Bootstrap) n'y ont pas droit car ils sont utilisés par notre scripts et async ne garanti pas l'ordre d'exécution. 
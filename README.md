# MIF03-TP

Un dépôt de qualité

## Membres du binôme

- 11702137 Jérémy Thomas
- 11704709 Julien Giraud

### TP 7 : Optimisation d'une Single-Page Application
#### 1. Analyse de l'état initial de votre application
##### Script utilisé
```javascript
console.log("Temps de chargement de la page HTML initiale :", window.performance.timing.responseEnd - window.performance.timeOrigin, " ms") ; 
console.log("Temps d'affichage de l'app shell : ", performance.timing.loadEventEnd - performance.timeOrigin, " ms");
console.log("Temps d'affichage du chemin critique de rendu (CRP) : ", performance.timing.loadEventStart - performance.timeOrigin, " ms");
```
##### Mesures réalisées en ms (déploiement sur Tomcat)

Temps de chargement de la page HTML initiale | Temps d'affichage de l'app shell | Temps d'affichage du chemin critique de rendu (CRP)
:---: | :---: | :---:
61|209|208
50|190|189
51|234|233
62|352|351
48|256|255
56|235|235
60|597|296
45|232|231
55|260|260
53|308|307
`54,1`|`257,3`|`256,5`
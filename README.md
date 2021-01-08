# MIF03-TP

Un dépôt de qualité

## Membres du binôme

- 11702137 Jérémy Thomas
- 11704709 Julien Giraud

## TP 7 : Optimisation d'une Single-Page Application
## 1. Analyse de l'état initial de l'application (déploiement sur Tomcat)
### Script utilisé

```javascript
console.log("Temps de chargement de la page HTML initiale :", window.performance.timing.responseEnd - window.performance.timeOrigin, " ms") ; 
console.log("Temps d'affichage de l'app shell : ", window.performance.timing.domInteractive - window.performance.timing.domLoading, " ms");
console.log("Temps d'affichage du chemin critique de rendu (CRP) : ", window.performance.timing.domComplete - window.performance.timing.domLoading, " ms");
```
### Mesures réalisées en ms 

Mesure | Temps de chargement de la page HTML initiale (en ms) | Temps d'affichage de l'app shell (en ms) | Temps d'affichage du chemin critique de rendu (CRP) (en ms)
:---:|:---: | :---: | :---:
1 | 61 | 102 | 122
2 | 50 | 83 | 103
3 | 51 | 127 | 147
4 | 62 | 245 | 265
5 | 48 | 149 | 169
6 | 56 | 128 | 149
7 | 60 | 190 | 210
8 | 45 | 125 | 145
9 | 55 | 153 | 174
10 | 53 | 201 | 221
Moyenne | 54.1 | 150.3 | 170.5
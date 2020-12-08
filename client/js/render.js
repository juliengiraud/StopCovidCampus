const DATA = {
  "salles": [
  ],
  "salle": null,
  "passages": [
    {
      "login": "login",
      "salle": "salle",
      "entree": "entree",
      "sortie": "sortie"
    },
    {
      "login": "login",
      "salle": "salle",
      "entree": "entree",
      "sortie": "sortie"
    },
  ],
  "loggedUser": {
    "login": "login",
    "nom": "nom",
    "admin": false
  }
}

function render() {
  const templates = ["loggedUser", "passages"];
  for (const template of templates) {
    doRender(template);
  }
}

function doRender(templateName) {
  const template = document.getElementById(templateName).innerHTML;
  const rendered = Mustache.render(template, { data: DATA[templateName] });
  console.log(templateName, DATA[templateName]);
  document.getElementById(templateName + 'Template').innerHTML = rendered;
}

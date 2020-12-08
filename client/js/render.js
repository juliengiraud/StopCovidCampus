mockObjects = [
  {
    "nomSalle": "",
    "capacite": -1,
    "presents": 0,
    "saturee": false
  }
]

function render(scriptId, data, elementId) {
  var template = document.getElementById('template').innerHTML;
  var rendered = Mustache.render(template, { name: 'Luke' });
  document.getElementById('target').innerHTML = rendered;
}

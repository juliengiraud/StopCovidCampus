const DATA = {};

function render(id) {
  const template = $("#" + id + "-template").html();
  const rendered = Mustache.render(template, { data: DATA[id] });
  $("#" + id + "-content").html(rendered);
}
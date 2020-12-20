let DATA = {};

function render(id) {
  console.log(id);
  const template = $("#" + id + "-template").html();
  const rendered = Mustache.render(template, { data: DATA[id] });
  $("#" + id + "-content").html(rendered);
}
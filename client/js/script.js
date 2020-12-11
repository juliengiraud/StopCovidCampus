$(document).ready(() => {
    $('*[id*=-template]').each(function() {
        Mustache.parse($(this).html());
    });
    initEvents();
})

function initEvents() {

    // Déplier/replier le menu quand on clique sur son titre
    $('.navbar-brand').click(() => {
        $('.navbar-nav').toggle("fast");
    });

    // Mettre en place un mécanisme de routage qui affiche la vue correspondant au hash sélectionné
    $('.nav-link').click(function() {
        $('section').hide();
        let id = $(this).attr("href").replace("#", "");

        switch (id) {
            case "mon-compte":
                //AJAX vers /users/{userId}
                DATA[id] = {
                    "login": "login",
                    "nom": "nom",
                    "admin": false
                };
                render(id);
                break;

            case "tous-mes-passages":
                // AJAX GET vers /passages/byUser/{userId}
                DATA[id] = [
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
                ];
                render(id);
                break;

            default:
        }

        $("#" + id).toggle("fast");
    });
}
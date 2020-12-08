$(document).ready(() => {
    initEvents();
})

function initEvents() {

    // Déplier/replier le menu quand on clique sur son titre
    $('.navbar-brand').click(function() {
        $('.navbar-nav').toggle("fast");
    });

    // Mettre en place un mécanisme de routage qui affiche la vue correspondant au hash sélectionné
    $('.nav-link').click(function() {
        $('section').hide();
        let id = $(this).attr("href");
        $(id).toggle("fast");
    });

}


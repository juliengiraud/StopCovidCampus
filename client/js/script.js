$(document).ready(() => {
    // Optimisation du chargement des templates en les pré-compilant
    $('*[id*=-template]').each(function() {
        Mustache.parse($(this).html());
    });

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
                $.ajax(
                    {
                        url: "https://192.168.75.76/api/v3/users/" + DATA["loggedUser"],
                        success: function(result){
                            DATA[id] = result;
                        }
                    }
                );

                render(id);
                break;

            case "tous-mes-passages":
                /*
                    AJAX GET vers /passages/byUser/{userId}, retourne un tableau qui contient l'ensemble des urls vers les passages de l'utilisateur de type :
                    [
                      "http://localhost:8080/passages/1",
                      "http://localhost:8080/passages/5"
                    ]
                 */
                let passagesUrl = [];
                $.ajax(
                    {
                        url: "https://192.168.75.76/api/v3/passages/byUser/" + DATA["loggedUser"],
                        type: "GET",
                        success: function(result, status){
                            passagesUrl = result;
                        },
                        error: function(result, status, error) {
                            $('#errMsg').html(result, status, error);
                        },
                        complete: function (result, status) {
                            $('#errMsg').html(result, status);
                        }
                    }
                );


                const passages = [];

                $.each(passagesUrl, (value) => {
                    /*
                        AJAX vers chaque passage de l'utilisateur par leur url. Renvoie un objet du type :
                        {
                          "user": "string",
                          "salle": "string",
                          "dateEntree": "string",
                          "dateSortie": "string"
                        }
                     */
                    $.ajax(
                        {
                            url: "https://192.168.75.76/api/v3/passages/" + value,
                            type: "GET",
                            success: function(result){
                                passages.push(result);
                            },
                            error: function(result, status, error) {
                                $('#errMsg').html(result, status, error);
                            },
                            complete: function (result, status) {
                                $('#errMsg').html(result, status);
                            }
                        }
                    );
                });

                DATA[id] = passages;

                render(id);
                break;

            default:
        }

        $("#" + id).toggle("fast");
    });
});
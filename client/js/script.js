const urlAPI = "https://192.168.75.76/api/v3/";
const urlLocal = "http://localhost:8080/tp4/";

/**
 * Exécute les instructions lorsque le document est prêt et que tous les objets sont créés
 */
$(document).ready(() => {
    // Optimisation du chargement des templates en les pré-compilant
    $('*[id*=-template]').each(function() {
        Mustache.parse($(this).html());
    });
    getMenu();
    initEvents();
})

/**
 * Initialise les évènement de chaque boutons
 */
function initEvents() {

    // Déplier/replier le menu quand on clique sur son titre
    $('.navbar-brand').click(() => {
        $('.navbar-nav').toggle("fast");
    });

    // Bouton de connexion
    $('#accueil form button').click((event) => {
        event.preventDefault()
        event.stopPropagation();
        connexion();
    });

    // Bouton de déconnexion
    $('#deconnexion form button').click((event) => {
        event.preventDefault()
        event.stopPropagation();
        deconnexion();
    });

    // Bouton de modification du nom
    $('#mon-compte form button').click((event) => {
        event.preventDefault()
        event.stopPropagation();
        updateNom();
    });

    // Bouton de création de salle
    $('#salles form button').click((event) => {
        event.preventDefault()
        event.stopPropagation();
        saveSalle();
    });

    // Bouton de création d'une entrée
    $('#entree form button').click((event) => {
        event.preventDefault()
        event.stopPropagation();
        savePassage("entree");
    });

    // Bouton de création d'une sortie
    $('#sortie form button').click((event) => {
        event.preventDefault()
        event.stopPropagation();
        savePassage("sortie");
    });

    // Mettre en place un mécanisme de routage qui affiche la vue correspondant au hash sélectionné
    $(window).on("popstate", () => {
        $("#msg").empty().hide();
        $('section').hide();
        let view = window.location.href.substr(window.location.href.indexOf("#")+1);

        // Rediriger l'utilisateur vers l'accueil s'il n'est pas connecté
        if (view !== "accueil" && DATA.loggedUser === undefined) {
            $("#msg").html("Vous n'êtes pas connecté.").addClass("alert-danger").show("fast");
            view ="accueil";
        }

        switch (view) {
            // todo gérer l'asynchrone
            case "accueil":
                // Ne pas charger les données de l'accueil si l'utilisateur n'est pas connecté
                if (DATA.loggedUser !== undefined) {
                    $.when(
                        getPassagesEnCours(DATA.loggedUser.login, view)
                    ).then(
                        render(view)
                    )
                }
                break;

            case "mon-compte":
                $.when(
                    getUserInfos(DATA.loggedUser.login, view)
                ).then(
                    render(view)
                )
                break;

            case "salles":
                $.when(
                    getSalles()
                ).then(
                    render(view)
                )
                break;

            case "tous-mes-passages":
                $.when(
                    getPassages(DATA.loggedUser.login, view)
                ).then(
                    render(view)
                )
                break;
        }

        $("#" + view).show();
    });
}


/**
 * Envoie une requête de connexion et recupère que le token
 */
function connexion() {
    // Récupération des données du formulaire de connexion
    let user = {
        "login": $('#accueil form input[name="login"]').val(),
        "nom": $('#accueil form input[name="nom"]').val(),
        "admin": $('#accueil form input[name="admin"]').is(":checked")
    }

    $.ajax({
        url: urlLocal + "users/login",
        type: "POST",
        dataType: "json",
        data: JSON.stringify(user),
        contentType: "application/json"
    }).done((data, textStatus, jqXHR) => {
        // Récupération du token jwt
        user.token = jqXHR.getResponseHeader("Authorization");
        DATA.loggedUser = user;
        // Cacher le formulaire de connexion
        $("#accueil form").hide();
        showMsg("Vous êtes connecté.", "success");
        getMenu();
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    });
}

/**
 * Libère le tableau DATA et redirige vers l'accueil avec le formulaire de connexion
 */
function deconnexion() {
    $.ajax({
        url: urlLocal + "users/logout",
        type: "POST",
        headers: {
            Authorization : DATA.loggedUser.token
        },
    }).done(() => {
        // Vider le tableau de données et rediriger vers l'accueil et afficher le formulaire de connexion
        DATA = [];
        $("#accueil form").show();
        window.location.href = urlLocal + "static/client/#accueil";
        showMsg("Vous êtes déconnecté.", "success");
        getMenu();
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    });
}

/**
 * Modifie le nom de l'utilisateur
 */
function updateNom() {
    // Récupération des données du formulaire de connexion
    let nom = {
        "nom": $('#mon-compte form input[name="nom"]').val()
    }

    $.ajax({
        url: urlLocal + "users/" + DATA.loggedUser.login + "/nom",
        type: "PUT",
        headers: {
            Authorization : DATA.loggedUser.token
        },
        dataType: "json",
        data: JSON.stringify(nom),
        contentType: "application/json"
    }).done((data, textStatus, jqXHR) => {
        DATA.loggedUser.nom = nom.nom;
        showMsg("Votre nom a bien été modifié.", "success");
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    });
}

/**
 * Récupère les données de la salle par son url et les ajoute au tableau DATA
 * @param url
 */
function getSalleByUrl(url) {
    DATA.salles = [];
    $.ajax({
        url: url,
        type: "GET",
        headers: {
            Authorization : DATA.loggedUser.token
        },
        accept: "application/json"
    }).done((data, textStatus, request) => {
        DATA.salles.push(data);
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    })
}

/**
 * Récupère la liste des url des salles et pour chaque url, fait appel à la fonction qui ajoute les données de la salle correspondante au tableau DATA
 */
function getSalles() {
    $.ajax({
        url: urlLocal + "salles",
        type: "GET",
        headers: {
            Authorization : DATA.loggedUser.token
        },
        accept: "application/json"
    }).done((data, textStatus, request) => {
        $(data).each((index, value) => {
            getSalleByUrl(value);
        });
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    });
}

/**
 * Récupère les données de l'utilisateur correspondant au login et les insère dans le tableau DATA à l'indice "key"
 * @param login
 * @param key
 */
function getUserInfos(login, key) {
    $.ajax({
        url: urlLocal + "users/" + DATA.loggedUser.login,
        type: "GET",
        accept: "application/json",
        headers: {
            Authorization : DATA.loggedUser.token
        },
    }).done((data, textStatus, request) => {
        DATA[key] = data;
    }).fail(function(jqXHR, textStatus, errorThrown) {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    });
}

/**
 * Envoie une requête de création de salle et rafraichit le template
 */
function saveSalle() {
    // Récupération des données du formulaire de création de la salle
    let salle = {
        "nomSalle": $('#salles form input[name="nomSalle"]').val()
    }

    $.ajax({
        url: urlLocal + "salles",
        type: "POST",
        dataType: "json",
        data: JSON.stringify(salle),
        headers: {
            Authorization : DATA.loggedUser.token
        },
        contentType: "application/json"
    }).done((data, textStatus, jqXHR) => {
        showMsg("La salle a bien été créée.", "success");
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    }).then(() => {
        window.location.href = urlLocal + "#salles";
    });
}

/**
 *
 * Récupère la liste des url des passages en cours de l'utilisateur associé au login et pour chaque url,
 * puis fait appel à la fonction qui ajoute les données du passage correspondant au tableau DATA à l'indice "key"
 */
function getPassagesEnCours(login, key) {
    $.ajax({
        url: urlLocal + "passages/byUser/" + login + "/enCours",
        type: "GET",
        accept: "application/json",
        headers: {
            Authorization : DATA.loggedUser.token
        },
    }).done((data, textStatus, request) => {
        $(data).each((index, value) => {
            getPassagesFromUrl(value, key);
        })
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    });
}

/**
 *
 * Récupère la liste des url des passages de l'utilisateur associé au login et pour chaque url,
 * puis fait appel à la fonction qui ajoute les données du passage correspondant au tableau DATA à l'indice "key"
 */
function getPassages(login, key) {
    $.ajax({
        url: urlLocal + "passages/byUser/" + login,
        type: "GET",
        accept: "application/json",
        headers: {
            Authorization : DATA.loggedUser.token
        },
    }).done((data, textStatus, request) => {
        $(data).each((index, value) => {
            getPassagesFromUrl(value, key);
        })
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    });
}

/**
 * Récupère les données du passage par son url et les ajoute au tableau DATA à l'indice key
 * @param url
 * @param key
 */
function getPassagesFromUrl(url, key) {
    DATA[key] = [];
    $.ajax({
        url: url,
        type: "GET",
        accept: "application/json",
        headers: {
            Authorization : DATA.loggedUser.token
        },
    }).done((data, textStatus, request) => {
        DATA[key].push(data);
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    });
}

/**
 * Ajoute un passage, entrée ou sortie
 * @param type
 */
function savePassage(type) {
    //todo gérer les date
    let passage = {
        "user": DATA.loggedUser.login,
        "salle": $('#entree form input[name="salle"]').val(),
        "dateEntree":"",
        "dateSortie":""
    }
    if (type == "entree") {
        passage.dateEntree = '"' + Date.now() + '"';
    } else if (type == "sortie") {
        passage.dateSortie = '"' + Date.now() + '"';
    }

    $.ajax({
        url: urlLocal + "passages",
        type: "POST",
        headers: {
            Authorization : DATA.loggedUser.token
        },
        dataType: "json",
        data: JSON.stringify(passage),
        contentType: "application/json"
    }).done((data, textStatus, jqXHR) => {
        DATA.loggedUser.nom = nom.nom;
        showMsg("Votre nom a bien été modifié.", "success");
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    });
}

/**
 * Génère le menu en fonction de si l'utilisateur est connecté, est admin..
 */
function getMenu() {
    let menu = "";
    menu += "<li class=\"nav-item\">\n" +
                "<a class=\"nav-link\" href=\"#accueil\">Accueil</a>\n" +
            "</li>";
    if (DATA.loggedUser !== undefined) {
        if (DATA.loggedUser.admin) {
            menu += " <li class=\"nav-item\">\n" +
                "<a class=\"nav-link\" href=\"#salles\">Salles</a>\n" +
                "</li>\n";
        }
        menu +="<li class=\"nav-item\">\n" +
                    "<a class=\"nav-link\" href=\"#mon-compte\">Mon compte</a>\n" +
                "</li>\n" +
                "<li class=\"nav-item\">\n" +
                    "<a class=\"nav-link\" href=\"#entree\">Entrée</a>\n" +
                "</li>\n" +
                "<li class=\"nav-item\">\n" +
                    "<a class=\"nav-link\" href=\"#sortie\">Sortie</a>\n" +
                "</li>\n" +
                "<li class=\"nav-item\">\n" +
                    "<a class=\"nav-link\" href=\"#tous-mes-passages\">Tous mes passages</a>\n" +
                "</li>\n" +
                "<li class=\"nav-item\">\n" +
                    "<a class=\"nav-link\" href=\"#deconnexion\">Déconnexion</a>\n" +
                "</li>";
    }
    $(".navbar-nav").html(menu);
}

/**
 * Affiche un message sous le menu, affichage d'une erreur ou d'un message de succès
 * @param text
 * @param type
 */
function showMsg(text, type) {
    if (type == "success") {
        $("#msg").html(text).addClass("alert-success").removeClass("alert-danger").show("fast");
    } else if (type == "error") {
        $("#msg").html(text).removeClass("alert-success").addClass("alert-danger").show("fast");
    } else {
        $("#msg").html(text).removeClass("alert-success").removeClass("alert-danger").show("fast");
    }
}
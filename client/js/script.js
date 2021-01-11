const apiUrl = getApiUrl();
const clientUrl = getClientUrl();
let passagesInterval;

/**
 * Exécute les instructions lorsque le document est prêt et que tous les objets sont créés
 */
$(document).ready(() => {
    // Optimisation du chargement des templates en les pré-compilant
    $('*[id*=-template]').each(function() {
        Mustache.parse($(this).html());
    });
    getTime();
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

    // Bouton de modification du nom depuis le formulaire
    $('#mon-compte form button').click((event) => {
        event.preventDefault()
        event.stopPropagation();
        let nom = {
            "nom": $('#mon-compte form input[name="nom"]').val()
        };
        updateNom(nom);
    });

    //Bouton de modification du nom depuis le champs nom
    $('#mon-compte').on('click', '.update-nom', function() {
        $(this).parent().find('.validate, .cancel').show();
        $(this).parent().find('.update-nom').hide();
		let input = $(this).parent().find('#nom');
		if (input.html() == '') {
			input.html("&nbsp;&nbsp;&nbsp;")
		}
        input.attr('contenteditable', 'true').focus();
    });

    //Validation de la modification du nom depuis le champs nom
    $('#mon-compte').on('click', '.validate', function() {
        $(this).parent().find('.update-nom').show();
        $(this).parent().find('.validate, .cancel').hide();
        $(this).parent().find('#nom').attr('contenteditable', 'false');
        let nom = {
            "nom": $(this).parent().find('#nom').text()
        };
        updateNom(nom);
    });

    //Annulation de la modification du nom depuis le champs nom
    $('#mon-compte').on('click', '.cancel', function() {
        $(this).parent().find('.update-nom').show();
        $(this).parent().find('.validate, .cancel').hide();
        $(this).parent().find('#nom').attr('contenteditable', 'false');
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

    //Bouton de suppression d'une salle
    $('#salles table').on('click', '.delete-salle', function() {
        deleteSalle($(this).parents().closest('tr').attr('data-salle'));
    });

    // Bouton de modification d'une salle
    $('#salles table').on('click', '.update-salle', function() {
        $(this).parent().find('.validate, .cancel').show();
        $(this).parent().find('.update-salle').hide();
        $(this).parent().children(':first').attr('contenteditable', 'true').focus();
    });

    // Bouton de validation de modification d'une salle
    $('#salles table').on('click', '.validate', function() {
        $(this).parent().find('.update-salle').show();
        $(this).parent().find('.validate, .cancel').hide();
        $(this).parent().children(':first').attr('contenteditable', 'false');
        updateSalle($(this).parents().closest('tr').attr('data-salle'));
    });

    // Bouton d'annulation modification d'une salle
    $('#salles table').on('click', '.cancel', function() {
        $(this).parent().find('.update-salle').show();
        $(this).parent().find('.validate, .cancel').hide();
        $(this).parent().children(':first').attr('contenteditable', 'false');
    });

    // Bouton d'affichage des infos d'une salle dans la modal
    $('table').on('click', '.link-salle', function() {
        if (DATA.loggedUser.admin) {
            let nomSalle = $(this).html();
            getSalleByNom(nomSalle, "modal-salle");
        } else {
            $('#modal-salle').hide();
            showMsg("Vous n'êtes pas administrateur.", "error");
        }
    });

    // Bouton d'affichage des infos d'un utilisateur dans la modal
    $('table').on('click', '.link-user', function() {
        let loginUser = $(this).html();
        getUserInfos(loginUser, "modal-user");
    });

    // Mettre en place un mécanisme de routage qui affiche la vue correspondant au hash sélectionné
    $(window).on("popstate", () => {
        clearInterval(passagesInterval);
        $("#msg").empty().hide();
        $('section').hide();
        let view = getView();

        // Rediriger l'utilisateur vers l'accueil s'il n'est pas connecté
        if (view !== "accueil" && DATA.loggedUser === undefined) {
            showMsg("Vous n'êtes pas connecté.", "error");
            view ="accueil";
        }

        switch (view) {
            case "accueil":
                // Ne pas charger les données de l'accueil si l'utilisateur n'est pas connecté
                if (DATA.loggedUser !== undefined) {
                    passagesInterval = setInterval(function(){
                        $("#passages-reload").show();
                        setTimeout(function() {
                            $("#passages-reload").hide()
                        }, 2000);
                        getPassagesEnCours(DATA.loggedUser.login);
                    }, 5000);

                    getPassagesEnCours(DATA.loggedUser.login);
                    if (DATA.loggedUser.admin) {
                        getSalles("salles-saturees");
                    }
				}
                break;

            case "mon-compte":
                getUserInfos(DATA.loggedUser.login);
                break;

            case "salles":
                // La liste des salles n'est visible que par les administrateur
                if (DATA.loggedUser.admin) {
                    getSalles();
                    break;
                } else {
                    window.location.href = clientUrl + "#accueil";
                    showMsg("Vous n'êtes pas administrateur.", "error");
                }
                break;

            case "entree":
                // La liste des salles n'est visible que par les administrateur. L'utilisateur doit donc deviner les salles qui existent
                if (DATA.loggedUser.admin) {
                    getSalles("entree");
                }
                break;

            case "sortie":
                // La liste des salles n'est visible que par les administrateur. L'utilisateur doit donc deviner les salles qui existent
                if (DATA.loggedUser.admin) {
                    getSalles("sortie");
                }
                break;

            case "tous-mes-passages":
                getPassages(DATA.loggedUser.login);
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
        url: apiUrl + "users/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(user)
    }).done((data, textStatus, jqXHR) => {
        // Récupération du token jwt
        user.token = jqXHR.getResponseHeader("Authorization");
        DATA.loggedUser = user;
        // Cacher le formulaire de connexion et afficher l'accueil de l'utilisateur connecté
        $("#accueil-not-co").hide();
        $("#accueil-co").show();
        window.location.href = clientUrl + "#accueil";
        // Actualisation du menu en fonction des droits de l'utilisateur connecté
        getMenu();
        showMsg("Vous êtes connecté.", "success");
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
        url: apiUrl + "users/logout",
        type: "POST",
        contentType: "application/json",
        headers: {
            Authorization : DATA.loggedUser.token
        }
    }).done(() => {
        // Vider le tableau de données et rediriger vers l'accueil et afficher le formulaire de connexion
        DATA = [];
        $("#accueil-not-co").show();
        $("#accueil-co").hide();
        window.location.href = clientUrl + "#accueil";
        $('*[id*=-content]').each(function() {
            $(this).html("");
        });
        showMsg("Vous êtes déconnecté.", "success");
        getMenu();
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    });
}

/**
 * Modifie la capacité d'une salle
 * @param nom
 */
function updateSalle(nom) {
    let salle = {
        "nomSalle": $("#salles table").find('tr[data-salle='+nom+'] .nom-salle').text(),
        "capacite": $("#salles table").find('tr[data-salle='+nom+'] .capacite-salle').text()
    };

    $.ajax({
        url: apiUrl + "salles/" + nom,
        type: "PUT",
        contentType: "application/json",
        data: JSON.stringify(salle),
        headers: {
            Authorization : DATA.loggedUser.token
        }
    }).done((data, textStatus, jqXHR) => {
        getSalles();
        showMsg("La salle" + nom + " a bien été modifiée.", "success");
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    });
}

/**
 * Supprime la salle
 * @param nom
 */
function deleteSalle(nom) {
    $.ajax({
        url: apiUrl + "salles/" + nom,
        type: "DELETE",
        headers: {
            Authorization : DATA.loggedUser.token
        }
    }).done((data, textStatus, jqXHR) => {
        getSalles();
        showMsg("La salle" + nom + " a bien été supprimée.", "success");
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    });
}

/**
 * Modifie le nom de l'utilisateur
 */
function updateNom(nom) {
    $.ajax({
        url: apiUrl + "users/" + DATA.loggedUser.login + "/nom",
        type: "PUT",
        contentType: "application/json",
        data: JSON.stringify(nom),
        headers: {
            Authorization : DATA.loggedUser.token
        }
    }).done((data, textStatus, jqXHR) => {
        DATA.loggedUser.nom = nom.nom;
        DATA["mon-compte"].nom = nom.nom;
        render(getView());
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
function getSalleByUrl(url, destination) {
    $.ajax({
        url: url,
        type: "GET",
        accept: "application/json",
        headers: {
            Authorization : DATA.loggedUser.token
        }
    }).done((data, textStatus, request) => {
        if (destination === "salles-saturees") {
            if (data.saturee) {
                DATA[destination].push(data);
            }
        } else {
            DATA[destination].push(data);
        }
        render(destination);
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    })
}

/**
 * Récupère la liste des url des salles et pour chaque url,
 * puis fait appel à la fonction qui ajoute les données de la salle correspondante au tableau DATA à l'indice salles
 */
function getSalles(destination = getView()) {
    $.ajax({
        url: apiUrl + "salles",
        type: "GET",
        accept: "application/json",
        headers: {
            Authorization : DATA.loggedUser.token
        }
    }).done((data, textStatus, request) => {
        DATA[destination] = [];
        render(destination);
        $(data).each((index, value) => {
            getSalleByUrl(value, destination);
        });
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    });
}

/**
 *
 * @param nom
 * @param destination
 */
function getSalleByNom(nom, destination = getView()) {
    $.ajax({
        url: apiUrl + "salles/" + nom,
        type: "GET",
        accept: "application/json",
        headers: {
            Authorization : DATA.loggedUser.token
        }
    }).done((data, textStatus, request) => {
        DATA[destination] = data;
        render(getView());
        render(destination);
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    })
}

/**
 * Récupère les données de l'utilisateur correspondant au login et les insère dans le tableau DATA à l'indice "destination"
 * @param login
 * @param destination
 */
function getUserInfos(login, destination = getView()) {
    $.ajax({
        url: apiUrl + "users/" + DATA.loggedUser.login,
        type: "GET",
        accept: "application/json",
        headers: {
            Authorization : DATA.loggedUser.token
        }
    }).done((data, textStatus, request) => {
        DATA[destination] = data;
        render(destination);
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
        url: apiUrl + "salles",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(salle),
        headers: {
            Authorization : DATA.loggedUser.token
        }
    }).done((data, textStatus, jqXHR) => {
        showMsg("La salle a bien été créée.", "success");
        getSalles();
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    });
}

/**
 *
 * Récupère la liste des url des passages en cours de l'utilisateur associé au login et pour chaque url,
 * puis fait appel à la fonction qui ajoute les données du passage correspondant au tableau DATA à l'indice "destination"
 */
function getPassagesEnCours(login, destination = getView()) {
    $.ajax({
        url: apiUrl + "passages/byUser/" + login + "/enCours",
        type: "GET",
        accept: "application/json",
        headers: {
            Authorization : DATA.loggedUser.token
        }
    }).done((data, textStatus, request) => {
        DATA[destination] = [];
        render(destination);
        $(data).each((index, value) => {
            getPassagesFromUrl(value, destination);
        });
    }).fail((jqXHR, textStatus, errorThrown) => {
        let msg = jqXHR.responseText;
        showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
    });
}

/**
 *
 * Récupère la liste des url des passages de l'utilisateur associé au login et pour chaque url,
 * puis fait appel à la fonction qui ajoute les données du passage correspondant au tableau DATA à l'indice "destination"
 */
function getPassages(login, destination = getView()) {
        $.ajax({
            url: apiUrl + "passages/byUser/" + login,
            type: "GET",
            accept: "application/json",
            headers: {
                Authorization : DATA.loggedUser.token
            }
        }).done((data, textStatus, request) => {
            DATA[destination] = [];
            render(destination);
            $(data).each((index, value) => {
                getPassagesFromUrl(value, destination);
            });
        }).fail((jqXHR, textStatus, errorThrown) => {
            let msg = jqXHR.responseText;
            showMsg(msg.substring(msg.indexOf("<body>")+6, msg.indexOf("</body>")), "error");
        });
}

/**
 * Récupère les données du passage par son url et les ajoute au tableau DATA à l'indice destination
 * @param url
 * @param destination
 */
function getPassagesFromUrl(url, destination = getView()) {
    $.ajax({
        url: url,
        type: "GET",
        accept: "application/json",
        headers: {
            Authorization : DATA.loggedUser.token
        }
    }).done((data, textStatus, request) => {
        let passage = {
            "user": data.user.split("/").pop(),
            "salle": data.salle.split("/").pop(),
            "dateEntree": data.dateEntree,
            "dateSortie": data.dateSortie,
        }
        DATA[destination].push(passage);
        render(destination);
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
    let passage = {
        "user": DATA.loggedUser.login,
        "salle": $('#' + type + ' form input[name="salle"]').val(),
        "dateEntree": type === "entree" ? getStrangeDateFormat(new Date()) : "",
        "dateSortie": type === "sortie" ? getStrangeDateFormat(new Date()) : ""
    }

    $.ajax({
        url: apiUrl + "passages",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(passage),
        headers: {
            Authorization : DATA.loggedUser.token
        }
    }).done((data, textStatus, jqXHR) => {
        showMsg("Votre passage a bien été ajouté.", "success");
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

/**
 * Récupère la vue à afficher en fonction du hash de l'url
 * @returns {string}
 */
function getView() {
    return window.location.hash.substr(1);
}

/**
 * Créer une date au format attendu par l'api
 * @param date
 * @returns {string}
 */
// From "Sat, 19 Dec 2020 17:33:43 GMT" to "Wed Oct 16 00:00:00 GMT 2013"
function getStrangeDateFormat(date) {
    const tmp = date.toGMTString().replace(",", "").split(" ");
    const tab = [];
    for (const i of [0, 2, 1, 4, 5, 3]) {
        tab.push(tmp[i]);
    }
    return tab.join(" ");
}

// https://192.168.75.76/api/v3/ -> prod
// http://192.168.75.76:8080/v3/ -> prod
// http://localhost:8080/tp4/ -> test
function getApiUrl() {
    const baseUrl = window.location.origin;

    if (baseUrl.includes("localhost") || baseUrl.includes("file")) {
        return "http://localhost:8080/tp4/";
    }
    if (baseUrl.includes("https")) {
        return baseUrl + "/api/v3/";
    }
    return "http://192.168.75.76:8080/v3/";
}

// https://192.168.75.76/api/client/ -> prod
// http://192.168.75.76:8080/client/ -> prod
// http://localhost:8080/tp4/static/client/ -> test
function getClientUrl() {
    return window.location.origin + window.location.pathname
}

function getTime() {
    let date = new Date();
    let h = (date.getHours() < 10) ? "0" + date.getHours() : date.getHours();
    let m = (date.getMinutes() < 10) ? "0" + date.getMinutes() : date.getMinutes();
    let s = (date.getSeconds() < 10) ? "0" + date.getSeconds() : date.getSeconds();
    $("#time").html(h + ":" + m + ":" + s);
    setTimeout(getTime, 1000);
}

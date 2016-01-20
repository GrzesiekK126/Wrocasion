/// <reference path="knockout-2.2.0.debug.js" />
var ileJest = 0;

var link = "/api/WydarzeniaApi/Pob";
var linkEdytuj = "/api/WydarzeniaApi/EdytujWydarzenie";

var wydarzenia = [];
var niezapisane = {};

var kategorie = {
    0: "Kino",
    1: "Teatr",
    2: "Sztuka nowoczesna",
    3: "Spektakle",
    4: "Koncerty"
};

var idAktualnego = -1;
var obrazyAktualnego = [];

$(document).ready(function () {
    
    pobierzWydarzenia(0, 20);

  // $("#przeslij-obraz").click(przeslijObraz);
    $('[data-toggle="tooltip"]').tooltip();
    przygotujUploader();
});

function pobierzWydarzenia(cnt, offset) {
    var kategoria = {};
    kategoria.Nazwa = $('#nazwa-dodawanej').val();
    kategoria.Obrazek = "";

    jQuery.ajax({
        type: "GET",
        url: link,
        data: {cnt: 20, offset: ileJest},
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, status) {
            for (i = 0; i < data.length;i++)
            {
                data[i].Data = parsujDateZJSONa(data[i].Data);

                wydarzenia[data[i].Id] = data[i];

                var link = $(generujWiersz(data[i]));
                $("#lista-wydarzen").append(link);
                //link.click(handler);
                przypiszZdarzenia(link, data[i].Id);
               
            }
        },
        error: function () {
            alert("Występił problem z pobraniem wydarzeń.");
        }
    });
}

//model -> wyświetlanie
function generujWiersz(data) {

    var wzorzec = $("#wzorzec").html();

    wzorzec = zamien(wzorzec, "nazwa", data.Nazwa);
    wzorzec = zamien(wzorzec, "id", data.Id);
    wzorzec = zamien(wzorzec, "data", dataNaTekst(data.Data));
    wzorzec = zamien(wzorzec, "czas", dataNaTekstCzasu(data.Data));
    wzorzec = zamien(wzorzec, "cena", data.Cena);
    wzorzec = zamien(wzorzec, "link", data.Link);
    wzorzec = zamien(wzorzec, "opis", data.Opis);
    wzorzec = zamien(wzorzec, "kategoria", data.Kategoria);

    if (data.LinkiDoObrazkow != undefined && data.LinkiDoObrazkow[0] != undefined) {
        wzorzec = zamien(wzorzec, "link-zdjecia", location.protocol + '//' + location.host + "/" + data.LinkiDoObrazkow[0]);
    }
    else {
        wzorzec = zamien(wzorzec, "link-zdjecia", "");
    }

    wzorzec = zamien(wzorzec, "miejsce", data.Lokacja.Miasto + ", " + data.Lokacja.Ulica);

    return wzorzec;
}

function przypiszZdarzenia(link, id) {
    console.log("Przypisz zdarzenia do id = " + id + "(" + link + ")");

    $(".wydarzenie-naglowek", link).click(klikNaWiersz);

    $(".przelacz-edycje", link).click(klikNaEdycje);

    $("#zatwierdz-edycje-" + id).click(zatwierdzEdycje);

    $('#czas-' + id).clockpicker();

}

function klikNaWiersz() {
    console.log("klik");

    //rozwiniecie nowego
    var abc = $(".wydarzenie-naglowek-przed-rozwinieciem", this);
    abc.toggle("slide", null);
    abc.parent().parent().children(".wydarzenie-tresc").toggle("slide", null);
    $(".wydarzenie-naglowek-po-rozwinieciu", this).toggle("slide", null);

    //id aktualnie kliknietego wydarzenia
    var wiersz = $(this).parent().attr("id");//$(abc).closest(".wiersz").attr("id");
    if (wiersz == undefined)
        return;
    idTego = wiersz.substring(7, wiersz.length);

    if (idTego == idAktualnego) {
        idAktualnego = -1;
    }
    else {
        //zwiniecie starego
        if (idAktualnego >= 0) {
            var poprzedniWiersz = $("#wiersz-" + idAktualnego);
            var a = $(".wydarzenie-naglowek-przed-rozwinieciem", poprzedniWiersz);
            a.toggle("slide", null);
            var trescPoprzedniego = a.parent().parent().children(".wydarzenie-tresc");
            trescPoprzedniego.toggle("slide", null);
            $(".wydarzenie-naglowek-po-rozwinieciu", poprzedniWiersz).toggle("slide", null);

            var elem = trescPoprzedniego.find(".tresc-edycja");
            if( elem.is(":visible")){
                trescPoprzedniego.find(".tresc-edycja-bez-animacji").toggle();
                trescPoprzedniego.find(".tresc-wyswietlanie-bez-animacji").toggle();
                elem.toggle("slide", null);
                trescPoprzedniego.find(".tresc-wyswietlanie").toggle("slide", null);
            }
        }

        idAktualnego = idTego;
    }
};

function klikNaEdycje() {
    //pobranie id aktualnego wydarzenia
    var id = $(this).closest(".wiersz").attr("id");
    id = id.substring(7, id.length);

    var tresc = $(this).closest(".wydarzenie-tresc");

    //wypełnienie danymi
    wypelnijWierszEdycji(tresc, id);

    //zmiana widoczności elementów
    tresc.find(".tresc-edycja-bez-animacji").toggle();//("slide", null);
    tresc.find(".tresc-wyswietlanie-bez-animacji").toggle();//("slide", null);
    tresc.find(".tresc-edycja").toggle("slide", null);
    tresc.find(".tresc-wyswietlanie").toggle("slide", null);

    var idAktualnego = id;
    obrazyAktualnego = wydarzenia[id].LinkiDoObrazkow;

    wypelnijListeObrazow();
}

//model -> inputy
function wypelnijWierszEdycji(divTresci, id) {
    divTresci.find("#nazwa-" + id).val(wydarzenia[id].Nazwa);
    divTresci.find("#data-" + id).val(dataNaTekst(wydarzenia[id].Data));
    divTresci.find("#czas-" + id).val(dataNaTekstCzasu(wydarzenia[id].Data));
    divTresci.find("#cena-" + id).val(wydarzenia[id].Cena);
    divTresci.find("#opis-" + id).val(wydarzenia[id].Opis);
    divTresci.find("#link-" + id).val(wydarzenia[id].Link);
    //var kat = divTresci.find("#kategoria-" + id);
    //$("option",kat).prop('selected', false);
    divTresci.find("#kategoria-" + id + " option").filter(function () {
        //may want to use $.trim in here
        return $(this).val().trim() == wydarzenia[id].Kategoria.trim();
    }).prop('selected', true);
}

//inputy -> json
function zatwierdzEdycje() {
    //pobranie id aktualnego wydarzenia
    var id = $(this).closest(".wiersz").attr("id");
    id = id.substring(7, id.length);

    //obiekt-kontener pol tekstowych
    var tresc = $(this).closest(".wydarzenie-tresc");

    var model = inputyNaModel(id, tresc);
    model.LinkiDoObrazkow = obrazyAktualnego;

    niezapisane[model.Id] = model;

    jQuery.ajax({
        type: "POST",
        url: linkEdytuj,
        data: modelNaJsona(model),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, status) {
            if (data == null) {
                alert("null!");
                return;
            }

            if (data.Sukces == null) {
                alert("Sukces to null!");
                return;
            }

            if (data.Sukces == true) {
                //pobranie id aktualnego wydarzenia
                var id = data.Wiadomosc;

                //obiekt-kontener pol tekstowych

                var klikniety = $("#zatwierdz-edycje-" + id);

                var tresc = klikniety.closest(".wydarzenie-tresc");

                wydarzenia[id] = niezapisane[id];

                zaktualizujWiersz(tresc, id);
                delete niezapisane[id];

                //zmiana widoczności elementów
                tresc.find(".tresc-edycja").toggle("slide", null);
                tresc.find(".tresc-wyswietlanie").toggle("slide", null);

                return;
            }
            else {
                if (data.Wiadomosc == null) {
                    alert("Wiadomosc to null!");
                }
                else {
                    alert("Wiadomość: " + data.Wiadomosc);
                }
            }
        },
        error: function () {
            alert("Występił problem z edycją wydarzeń.");
        }
    });
    //EdytujWydarzenie
}

//aktualizuje wyswietlane nieedytowalne dane (na dane z obiektu listy wydarzen)
function zaktualizujWiersz(divTresci, id) {
    divTresci.find("#nazwa-wyswietlanie-" + id).text(wydarzenia[id].Nazwa);
    divTresci.find("#data-wyswietlanie-" + id).html(dataNaTekst(wydarzenia[id].Data));
    divTresci.find("#czas-wyswietlanie-" + id).html(dataNaTekstCzasu(wydarzenia[id].Data));
    divTresci.find("#cena-wyswietlanie-" + id).html(wydarzenia[id].Cena);
    divTresci.find("#link-wyswietlanie-" + id).html(wydarzenia[id].Link);
    divTresci.find("#opis-wyswietlanie-" + id).html(wydarzenia[id].Opis);
    divTresci.find("#kategoria-wyswietlanie-" + id).html(wydarzenia[id].Kategoria);

    var divNaglowka = divTresci.closest(".wiersz");
    divNaglowka = divNaglowka.find(".wydarzenie-naglowek");

    divNaglowka.find(".naglowek-nazwa").text(wydarzenia[id].Nazwa);
    divNaglowka.find(".naglowek-nazwa2").text(wydarzenia[id].Nazwa);
    divNaglowka.find(".naglowek-data").html(dataNaTekst(wydarzenia[id].Data));
    divNaglowka.find(".naglowek-kategoria").html(wydarzenia[id].Kategoria);
    //divNaglowka.find(".naglowek-miejsce").text("xxdd");
}

//rzeczy do podmiany wzorca
function escapeRegExp(str) {
    return str.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
}

function replaceAll(str, find, replace) {
    return str.replace(new RegExp(escapeRegExp(find), 'g'), replace);
}

function zamien(str, klucz, wartosc) {
    return replaceAll(str, "---" + klucz + "---", wartosc);
}
//koniec rzeczy do podmiany wzorca

function parsujDateZJSONa(data) {
    return new Date(
        data.substring(6, 10),
        data.substring(3, 5),
        data.substring(0, 2),
        data.substring(11, 13),
        data.substring(14, 16));
}

//model -> wyswietlanie, inputy, json edycji
function dataNaTekst(obiektDaty) {
    var day = ("0" + obiektDaty.getDate()).slice(-2);
    var month = ("0" + (obiektDaty.getMonth() + 1)).slice(-2);
    return obiektDaty.getFullYear() + "-" + (month) + "-" + (day);
}

//model -> wyswietlanie, inputy, json
function dataNaTekstCzasu(obiektDaty) {
    var godzina = obiektDaty.getHours();
    var minuty = obiektDaty.getMinutes();
    
    if (godzina < 10)
        godzina = "0" + godzina;

    if (minuty < 10)
        minuty = "0" + minuty;


    return godzina + ":" + minuty;
}

function parsujDateZInputow(data,czas) {
    var b = data.split(/\D/);

    var c = czas.split(/:/);

    return new Date(b[0], --b[1], b[2],c[0],c[1],0,0);
}

//model -> json edycji
function dataNaJSONa(obiektDaty) {
    var dzien = ("0" + obiektDaty.getDate()).slice(-2);
    var mc = ("0" + (obiektDaty.getMonth() + 1)).slice(-2);
    var rok = obiektDaty.getFullYear();

    return dataNaTekst(obiektDaty) + " " + dataNaTekstCzasu(obiektDaty);
}

function inputyNaModel(id,tresc) {
    var model = jQuery.extend(true, {}, wydarzenia[id]); 
        
    model.Nazwa = tresc.find("#nazwa-" + id).val();

    model.Data = parsujDateZInputow(tresc.find("#data-" + id).val(),tresc.find("#czas2-" + id).val());

    model.Cena = tresc.find("#cena-" + id).val();

    //TODO: zrobic tutaj ustawianie lokacji
    model.Lokacja = { Id: 1 };

    model.NazwaOperatora = "Operator domyślny";

    model.Opis = tresc.find("#opis-" + id).val();

    model.Link = tresc.find("#link-" + id).val();

    model.Kategoria = $("#kategoria-" + id + " option:selected").text();

    return model;
}

function modelNaJsona(modelWydarzenia) {
    return JSON.stringify({
        Id: modelWydarzenia.Id,
        Nazwa: modelWydarzenia.Nazwa,
        Data: dataNaJSONa(modelWydarzenia.Data),
        Cena: modelWydarzenia.Cena,

        Lokacja: modelWydarzenia.Lokacja,
        LinkiDoObrazkow: modelWydarzenia.LinkiDoObrazkow,
        NazwaOperatora: modelWydarzenia.NazwaOperatora,
        Opis: modelWydarzenia.Opis,
        Link: modelWydarzenia.Link,
        Kategoria: modelWydarzenia.Kategoria,
    });
}

function przygotujUploader() {
    document.getElementById('uploader').onsubmit = function () {
        var formdata = new FormData();
        var fileInput = document.getElementById('img_url');

        for (i = 0; i < fileInput.files.length; i++) {
            formdata.append(fileInput.files[i].name, fileInput.files[i]);
        }
        var xhr = new XMLHttpRequest();
        xhr.open('POST', "/Wydarzenia/WgrajZdjecieWydarzenia");
        xhr.send(formdata);
        console.log("Wyslano");
        xhr.onreadystatechange = function () {
            if(xhr.status == 200){
                if (xhr.readyState == 4 ) {
                    if (xhr.responseText.slice(0, 6) == "\"ERROR") {
                        swal("Nie udało się :(",xhr.responseText.slice(6,xhr.responseText.length-1) , "error");
                    }
                    else {
                        var nowa = JSON.parse(xhr.responseText);

                        var link = $(generujWierszObrazu(nowa));
                        $("#lista-obrazow").append(link);
                        $(".usuwanie-obrazu", link).click(usunObraz);

                        obrazyAktualnego.push(nowa);
                    }
                }
                    
            }
            else{
                swal("Nie udało się :(", "Wystąpił błąd podczas dodawania zdjęcia" , "error");
            }
        }
        return false;
    } 
}

function generujWierszObrazu(data) {
    var wzorzec = $("#wzorzec-obrazu").html();
    wzorzec = zamien(wzorzec, "link", data);
    wzorzec = zamien(wzorzec, "link-caly", location.protocol + '//' + location.host + "/" + data);
    return wzorzec;
}

function wypelnijListeObrazow() {
    $("#lista-obrazow").html("");

    console.log(obrazyAktualnego);

    for (var i = 0, len = obrazyAktualnego.length; i < len; i++) {
        console.log(obrazyAktualnego[i]);
        var link = $(generujWierszObrazu(obrazyAktualnego[i]));
        $("#lista-obrazow").append(link);

        $(".usuwanie-obrazu", link).click(usunObraz);
    }
}

function usunObraz() {
    var row = $(this).closest("tr");;

    var index = obrazyAktualnego.indexOf($("a", row).html());
    if (index > -1) {
        obrazyAktualnego.splice(index, 1);
        row.remove();
    }   
}
/// <reference path="knockout-2.2.0.debug.js" />
var ileJest = 0;

var link = "/api/WydarzeniaApi/Pob";
var linkEdytuj = "/api/WydarzeniaApi/EdytujWydarzenie";

var wydarzenia = [];
var niezapisane = {};

$(document).ready(function () {
    //your code here

    $(".wydarzenie-naglowek").click(klikNaWiersz);

    $(".przelacz-edycje").click(klikNaEdycje);

    $("#zatwierdz-edycje-134").click(zatwierdzEdycje);

    pobierzWydarzenia(0, 20);

    $('#czas-134').clockpicker();
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

                //TODO usunąć ten warunek
                if (i == 0) {
                    wydarzenia[134] = data[i];
                    wydarzenia[134].Id = 134;
                }

                var link = $(generujWiersz(data[i]));
                $("#lista-wydarzen").append(link);
                //link.click(handler);
                przypiszZdarzenia(link, data[i].Id);

                $('#czas-'+i).clockpicker();
                //$("#lista-wydarzen").append(generujWiersz(data[i]));
                
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
    wzorzec = zamien(wzorzec, "kategoria", "JESZCZE NIE");
    wzorzec = zamien(wzorzec, "miejsce", data.Lokacja.Miasto + ", " + data.Lokacja.Ulica);

    return wzorzec;
}

function przypiszZdarzenia(link,id) {
    $(".wydarzenie-naglowek", link).click(klikNaWiersz);

    $(".przelacz-edycje", link).click(klikNaEdycje);

    $("#zatwierdz-edycje-"+id).click(zatwierdzEdycje);
}

function klikNaWiersz() {
    console.log("klik");

    var abc = $(".wydarzenie-naglowek-przed-rozwinieciem", this);
    abc.toggle("slide", null);
    abc.parent().parent().children(".wydarzenie-tresc").toggle("slide", null);

    $(".wydarzenie-naglowek-po-rozwinieciu", this).toggle("slide", null);
};

function klikNaEdycje() {
    console.log("klik na edycje");

    //pobranie id aktualnego wydarzenia
    var id = $(this).closest(".wiersz").attr("id");
    id = id.substring(7, id.length);

    var tresc = $(this).closest(".wydarzenie-tresc");

    //wypełnienie danymi
    wypelnijWierszEdycji(tresc, id);

    //zmiana widoczności elementów
    tresc.find(".tresc-edycja").toggle("slide", null);
    tresc.find(".tresc-wyswietlanie").toggle("slide", null);
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
                alert("Edycja udana!");

                //pobranie id aktualnego wydarzenia
                var id = data.Wiadomosc;

                //obiekt-kontener pol tekstowych

                var klikniety = $("#zatwierdz-edycje-" + id);

                var tresc = klikniety.closest(".wydarzenie-tresc");

                wydarzenia[id] = niezapisane[id];
                //przepiszZEdytowalnychDoModelu(tresc, id);
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

//inputy -> model
/*
function przepiszZEdytowalnychDoModelu(divTresci, id) {
    wydarzenia[id].Nazwa = divTresci.find("#nazwa-" + id).val();

    wydarzenia[id].Data = parsujDateZInputow(divTresci.find("#data-"+id).val(),divTresci.find("#czas2-"+id).val());

    wydarzenia[id].Cena = divTresci.find("#cena-" + id).val();
    wydarzenia[id].Opis = divTresci.find("#opis-" + id).val();
    wydarzenia[id].Link= divTresci.find("#link-" + id).val();
    wydarzenia[id].Kategoria = $("#kategoria-" + id + " option:selected").text();

    console.log(divTresci.find("#czas2-" + id).val());
}
*/

//aktualizuje wyswietlane nieedytowalne dane (na dane z obiektu listy wydarzen)
function zaktualizujWiersz(divTresci, id) {

    divTresci.find("#nazwa-wyswietlanie-" + id).text(wydarzenia[id].Nazwa);
    divTresci.find("#data-wyswietlanie-" + id).html(dataNaTekst(wydarzenia[id].Data));
    divTresci.find("#czas-wyswietlanie-" + id).html(dataNaTekstCzasu(wydarzenia[id].Data));
    divTresci.find("#cena-wyswietlanie-" + id).html(wydarzenia[id].Cena);
    divTresci.find("#link-wyswietlanie-" + id).html(wydarzenia[id].Link);
    divTresci.find("#opis-wyswietlanie-" + id).html(wydarzenia[id].Opis);
    divTresci.find("#kategoria-wyswietlanie-" + id + " option").html(wydarzenia[id].Kategoria);

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

        NazwaOperatora: modelWydarzenia.NazwaOperatora,
        Opis: modelWydarzenia.Opis,
        Link: modelWydarzenia.Link,
        Kategoria: modelWydarzenia.Kategoria,
    });
}
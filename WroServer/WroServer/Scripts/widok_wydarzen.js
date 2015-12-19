var ileJest = 0;

var link = "/api/WydarzeniaApi/Pobierz";

$(document).ready(function () {
    //your code here

    $(".wydarzenie-naglowek").click(function () {
        console.log("klik");

        var abc = $(".wydarzenie-naglowek-przed-rozwinieciem", this);
        abc.toggle("slide", null);
        abc.parent().parent().children(".wydarzenie-tresc").toggle("slide", null);

        $(".wydarzenie-naglowek-po-rozwinieciu", this).toggle("slide", null);
    });

    pobierzWydarzenia(0, 20);
});

function pobierzWydarzenia(cnt, offset) {
    var kategoria = {};
    kategoria.Nazwa = $('#nazwa-dodawanej').val();
    kategoria.Obrazek = "";

    jQuery.ajax({
        type: "POST",
        url: link,
        data: "20;" + ileJest,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data, status) {
            for (i = 0; i < 20;i++) //tutaj trzeba zamiast 20 wstawic dlugosc listy bo moze byc mniej
            {
                $("#lista-wydarzen").append(generujWiersz(data.lista[i]));
            }
            
        },
        error: function () {
            alert("Występił problem z pobraniem wydarzeń.");
        }
    });
}

function generujWiersz(data) {
    return "<p>"+data.Nazwa+"</p>";
}

function przypiszZdarzenia(id) {

}
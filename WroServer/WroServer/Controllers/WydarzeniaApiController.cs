using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WroServer.Models.WidokWydarzen;
using WroBL.Wydarzenia;

namespace WroServer.Controllers
{
    public class WydarzeniaApiController : ApiController
    {
        [HttpPost]
        public IHttpActionResult Testuj(string data)
        {
            WydarzenieAjaxModel a = new WydarzenieAjaxModel();

            return Content<WydarzenieAjaxModel>(HttpStatusCode.OK, a);
        }

        public IHttpActionResult Test2()
        {
            WydarzenieAjaxModel a = new WydarzenieAjaxModel();

            return Content<WydarzenieAjaxModel>(HttpStatusCode.OK, a);
        }

        [HttpGet]
        [HttpPost]
        public int SumNumbers(int first, int second)
        {
            return first + second;
        }


        [HttpGet]
        public IEnumerable<WydarzenieAjaxModel> Pob(int cnt, int offset)
        {
            var listaZBazy = WydarzeniaService.PobierzWydarzenia(cnt, offset);

            var lista = new List<WydarzenieAjaxModel>();

            foreach (var wyd in listaZBazy)
            {
                var lokacja = WydarzeniaService.PobierzLokacje(wyd.IdLokacji);

                if (lokacja == null)
                    continue;

                var oper = WydarzeniaService.NazwaOperatora(wyd.IdOperatora);

                lista.Add(new Models.WidokWydarzen.WydarzenieAjaxModel()
                {
                    Nazwa = wyd.Nazwa,
                    Cena = wyd.Cena,
                    Data = wyd.Data.ToString("dd-MM-yyyy HH:mm"),
                    DataDodania = wyd.DataDodania.ToString("dd-MM-yyyy HH:mm"),
                    Id = wyd.Id,
                    Link = wyd.Link,
                    LinkiDoObrazkow = wyd.LinkiDoObrazkow,
                    Lokacja = new LokacjaAjaxModel()
                    {
                        Id = lokacja.Id,
                        Nazwa = lokacja.Nazwa,
                        KodPocztowy = lokacja.KodPocztowy,
                        Lat = lokacja.Lat,
                        Lng = lokacja.Lng,
                        Miasto = lokacja.Miasto,
                        Ulica = lokacja.Ulica
                    },
                    Kategoria = WydarzeniaService.NazwaKategorii(wyd.IdKategorii),
                    NazwaOperatora = oper,
                    Opis = wyd.Opis
                });
            }

            return lista;
        }

        /// <summary>
        /// edytuje wydarzenie
        /// zwraca model SukcesLubBladModel.
        /// </summary>
        /// <param name="model">Model SukcesLubBladModel. Jeśli się powiodło, Sukces = true a Wiadomosc = id wydarzenia. Jesli błąd, Sukces = false a Wiadomosc = powód niepowodzenia.</param>
        /// <returns></returns>
        [HttpPost]
        public IHttpActionResult EdytujWydarzenie(Models.WidokWydarzen.WydarzenieAjaxModel model)
        {
            int id;
            string wiadomosc;

            DateTime dataWydarzenia;
            if(! DateTime.TryParseExact(model.Data,"yyyy-MM-dd H:m",System.Globalization.CultureInfo.InvariantCulture,System.Globalization.DateTimeStyles.None,out dataWydarzenia))
            {
                //BŁĄD - przesłana data jest niepoprawna
                return Content<SukcesCzyBladModel>(HttpStatusCode.OK, new SukcesCzyBladModel(){
                    Sukces = false,
                    Wiadomosc = "Niepoprawna data"
                });
            }

            var sukces = WydarzeniaService.DodajLubEdytuj(new WroBL.Wydarzenia.Modele.Wydarzenie()
            {
                Id = model.Id,
                Cena = model.Cena,
                Data = dataWydarzenia,
                IdKategorii = WydarzeniaService.IdKategorii(model.Kategoria),
                IdLokacji = model.Lokacja.Id,
                IdOperatora = WydarzeniaService.IdOperatora(model.NazwaOperatora),
                Link = model.Link,
                LinkiDoObrazkow = model.LinkiDoObrazkow,
                Nazwa = model.Nazwa,
                Opis = model.Opis,
            }, out id, out wiadomosc);

            //Zwracamy wynik funkcji
            SukcesCzyBladModel a = new SukcesCzyBladModel(){
                Sukces = sukces,
                Wiadomosc = sukces?id.ToString():wiadomosc
            };
            return Content<SukcesCzyBladModel>(HttpStatusCode.OK, a);
        }
    }
}
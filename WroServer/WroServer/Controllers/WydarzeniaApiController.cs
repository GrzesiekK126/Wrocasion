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
        [ActionName("Pobierz")]
        [HttpPost]
        public IEnumerable<WydarzenieAjaxModel> Pobierz(string data)
        {
            #region sparsowanie danych
            if (data == null || data.Equals(string.Empty))
                return null;

            var czesci = data.Split(';');
            if(czesci.Length !=2)
                return null;

            int cnt, offset;

            if(! Int32.TryParse(czesci[0],out cnt))
                return null;

            if(! Int32.TryParse(czesci[1], out offset))
                return null;
            #endregion

            var listaZBazy = WydarzeniaService.PobierzWydarzenia(cnt,offset);

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
                        KodPocztowy = lokacja.KodPocztowy,
                        Lat = lokacja.Lat,
                        Lng = lokacja.Lng,
                        Miasto = lokacja.Miasto,
                        Ulica = lokacja.Ulica
                    },
                    NazwaOperatora = oper,
                    Opis = wyd.Opis
                });
            }

            return lista;
        }
    }
}
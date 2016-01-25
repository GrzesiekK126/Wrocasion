using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Mvc;
using WroServer.Models.AnalizyModel;

namespace WroServer.Controllers.Analizy
{
    public class AnalizyController : Controller
    {
        public ActionResult Analizy()
        {
            var model =
                WroBL.DAL.DatabaseUtils.EleentsToDataTable(
                    " select count(c2e.categories), c.name from cat2event c2e left join categories c on c2e.categories = c.id group by c2e.categories, c.name order by 1 desc").AsEnumerable();
            var lista = new List<Models.AnalizyModel.NajpopularniejszeKategorie>();

                        lista = (from item in model
                                    select new Models.AnalizyModel.NajpopularniejszeKategorie
                                    {
                                        Ilosc = item.Field<int>("count"),
                                        Nazwa = item.Field<string>("name"),
                                    }).ToList();
            return View(lista);
        }
    }
}

using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WroServer.Models;

namespace WroServer.Controllers
{
    [Authorize]
    public class WydarzeniaController : Controller
    {
        //
        // GET: /Wydarzenia/

        public ActionResult Wydarzenia()
        {
            return View();
        }

        public ActionResult Kategorie()
        {
            var model = new ListaKategoriiModel();
            var categoriesDataTable = WroBL.DAL.DatabaseUtils.EleentsToDataTable("select id,name,img_link from categories").AsEnumerable();
            model.ListaKategorii = (from item in categoriesDataTable
                                    select new Models.JednaKategoriaModel
                                    {
                                        Id = item.Field<int>("id"),
                                        Nazwa = item.Field<string>("name"),
                                        LinkDoObrazka = item.Field<string>("img_link")
                                    }).ToList();
        

            return View(model);
        }

    }
}

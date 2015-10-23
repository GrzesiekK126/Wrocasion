using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

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
            var model = new Models.ListaKategoriiModel();
            model.ListaKategorii = new List<Models.JednaKategoriaModel>()
            {
                new Models.JednaKategoriaModel(){
                    Id = 0,
                    Nazwa = "Historia",
                    LinkDoObrazka = "/Content/Images/kategorie/historia.png"
                },
                new Models.JednaKategoriaModel(){
                    Id = 1,
                    Nazwa = "Film",
                    LinkDoObrazka = "/Content/Images/kategorie/film.png"
                },
                new Models.JednaKategoriaModel(){
                    Id = 2,
                    Nazwa = "Literatura",
                    LinkDoObrazka = "/Content/Images/kategorie/literatura.png"
                },
            };

            return View(model);
        }

    }
}

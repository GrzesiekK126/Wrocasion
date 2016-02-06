using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WroBL.Wydarzenia.Modele;
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
            var model = new Models.WidokWydarzen.WydarzeniaModel();

            var categoriesDataTable = WroBL.DAL.DatabaseUtils.EleentsToDataTable("select id,name,img_link from categories").AsEnumerable();
            model.ListaKategorii = (from item in categoriesDataTable
                                    select new Models.JednaKategoriaModel
                                    {
                                        Id = item.Field<int>("id"),
                                        Nazwa = item.Field<string>("name"),
                                    }).ToList();
            return View(model);
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

        [AllowAnonymous]
        public JsonResult Upload(FormCollection form)
        {
            HttpPostedFileBase file;
            try
            {
                file = Request.Files[0];
            }
            catch (ArgumentOutOfRangeException)
            {
                return Json("ERRORBrak obrazka", JsonRequestBehavior.AllowGet);
            }
            string fileName = file.FileName;
            string mimeType = file.ContentType;

            System.IO.Stream fileContent = file.InputStream;
            try
            {
                string savePath = Server.MapPath(@"/files/") + fileName;
                file.SaveAs(savePath);
            }
            catch (System.IO.IOException ex)
            {
                return Json("ERRORProblem z dodaniem obrazka", JsonRequestBehavior.AllowGet);
            }
            var nazwaKategorii = form["nazwa_kategorii"];
            if( String.IsNullOrWhiteSpace(nazwaKategorii))
                return Json("ERRORBrak nazwy", JsonRequestBehavior.AllowGet);

             
            if (WroBL.DAL.DatabaseUtils.ExistsElement("select first 1 1 from categories c where c.name='" + nazwaKategorii + "'"))
            {
                return Json("ERRORKategoria już istnieje", JsonRequestBehavior.AllowGet);
            }
            else
            {
                WroBL.DAL.DatabaseUtils.DatabaseCommand("insert into categories (name,img_link) values('" + nazwaKategorii + "','" + "/files/"+ fileName + "')");
                JednaKategoriaModel toReturn = new JednaKategoriaModel()
                {
                    Id = Int32.Parse(WroBL.DAL.DatabaseUtils.GetOneElement("select id from categories c where c.name='" + nazwaKategorii + "'")),
                    LinkDoObrazka = "/files/" + fileName,
                    Nazwa = nazwaKategorii
                };
                return Json(toReturn, JsonRequestBehavior.AllowGet);
                //return Json("OK", JsonRequestBehavior.AllowGet);
            }
        }

        [AllowAnonymous]
        public JsonResult WgrajZdjecieWydarzenia(FormCollection form)
        {
            HttpPostedFileBase file;
            try
            {
                file = Request.Files[0];
            }
            catch (ArgumentOutOfRangeException)
            {
                return Json("ERRORBrak obrazka", JsonRequestBehavior.AllowGet);
            }
            string fileName = file.FileName;
            string mimeType = file.ContentType;

            System.IO.Stream fileContent = file.InputStream;
            try
            {
                string savePath = Server.MapPath(@"/files/") + fileName;
                file.SaveAs(savePath);
            }
            catch (System.IO.IOException ex)
            {
                return Json("ERRORProblem z dodaniem obrazka", JsonRequestBehavior.AllowGet);
            }

            return Json("files/" + fileName, JsonRequestBehavior.AllowGet);
        }

        public ActionResult Bindings()
        {
            return View();
        }

        public ActionResult DodajWydarzenie()
        {
            var model = new Models.WidokWydarzen.WydarzeniaModel();

            var categoriesDataTable = WroBL.DAL.DatabaseUtils.EleentsToDataTable("select id,name,img_link from categories").AsEnumerable();
            model.ListaKategorii = (from item in categoriesDataTable
                                    select new Models.JednaKategoriaModel
                                    {
                                        Id = item.Field<int>("id"),
                                        Nazwa = item.Field<string>("name"),
                                    }).ToList();

            return View(model);
        }

        [HttpPost]
        public ActionResult DodajWydarzenie(Models.WidokWydarzen.WydarzenieAjaxModel model)
        {
            int id = 0;
            string msg = "";
            int idOperatora=0;
            Int32.TryParse(WroBL.DAL.DatabaseUtils.GetOneElement("SELECT O.ID FROM OPERATOR O WHERE O.NAME='"+model.NazwaOperatora+"';"), out idOperatora);
            List<string> kategoria = new List<string>()
            {
                model.Kategoria
            };
            int idKategorii;
            Int32.TryParse(WroBL.DAL.DatabaseUtils.GetOneElement("SELECT O.ID FROM CATEGORIES O WHERE O.NAME='" + model.Kategoria + "';"), out idKategorii);
            var wydarzenie = new WroBL.Wydarzenia.Modele.Wydarzenie
            {
                Nazwa = model.Nazwa,
                Cena = model.Cena,
                Opis = model.Opis,
                Link = model.Link,
                Data = DateTime.ParseExact(model.Data, "yyyy-MM-dd HH:mm:ss,fff",
                                       System.Globalization.CultureInfo.InvariantCulture),
                DataDodania = DateTime.ParseExact(model.DataDodania, "yyyy-MM-dd HH:mm:ss,fff",
                                       System.Globalization.CultureInfo.InvariantCulture),
                LinkiDoObrazkow = model.LinkiDoObrazkow,
                Lokalizacja = new Lokacja()
                {
                    Nazwa = model.Lokacja.Nazwa,
                    Lng = model.Lokacja.Lng,
                    Lat = model.Lokacja.Lat,
                    Ulica = model.Lokacja.Ulica,
                    Miasto = model.Lokacja.Miasto,
                    KodPocztowy = model.Lokacja.KodPocztowy,
                    Id = model.Lokacja.Id,
                },
                IdOperatora = idOperatora,
                ListaKategorii = kategoria,
                IdKategorii = idKategorii
            };
            var sukces = WroBL.Wydarzenia.WydarzeniaService.DodajLubEdytuj(wydarzenie, out id, out msg);

            if (!sukces)
            {
                ViewBag.Wiadomosc = msg;
                return View(model);
            }

            if (model.Kolejne)
                return View();
             return Redirect("Wydarzenia");
            
        }
    }
}

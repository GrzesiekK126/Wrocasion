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
    }
}

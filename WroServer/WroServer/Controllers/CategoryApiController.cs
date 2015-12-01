using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WroServer.Models;

namespace WroServer.Controllers
{
    public class CategoryApiController : ApiController
    {

        [ActionName("GetAllCategories")]//zwrócenie wszystkic możliwych kategorii
        public IEnumerable<JednaKategoriaModel> Get()
        {
            var model = new ListaKategoriiModel();
            var categoriesDataTable = WroBL.DAL.DatabaseUtils.EleentsToDataTable("select id,name,img_link from categories").AsEnumerable();
            model.ListaKategorii = (from item in categoriesDataTable
                                    select new JednaKategoriaModel
                                    {
                                        Id = item.Field<int>("id"),
                                        Nazwa = item.Field<string>("name"),
                                        LinkDoObrazka = item.Field<string>("img_link")
                                    }).ToList();
            return model.ListaKategorii;
        }

        [ActionName("AddOrChangeUserCategories")]//modyfikacja bądź dodanie kategorii przypisanych do użytkownika
        [HttpPost]
        public HttpResponseMessage UserCategories([FromBody]Models.UserCategories value)
        {
            //czyścimy bazę na wszelki wypadek (podczas edycji kategori łatwiej jest usunąc i wstawić na nowo, niż sprawdzać co jest i usuwać bądź dodawać)
            WroBL.DAL.DatabaseUtils.DatabaseCommand("delete from cat2user c where c.USER_ID=(select id from users where name='" + value.User + "')");
            foreach (var item in value.Categories)
            {
                WroBL.DAL.DatabaseUtils.DatabaseCommand("INSERT INTO CAT2USER (CATEGORY, USER_ID) VALUES ((select c.id from categories c where c.name='" + item + "'), (select u.id from users u where u.name='" + value.User + "'))");
            }
            return Request.CreateResponse(HttpStatusCode.OK, "ChangeOrAddCategories");
        }


        [HttpPost]
        public HttpResponseMessage DodajKategorie(Models.ModeleAPI.DodawanieKategoriiModel model)
        {
            if (WroBL.DAL.DatabaseUtils.ExistsElement("select first 1 1 from categories c where c.name='" + model.Nazwa + "'"))
            {
                return Request.CreateResponse(HttpStatusCode.OK, "Category is already exists");
            }
            else
            {
                WroBL.DAL.DatabaseUtils.DatabaseCommand("insert into categories (name,img_link) values('" + model.Nazwa + "','img')");
                JednaKategoriaModel toReturn = new JednaKategoriaModel()
                {
                    Id = Int32.Parse(WroBL.DAL.DatabaseUtils.GetOneElement("select id from categories c where c.name='" + model.Nazwa + "'")),
                    LinkDoObrazka = "img",
                    Nazwa = model.Nazwa
                };
                return Request.CreateResponse(HttpStatusCode.OK, toReturn);
            }
        }

        [HttpPost]
        public HttpResponseMessage UsunKategorie(Models.ModeleAPI.UsuwanieKategoriiModel model)
        {
            if (WroBL.DAL.DatabaseUtils.ExistsElement("select first 1 1 from cat2event c where c.CATEGORIES=" + model.Id))
                return Request.CreateResponse(HttpStatusCode.OK, "Category connect with event");
            else
            {
                WroBL.DAL.DatabaseUtils.DatabaseCommand("delete from categories where id=" + model.Id);
                return Request.CreateResponse(HttpStatusCode.OK, "Deleted");
            }
        }
    }
}
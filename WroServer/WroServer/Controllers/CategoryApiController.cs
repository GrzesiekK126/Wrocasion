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
        // GET api/CategoryApi
        public IEnumerable<JednaKategoriaModel> Get()
        {
            var model = new ListaKategoriiModel();
            var categoriesDataTable = WroBL.DAL.DatabaseUtils.EleentsToDataTable("select id,name,img_link from categories").AsEnumerable();
            model.ListaKategorii = (from item in categoriesDataTable
                                                     select new Models.JednaKategoriaModel {
                                                         Id=item.Field<int> ("id"),
                                                         Nazwa=item.Field<string> ("name"),
                                                         LinkDoObrazka=item.Field<string> ("img_link")
                                                     }).ToList();            


            return model.ListaKategorii;
        }

        // GET api/CategoryApi/5
        public string Get(int id)
        {
            return "value";
        }
        // POST api/CategoryApi
        /***DECSRIPTION***
        Funkcja pozwalając na dodawanie nowych kategorii do użytkownika, a także ich edycję
        ***END***/
        [HttpPost]
        public void User([FromBody]Models.UserCategories value)
        {
            //czyścimy bazę na wszelki wypadek (podczas edycji kategori łatwiej jest usunąc i wstawić na nowo, niż sprawdzać co jest i usuwać bądź dodawać)
            WroBL.DAL.DatabaseUtils.DatabaseCommand("delete from cat2user c where c.\"USER\"=(select id from users where name='"+value.User+"')");
            foreach (var item in value.Categories)
            {
              //TODO dopisać co ma się insertwować (trzeba dodać dwa select'y zagnieżdżone)
              //  WroBL.DAL.DatabaseUtils.DatabaseCommand("Insert into cat2user (category,user) values ()");
            }
        }

        // PUT api/CategoryApi/5
        public void Put(int id, [FromBody]string value)
        {
        }

        // DELETE api/CategoryApi/5
        public void Delete(int id)
        {
        }
    }
}

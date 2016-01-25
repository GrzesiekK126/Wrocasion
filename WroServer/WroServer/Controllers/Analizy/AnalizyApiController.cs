using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WroServer.Models.AnalizyModel;

namespace WroServer.Controllers.Analizy
{
    public class AnalizyApiController : ApiController
    {
        [HttpGet]
        [ActionName("NajpopularniejszeMiejsca")]
        public List<NajpopularniejszeKategorie> NajpopularniejszeMiejsca()
        {
            var model = WroBL.DAL.DatabaseUtils.EleentsToDataTable("select count(e2.\"EVENT\"), l.nazwa from \"EVENT\" e left join event2user e2 on e2.\"EVENT\" = e.id left join location l on l.id = e.location group by 2 order by 1 desc").AsEnumerable();
            var lista = new List<Models.AnalizyModel.NajpopularniejszeKategorie>();

            lista = (from item in model
                     select new Models.AnalizyModel.NajpopularniejszeKategorie
                     {
                         Ilosc = item.Field<int>("count"),
                         Nazwa = item.Field<string>("nazwa"),
                     }).ToList();
            return lista;
        }

        [HttpGet]
        [ActionName("NajpopularniejszeWydarzenie")]
        public List<NajpopularniejszeKategorie> NajpopularniejszeWydarzenie()
        {
            var model = WroBL.DAL.DatabaseUtils.EleentsToDataTable("select count(e2.id),e.name from  \"EVENT\" e left join event2user e2 on e2.\"EVENT\" = e.id group by 2 order by 1 desc").AsEnumerable();
            var lista = new List<Models.AnalizyModel.NajpopularniejszeKategorie>();

            lista = (from item in model
                     select new Models.AnalizyModel.NajpopularniejszeKategorie
                     {
                         Ilosc = item.Field<int>("count"),
                         Nazwa = item.Field<string>("name"),
                     }).ToList();
            return lista;
        }
    }
}

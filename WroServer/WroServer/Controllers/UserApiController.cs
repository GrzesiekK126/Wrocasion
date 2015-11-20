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
    public class UserApiController : ApiController
    {
        [ActionName("GetUsers")]
        public string Get()
        {
            return null;
        }

        [HttpPost]
        [ActionName("UserCategories")] //zwraca kategorie przypisane do uzytkownika
        public HttpResponseMessage userCategoriesList([FromBody]Models.UserModels value)
        {
            if (WroBL.DAL.DatabaseUtils.ExistsElement("select first 1 1 from users u where u.name='" + value.Name + "'"))
            {
                return Request.CreateResponse(HttpStatusCode.OK, WroBL.DAL.DatabaseUtils.ListOfElementsFromDatabase(
                        "select cu.id, c.name  from cat2user cu left join categories c on (cu.category = c.id) where cu.user_id = (select u.id from users u where u.name = '" +
                        value.Name + "')"));
            }
            else
            {
                return Request.CreateResponse(HttpStatusCode.NotFound, "User not found in database");
            }
        }

        [HttpPost]
        [ActionName("AddUser")] //dodaje nowego użytkownika od bazy
        public HttpResponseMessage Add([FromBody]UserModels value)
        {
            if(WroBL.DAL.DatabaseUtils.ExistsElement("select first 1 1 from users u where u.name='" + value.Name + "'"))
            {
                return Request.CreateResponse(HttpStatusCode.NotFound, "User with that name already exists");
            }
            else
            {
                WroBL.DAL.DatabaseUtils.DatabaseCommand("Insert into users(name) values ('" + value.Name + "')");
                return Request.CreateResponse(HttpStatusCode.OK, "New user add correctly");
            }
        }

        [HttpPost]
        [ActionName("RemoveUser")] //usunięcie użytkownika z bazy
        public HttpResponseMessage Remove([FromBody]UserModels value)
        {
            if (WroBL.DAL.DatabaseUtils.ExistsElement("select first 1 1 from users u where u.name='" + value.Name + "'"))
            {
                WroBL.DAL.DatabaseUtils.DatabaseCommand("delete from users where name='" + value.Name + "'");
                return Request.CreateResponse(HttpStatusCode.OK, "User deleted correctly");
            }
            else
            {
                return Request.CreateResponse(HttpStatusCode.NotFound, "User with that name doesn't exists");
            }

        }
    }
}

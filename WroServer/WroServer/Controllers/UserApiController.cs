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
        public string Get()
        {
            return WroBL.DAL.DatabaseUtils.GetOneElement("select name from users");
        }
        
		//TODO trzeba dopisać logikę insertującą nowych użytkowników do bazy danych
        [HttpPost]
        public HttpResponseMessage User([FromBody]UserModels value)
        {
            WroBL.DAL.DatabaseUtils.DatabaseCommand("Insert into users(name) values ('"+value.Name+"')");
            return Request.CreateResponse(HttpStatusCode.OK, "InsertComplete");
        }

        [HttpPost]
        public HttpResponseMessage Remove([FromBody]UserModels value)
        {
            if (WroBL.DAL.DatabaseUtils.ExistsElement("select first 1 1 from users u where u.name='" + value.Name + "'"))
            {
                WroBL.DAL.DatabaseUtils.DatabaseCommand("delete from users where name='" + value.Name + "'");
                return null;
            }
            else
            {
                return Request.CreateResponse(HttpStatusCode.NotFound, "User with that name doesn't exists");
            }

        }
    }
}

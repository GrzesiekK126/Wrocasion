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
        public IEnumerable<JednaKategoriaModel> Get()
        {
            return null;
        }

        // GET api/CategoryApi/5
        public string Get(int id)
        {
            return "value";
        }
        // POST api/UserApi

		//TODO trzeba dopisać logikę insertującą nowych użytkowników do bazy danych
        [HttpPost]
        public HttpResponseMessage User([FromBody]Models.UserModels value)
        {

            return null;
        }

        // PUT api/UserApi/5
        public void Put(int id, [FromBody]string value)
        {
        }

        // DELETE api/UserApi/5
        public void Delete(int id)
        {
        }
    }
}

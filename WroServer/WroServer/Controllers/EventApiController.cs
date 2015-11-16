using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace WroServer.Controllers
{
    public class EventApiController : ApiController
    {
        // GET api/CategoryApi
        public IEnumerable<string> Get()
        {
            return null;
        }

        // GET api/CategoryApi/5
        public string Get(int id)
        {
            return "value";
        }
        // POST api/CategoryApi
        public void Post([FromBody]Models.ListaKategoriiModel value)
        {

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

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
    public class OperatorController : Controller
    {
        public ActionResult Operator()
        {
            var nowyOperator = new Models.OperatorModel
            {
                Id = 1,
                Login = "Madonna",
                Name = "Olga",
                Surname = "Kowalska",
                Password = "aaa",
                Contact = "7770000777",
                ContactForm = 1,
                Role = 0
            };
            return View(nowyOperator);
        }
    }
}
 
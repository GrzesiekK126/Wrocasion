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
            return View();
        }
    }
}

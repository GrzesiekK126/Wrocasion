using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WRO.BL;
using WRO.Web.Models;

namespace WRO.Web.Controllers
{
    public class HomeController : Controller
    {
        IBundleService bundleService;

        public ActionResult Index()
        {
            ViewBag.Title = "Panel administracyjny";

            return View();
        }

    }
}

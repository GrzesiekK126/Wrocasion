using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WroBL;

namespace WroServer.Controllers
{
    [Authorize]
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            try
            {
                ViewBag.Przywitanie = "Witaj " + User.Identity.Name;
            }
            catch(NullReferenceException)
            {
                ViewBag.Przywitanie = ":)";
            }
            //return RedirectToAction("Login", "Account");
            ViewBag.Message = "Modify this template to jump-start your ASP.NET MVC application.";

            return View();
        }

        public ActionResult About()
        {
            //var aa = WroBL.ExampleService.Testowy();

           // aa.ToString();
            ViewBag.Message = "Your app description page.";

            return View();
        }
    }
}

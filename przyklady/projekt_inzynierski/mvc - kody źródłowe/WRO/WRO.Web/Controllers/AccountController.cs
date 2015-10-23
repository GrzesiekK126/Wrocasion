using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Principal;
using System.Web;
using System.Web.Mvc;
using System.Web.Security;
using WRO.Web.Models;

namespace WRO.Web.Controllers
{
    public class AccountController : Controller
    {

        [AllowAnonymous]
        public ActionResult Login()
        {
            if (HttpContext.User.Identity.IsAuthenticated)
                return Redirect("../Home/Index");

            return View();
        }

        [AllowAnonymous]
        [HttpPost]
        public ActionResult Login(LoginViewModel model)
        {
            string correctLogin = System.Web.Configuration.WebConfigurationManager.AppSettings["Login"];
            string correctPassword = System.Web.Configuration.WebConfigurationManager.AppSettings["Password"];
            if ( model.LoginVal == correctLogin && model.Password == correctPassword)
            {
                FormsAuthentication.SetAuthCookie(model.LoginVal, model.RememberMe);
                return RedirectToAction("../Home/Index");
            }
            else
            {
                ModelState.AddModelError("BadPass", "Login lub hasło są niepoprawne.");
   
                return View(model);
            }
        }
        [Authorize]
        public ActionResult Logout()
        {
            FormsAuthentication.SignOut();
            HttpContext.User = new GenericPrincipal(new GenericIdentity(""), null);
            return View("Login");
        }

    }
}
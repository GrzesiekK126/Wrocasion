using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Principal;
using System.Transactions;
using System.Web;
using System.Web.Mvc;
using System.Web.Security;
using WroServer.Models;

namespace WroServer.Controllers
{
    [Authorize]
    public class AccountController : WroController
    {
        //
        // GET: /Account/Login

        [AllowAnonymous]
        public ActionResult Login(string returnUrl)
        {
            if (HttpContext.User.Identity.IsAuthenticated)
                return Redirect("../Home/Index");

            IPrincipal gp = HttpContext.User;   // clearing user data in context

            ViewBag.ReturnUrl = returnUrl;

            return View();
        }

        //
        // POST: /Account/Login

        [HttpPost]
        [AllowAnonymous]
        [ValidateAntiForgeryToken]
        public ActionResult Login(LoginModel model, string returnUrl)
        {
            //sprawdzamy czy dane logowania sa poprawne
            if (WroBL.Logowanie.Waliduj(model.UserName,model.Password))
            {
                //ustawiamy ciasteczko
                FormsAuthentication.SetAuthCookie(model.UserName, model.RememberMe);
                return RedirectToLocal(returnUrl);
                //return RedirectToAction("../Home/Index");
            }
            else
            {
                //informujemy o bledzie
                ModelState.AddModelError("BadPass", "Nazwa użytkownika lub hasło niepoprawne");
                return View(model);
            }
        }

        //
        // POST: /Account/LogOff

        //[HttpPost]
        //[ValidateAntiForgeryToken]
        public ActionResult LogOff()
        {
            HttpContext.User = new GenericPrincipal(new GenericIdentity(""), null);

            FormsAuthentication.SignOut();
            return RedirectToAction("Login","Account");
        }

        #region Helpers
        private ActionResult RedirectToLocal(string returnUrl)
        {
            if (Url.IsLocalUrl(returnUrl))
            {
                return Redirect(returnUrl);
            }
            else
            {
                return RedirectToAction("Index", "Home");
            }
        }
        #endregion
    }
}

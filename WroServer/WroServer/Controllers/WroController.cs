using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Principal;
using System.Web;
using System.Web.Mvc;

namespace WroServer.Controllers
{
    public abstract class WroController : Controller
    {
        protected override void OnAuthorization(AuthorizationContext filterContext)
        {
            base.OnAuthorization(filterContext);

            //Zapisanie pryncypala z rolami
            if (HttpContext.User != null && HttpContext.User.Identity!=null && !string.IsNullOrEmpty(HttpContext.User.Identity.Name))
            {
                string[] tablicaRol = new string[1];

                string nazwaUzytkownika = HttpContext.User.Identity.Name;

                var rola = WroBL.Logowanie.RolaUzytkownika(nazwaUzytkownika);


                if (rola != WroBL.Logowanie.Rola.Gosc)
                {
                    tablicaRol[0] = WroBL.Logowanie.NazwaRoli(rola);

                    GenericPrincipal principal = new GenericPrincipal(HttpContext.User.Identity, tablicaRol);
                    HttpContext.User = principal;

                }
                //Update daty ostatniego zalogowania
                if (HttpContext.User.Identity.Name != null && WroBL.Logowanie.UzytkownikIstnieje(nazwaUzytkownika))
                    WroBL.Logowanie.ZaktualizujDateOstatniegoLogowania(nazwaUzytkownika,true);
            }
        }

        [ChildActionOnly]
        public string CurrentUserRole()
        {
            if (HttpContext.User != null)
            {
                var rola = WroBL.Logowanie.RolaUzytkownika(HttpContext.User.Identity.Name);

                return WroBL.Logowanie.NazwaRoli(rola);
            }
            return WroBL.Logowanie.NazwaRoli(WroBL.Logowanie.Rola.Gosc); ;
        }

        [ChildActionOnly]
        public string UserRole(string email)
        {
            var rola = WroBL.Logowanie.RolaUzytkownika(email);

            return WroBL.Logowanie.NazwaRoli(rola);
        }

    }
}

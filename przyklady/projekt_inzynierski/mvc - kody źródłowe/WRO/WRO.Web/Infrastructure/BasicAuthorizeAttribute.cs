using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http.Headers;
using System.Text;
using System.Web;
using System.Web.Http.Filters;
using System.Net.Http;
using WRO.BL;
using System.Web.Http.Controllers;
using System.Diagnostics.Contracts;
using System.Web.Http;

namespace WRO.Web.Infrastructure
{
    public class BasicAuthorizeAttribute : AuthorizationFilterAttribute
    {
        public override void OnAuthorization(HttpActionContext filterContext)
        {
            //allow anonymous
            if (SkipAuthorization(filterContext))
            {
                return;
            }

            if (filterContext == null)
            {
                throw new ArgumentNullException("filterContext");
            }

            var authHeader = filterContext.Request.Headers.Authorization;

            if (authHeader != null)
            {
                if (authHeader.Scheme.Equals("basic", StringComparison.OrdinalIgnoreCase) && !string.IsNullOrWhiteSpace(authHeader.Parameter))
                {
                    try
                    {
                        var credentials = GetCredentials(authHeader);

                        IAccountService accountService = new AccountService();

                        if (credentials.Length == 2 && accountService.Authenticate(credentials[0], credentials[1]))
                            return;
                    }
                    catch (Exception)
                    {
                        HandleUnauthorizedRequest(filterContext);
                    }
                }
            }

            HandleUnauthorizedRequest(filterContext);
        }

        private static bool SkipAuthorization(HttpActionContext actionContext)
        {
            if (!Enumerable.Any<AllowAnonymousAttribute>((IEnumerable<AllowAnonymousAttribute>)actionContext.ActionDescriptor.GetCustomAttributes<AllowAnonymousAttribute>()))
                return Enumerable.Any<AllowAnonymousAttribute>((IEnumerable<AllowAnonymousAttribute>)actionContext.ControllerContext.ControllerDescriptor.GetCustomAttributes<AllowAnonymousAttribute>());
            else
                return true;
        }

        private string[] GetCredentials(AuthenticationHeaderValue authHeader)
        {
            var raw = authHeader.Parameter;
            var encoding = Encoding.ASCII;
            var credentials = encoding.GetString(Convert.FromBase64String(raw));
            return credentials.Split(':');
        }

        protected void HandleUnauthorizedRequest(System.Web.Http.Controllers.HttpActionContext actionContext)
        {
            actionContext.Response = actionContext.Request.CreateResponse(HttpStatusCode.Unauthorized);
        }

    }
}
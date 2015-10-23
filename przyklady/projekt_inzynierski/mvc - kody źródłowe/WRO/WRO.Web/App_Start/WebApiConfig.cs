using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;

namespace WRO.Web
{
    public static class WebApiConfig
    {
        public static void Register(HttpConfiguration config)
        {
            // Web API configuration and services

            // Web API routes
            config.MapHttpAttributeRoutes();

            config.Routes.MapHttpRoute(
                name: "DefaultApi",
                routeTemplate: "api/{controller}/{action}/{id}",
                defaults: new { id = RouteParameter.Optional }
            );
            config.Routes.MapHttpRoute(
                name: "WithUserId",
                routeTemplate: "api/{controller}/{action}/{id}/{user_id}",
                defaults: new { id = RouteParameter.Optional, user_id = RouteParameter.Optional }
            );
            config.Filters.Add(new Infrastructure.BasicAuthorizeAttribute());
        }
    }
}

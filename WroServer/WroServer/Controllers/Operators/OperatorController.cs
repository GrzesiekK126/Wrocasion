using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Mvc;
using WroServer.Models;

namespace WroServer.Controllers.Operators
{
    public class OperatorController : Controller
    {
        public ActionResult Operator()
        {
            var model = new Models.OperatorList();
            var operatorsDataTable =
                WroBL.DAL.DatabaseUtils.EleentsToDataTable(
                    "select o.login, o.name,o.surname, o.contact, o.contact_form, o.role from operator o")
                    .AsEnumerable();
            model.listOfOperators=(from item in operatorsDataTable
                select new Models.OperatorModel()
                {
                    Login = item.Field<string>("login"),
                    Name = item.Field<string>("name"),
                    Surname = item.Field<string>("surname"),
                    ContactForm = item.Field<int>("contact_form"),
                    Contact = item.Field<string>("contact"),
                    Role = item.Field<int>("role")
                }).ToList();
            return View(model.listOfOperators);
        }
    }
}
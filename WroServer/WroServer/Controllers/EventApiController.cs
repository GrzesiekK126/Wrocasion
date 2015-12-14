using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace WroServer.Controllers
{
    public class EventApiController : ApiController
    {

        [ActionName("getFormAndoid")]
        [HttpGet]
        public HttpResponseMessage getFromAndroid()
        {
            var model = new Models.EventModels.FromAndroidModel();
            model.UserName = "janekKowalski11214";
            model.Latitude = (decimal) 62.11;
            model.Longtitude = (decimal) 12.23;
            return Request.CreateResponse(HttpStatusCode.OK, model);
        }

        [ActionName("EventToAndroid")]//modyfikacja bądź dodanie kategorii przypisanych do użytkownika
        [HttpPost]
        public HttpResponseMessage UserCategories([FromBody]Models.EventModels.FromAndroidModel value)
        {
            string userName = "";
            string categories = "";
            if (String.IsNullOrEmpty(value.UserName))
            {
                userName = "null";
            }
            else
            {
                userName = "'" + value.UserName + "'";
            }
            /*
            if (value.Categories.Count == 0)
            {
                categories = "null";
            }
            else
            {
                foreach (var item in value.Categories)
                {
                    categories = categories + item;
                }
            }
            */
            var model = new Models.EventModels.EventsList();
            var EventsDatatable = WroBL.DAL.DatabaseUtils.EleentsToDataTable("select e.id, e.nazwa, e.data, e.street, e.city, e.zipcode, e.price, e.image, e.operator, e.adddata, e.link,"+
                                                                              " e.categoriesout, e.locationid, e.outlongtitude, e.outlatitude, e.takingpart"+
                                                                              " from event_select_android("+userName+", null, null) e").AsEnumerable();
            model.ListOfEventModels = (from item in EventsDatatable
                select new Models.EventModels.EventModel()
                {
                    Id = item.Field<int>("id"),
                    Nazwa = item.Field<string>("nazwa"),
                    Data = item.Field<DateTime>("data"),
                    LocationId = item.Field<int>("locationid"),
                    Street = item.Field<string>("street"),
                    City = item.Field<string> ("city"),
                    ZipCode = item.Field<string>("zipcode"),
                    Price = item.Field<decimal>("price"),
                    Image = item.Field<string>("image"),
                    AddData = item.Field<DateTime>("adddata"),
                    Categories = item.Field<string>("categoriesout"),
                    Longtitude = item.Field<decimal> ("outlongtitude"),
                    Latitude = item.Field<decimal>("outlatitude"),
                    TakingPart = item.Field<int>("takingpart"),
                    Link = item.Field<string>("link")
                }).ToList();
            return Request.CreateResponse(HttpStatusCode.OK, model.ListOfEventModels);
        }
    }
}

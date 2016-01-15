using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WroServer.Models.UserModel;

namespace WroServer.Controllers
{
    public class EventApiController : ApiController
    {

        [ActionName("getFormAndoid")]
        [HttpGet]
        public HttpResponseMessage getFromAndroid()
        {
            var model = new Models.UserModel.UserEventModel();
            model.Username = "123";
            return Request.CreateResponse(HttpStatusCode.OK, model);
        }

        [ActionName("EventToAndroid")]
        [HttpPost]
        public HttpResponseMessage UserCategories([FromBody]Models.EventModels.FromAndroidModel value)
        {
            if (value.UserName == "" || WroBL.DAL.DatabaseUtils.ExistsElement("select first 1 1 from users u where u.name='" + value.UserName + "'"))
            {


                var _username = String.IsNullOrEmpty(value.UserName) ? "null" : "'" + value.UserName + "'";
                var _longtitude = value.Longtitude == -1 ? "null" : value.Longtitude.ToString().Replace(',','.');
                var _latitude = value.Latitude == -1 ? "null" : value.Latitude.ToString().Replace(',', '.');

                var model = new Models.EventModels.EventsList();
                var str = "select e.id, e.nazwa, e.data, e.street, e.city, e.zipcode, e.price, e.image, e.operator, e.adddata, e.link," +
                                                                                  " e.categoriesout, e.locationid, e.outlongtitude, e.outlatitude, e.takingpart, e.description, e.locationname" +
                                                                                  " from event_select_android(" + _username + ", " + _longtitude + ", " + _latitude + ") e";
                var EventsDatatable = WroBL.DAL.DatabaseUtils.EleentsToDataTable(str).AsEnumerable();
                model.ListOfEventModels = (from item in EventsDatatable
                                           select new Models.EventModels.EventModel()
                                           {
                                               Id = item.Field<int>("id"),
                                               Nazwa = item.Field<string>("nazwa"),
                                               Data = item.Field<DateTime>("data"),
                                               LocationId = item.Field<int>("locationid"),
                                               Street = item.Field<string>("street"),
                                               City = item.Field<string>("city"),
                                               ZipCode = item.Field<string>("zipcode"),
                                               Price = item.Field<decimal>("price"),
                                               Image = item.Field<string>("image"),
                                               AddData = item.Field<DateTime>("adddata"),
                                               Categories = item.Field<string>("categoriesout"),
                                               Longtitude = item.Field<decimal>("outlongtitude"),
                                               Latitude = item.Field<decimal>("outlatitude"),
                                               TakingPart = item.Field<int>("takingpart"),
                                               Link = item.Field<string>("link"),
                                               Description = item.Field<string>("description"),
                                               LocationName = item.Field<string>("locationname")
                                           }).ToList();


                return Request.CreateResponse(HttpStatusCode.OK, model.ListOfEventModels);
            }
            else
            {
                var response = new Models.UserModel.UserResponseModel {
                    Id = -1,
                    Message = "User with that name dont't exists" 
                };
                return Request.CreateResponse(HttpStatusCode.OK, response);
            }
        }

        [ActionName("UserEvent")]
        [HttpPost]
        public HttpResponseMessage UserEvent([FromBody] Models.UserModel.UserEventModel model)
        {

            var userID = WroBL.DAL.DatabaseUtils.GetOneElement("SELECT U.ID FROM USERS U WHERE U.NAME='"+model.Username+"';");
            if (String.IsNullOrEmpty(userID))
            {
                Models.UserModel.UserResponseModel response = new UserResponseModel();
                response.Message = "User with that name doesn't exists";
                return Request.CreateResponse(HttpStatusCode.OK, response);
            }
            else
            {
                model.Events = WroBL.DAL.DatabaseUtils.ListOfElementsFromDatabase("SELECT distinct(E2U.\"EVENT\") "+
                                                                                    "FROM EVENT2USER E2U "+
                                                                                    "LEFT JOIN \"EVENT\" e on e2u.\"EVENT\" = e.id "+
                                                                                    "WHERE e2u.\"USER\" = '"+userID+"' "+
                                                                                      "AND e.\"DATE\" > current_timestamp; ");
                return Request.CreateResponse(HttpStatusCode.OK,model);
            }
            
        }

        [ActionName("UserTakingPart")]
        [HttpPost]
        public HttpResponseMessage UserTakingPart([FromBody] Models.UserModel.UserEventModel value)
        {
            var userID =
                WroBL.DAL.DatabaseUtils.GetOneElement("SELECT U.ID FROM USERS U WHERE U.NAME='" + value.Username +"';");
            if (!String.IsNullOrEmpty(userID))
            {

                if (value.TakingPart)
                {

                    //insert into table event to user
                    WroBL.DAL.DatabaseUtils.DatabaseCommand("Insert into event2user (\"EVENT\",\"USER\") values ("+value.EventIdToTakingPart+","+userID+");");
                    Models.UserModel.UserResponseModel response = new UserResponseModel();
                    response.Message = "User taking part in an event";
                    return Request.CreateResponse(HttpStatusCode.OK, response);
                }
                else
                {
                    //delete from table event2user
                    WroBL.DAL.DatabaseUtils.DatabaseCommand("delete from event2user e where (e.\"EVENT\"="+value.EventIdToTakingPart+" and e.\"USER\"="+userID+")");
                    Models.UserModel.UserResponseModel response = new UserResponseModel();
                    response.Message = "User no longer taking part in event";
                    return Request.CreateResponse(HttpStatusCode.OK, response);
                }
            }
            else
            {
                Models.UserModel.UserResponseModel response = new UserResponseModel();
                response.Message = "User with that name doesn't exists";
                return Request.CreateResponse(HttpStatusCode.OK, response);
            }
        }

    }
}

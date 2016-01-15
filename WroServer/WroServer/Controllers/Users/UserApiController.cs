using System;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WroServer.Models;
using WroServer.Models.UserModel;

namespace WroServer.Controllers
{
    public class UserApiController : ApiController
    {
        [ActionName("GetUsers")]
        public string Get()
        {
            return null;
        }

        [HttpPost]
        [ActionName("UserCategories")] //zwraca kategorie przypisane do uzytkownika
        public HttpResponseMessage UserCategoriesList([FromBody] Models.UserModels value)
        {
            if (WroBL.DAL.DatabaseUtils.ExistsElement("select first 1 1 from users u where u.name='" + value.Name + "'"))
            {
                var model = new UserCategories();
                model.User = value.Name;
                model.Categories = WroBL.DAL.DatabaseUtils.ListOfElementsFromDatabase(
                    "select c.name  from cat2user cu left join categories c on (cu.category = c.id) where cu.user_id = (select u.id from users u where u.name = '" +
                    value.Name + "')");
                return Request.CreateResponse(HttpStatusCode.OK, model);
            }
            else
            {
                return Request.CreateResponse(HttpStatusCode.NotFound, "User not found in database");
            }
        }

        [HttpPost]
        [ActionName("AddUser")] //dodaje nowego użytkownika od bazy
        public HttpResponseMessage Add([FromBody] UserModels value)
        {
            if (WroBL.DAL.DatabaseUtils.ExistsElement("select first 1 1 from users u where u.name='" + value.Name + "'"))
            {
                var response = new UserResponseModel()
                {
                    Id = int.Parse(WroBL.DAL.DatabaseUtils.GetOneElement("select u.id from users u where u.name='" + value.Name + "'")),
                    Message = "User with that name already exists"

                };
                return Request.CreateResponse(HttpStatusCode.NotFound, "User with that name already exists");
            }
            else
            {

                if (String.IsNullOrWhiteSpace(value.Email))
                {
                    WroBL.DAL.DatabaseUtils.DatabaseCommand("Insert into users(name, facebook) values ('" + value.Name + "',1)");
                    var response = new UserResponseModel()
                    {
                        Id = int.Parse(WroBL.DAL.DatabaseUtils.GetOneElement("select u.id from users u where u.name='"+value.Name+"'")),
                        Message = "Add new user by Facebook"
                       
                    };
                    return Request.CreateResponse(HttpStatusCode.OK, response);
                }
                else
                {
                    var enPassword = WroBL.DAL.Crytography.EncryptRijndaelManaged(value.Password,
                        WroBL.DAL.Crytography.ElChupacabra, WroBL.DAL.Crytography.ElMariachi);
                    var password = WroBL.DAL.Crytography.GetString(enPassword);

                    WroBL.DAL.DatabaseUtils.DatabaseCommand("INSERT INTO USERS(NAME, \"PASSWORD\", EMAIL, FACEBOOK) " +
                                                            "VALUES('" + value.Name + "', '" + password + "','" +
                                                            value.Email + "',0);");
                    var response = new UserResponseModel()
                    {
                        Id = Int32.Parse(WroBL.DAL.DatabaseUtils.GetOneElement("select u.id from users u where u.name='" + value.Name + "';")),
                        Message = "Add new user by registartion"

                    };
                    return Request.CreateResponse(HttpStatusCode.OK, response);
                }

            }
        }

        [HttpPost]
        [ActionName("RemoveUser")] //usunięcie użytkownika z bazy
        public HttpResponseMessage Remove([FromBody] UserModels value)
        {
            if (WroBL.DAL.DatabaseUtils.ExistsElement("select first 1 1 from users u where u.name='" + value.Name + "'"))
            {
                WroBL.DAL.DatabaseUtils.DatabaseCommand("delete from users where name='" + value.Name + "'");
                return Request.CreateResponse(HttpStatusCode.OK, "User deleted correctly");
            }
            else
            {
                return Request.CreateResponse(HttpStatusCode.NotFound, "User with that name doesn't exists");
            }

        }

        [HttpPost]
        [ActionName("LoginUser")]
        public HttpResponseMessage LoginUser([FromBody] UserModels value)
        {
            var response = new UserResponseModel();

            if (WroBL.DAL.DatabaseUtils.ExistsElement("select first 1 1 from users u where u.name='" + value.Name + "'"))
            {
                if (WroBL.DAL.DatabaseUtils.GetOneElement("select u.facebook from users u where u.name='" + value.Name + "'") == "1")
                {
                    response.CorrectLogin = true;
                    response.Message = "You login correct By Facebook";
                    return Request.CreateResponse(HttpStatusCode.OK, response);
                }
                else
                {

                
                if (WroBL.UserLogin.ComparePassword(value.Password, value.Name))
                {
                    response.Id =
                        Int32.Parse(
                            WroBL.DAL.DatabaseUtils.GetOneElement("select u.id from users u where u.name='" + value.Name +
                                                                  "';"));
                    response.CorrectLogin = true;
                    response.Message = "Username and password is correct";
                    return Request.CreateResponse(HttpStatusCode.OK, response);
                }
                else
                {
                    response.Id =
                        Int32.Parse(
                            WroBL.DAL.DatabaseUtils.GetOneElement("select u.id from users u where u.name='" + value.Name +
                                                                  "';"));
                    response.CorrectLogin = true;
                    response.Message = "Password is incorrect";
                    return Request.CreateResponse(HttpStatusCode.OK, response);
                }
            } 
            }
            else
            {
                response.CorrectLogin = false;
                response.Message = "Username is incorrect";
                return Request.CreateResponse(HttpStatusCode.OK, response);
            }
        }
        

    }
}

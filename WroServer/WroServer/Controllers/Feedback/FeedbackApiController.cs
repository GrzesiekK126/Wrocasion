using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WroServer.Models.UserModel;

namespace WroServer.Controllers.Feedback
{
    public class FeedbackApiController : ApiController
    {
       /* [ActionName("ApplicationFeedback")]
        public HttpResponseMessage ApplicaionFeedback(Models.FeedbackModel.FeedbackModel model)
        {
            var userId =
                WroBL.DAL.DatabaseUtils.GetOneElement("SELECT U.NAME FROM USERS U WHERE U.NAME='" + model.Username +"';");
            if (!WroBL.DAL.DatabaseUtils.ExistsElement("SELECT FIRST 1 1 FROM FEEDBACK_APP F WHERE F.USER=" + userId))
            {
            }
            else
            {
                Models.UserModel.UserResponseModel response =new Models.UserModel.UserResponseModel();
                response.Message = "User already vote this app";
                ;
            }
            return null;
        }*/

        [ActionName("EventsToFeedback")]
        public HttpResponseMessage EventsToFeedback(Models.FeedbackModel.FeedbackModel model)
        {
            var userId = WroBL.DAL.DatabaseUtils.GetUserId(model.Username);

            if (String.IsNullOrEmpty(userId))
            {
                Models.UserModel.UserResponseModel response = new UserResponseModel();
                response.Message="user with that name don't exists";
                return Request.CreateResponse(HttpStatusCode.OK, response);
            }
            else
            {
                model.EventsId = WroBL.DAL.DatabaseUtils.ListOfElementsFromDatabase("SELECT distinct(E2U.\"EVENT\") " +
                                                                                    "FROM EVENT2USER E2U " +
                                                                                    "LEFT JOIN \"EVENT\" e on e2u.\"EVENT\" = e.id " +
                                                                                    "WHERE e2u.\"USER\" = '" + userId+ "' " +
                                                                                      "AND e.\"DATE\" < current_timestamp; ");
                return Request.CreateResponse(HttpStatusCode.OK, model);
            }
        }
    }
}

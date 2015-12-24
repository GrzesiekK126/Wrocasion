using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace WroServer.Controllers.Operators
{
    public class OperatorApiController : ApiController
    {
        
        [ActionName("AddOperator")]
        [HttpPost]
        public HttpResponseMessage AddOperator([FromBody] Models.OperatorModel operatorModel)
        {   var oper =  new Models.OperatorModel();
            if (OperatorValidate.ValidateOperator(operatorModel))
            {
                AddAndRemoveOperator.AddOperator(operatorModel);

                oper.Id =
                    Int32.Parse(
                        WroBL.DAL.DatabaseUtils.GetOneElement("select id from operator c where c.name='" +
                                                              operatorModel.Name + "'"));
                oper.Login = operatorModel.Login;
                oper.Name = operatorModel.Name;
                oper.Surname = operatorModel.Surname;
                oper.Contact = operatorModel.Contact;

                return Request.CreateResponse(HttpStatusCode.OK, oper);
            }
            else
            {
                return Request.CreateResponse(HttpStatusCode.OK, operatorModel.Login);
            }
        }

        //TODO dopisać logikę
        [ActionName("RemoveOperator")]
        [HttpPost]
        public HttpResponseMessage RemoveOperator([FromBody] Models.OperatorModel operatorModel)
        {
            return null;
        }
    }

    
}

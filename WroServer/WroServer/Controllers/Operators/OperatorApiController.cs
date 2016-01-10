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
            if(WroBL.DAL.DatabaseUtils.ExistsElement("select first 1 1 from operator o where o.id ="+operatorModel.Id))
            {
                WroBL.DAL.DatabaseUtils.DatabaseCommand("delete from operator where id="+operatorModel.Id);
                return Request.CreateResponse(HttpStatusCode.OK, operatorModel.Id);
            }
            return Request.CreateResponse(HttpStatusCode.OK, "noExist");
        }

        //TODO dopisać logikę
        [ActionName("ChangePassword")]
        [HttpPost]
        public HttpResponseMessage ChangePassword([FromBody] Models.OperatorModel operatorModel)
        {
            return null;
        }

        //TODO dopisać logikę
        [ActionName("UpdateOperator")]
        [HttpPost]
        public HttpResponseMessage UpdateOperator([FromBody] Models.OperatorModel operatorModel)
        {
            if (OperatorValidate.ValidateOperatorForEdit(operatorModel))
            {
                WroBL.DAL.DatabaseUtils.DatabaseCommand("update operator o set o.login='"+operatorModel.Login+"',o.name='"+operatorModel.Name+"',o.surname='"+operatorModel.Surname+"', o.contact='"+operatorModel.Contact+"',o.contact_form="+operatorModel.ContactForm+",o.\"ROLE\"="+operatorModel.Role+" where o.id="+operatorModel.Id+";");
                return Request.CreateResponse(HttpStatusCode.OK, operatorModel.Id);
            }
            return Request.CreateResponse(HttpStatusCode.OK, "failValidate");
        }


    }


}

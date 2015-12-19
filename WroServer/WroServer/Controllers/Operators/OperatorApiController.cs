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
        //TODO dopisać logikę
        [ActionName("AddOperator")]
        [HttpPost]
        public HttpResponseMessage AddOperator([FromBody] Models.OperatorModel operatorModel)
        {
            // if (OperatorValidate.ValidateOperator(operatorModel))
            // {
            //     WroBL.DAL.DatabaseUtils.DatabaseCommand("");
            //     return Request.CreateResponse(HttpStatusCode.OK, operatorModel.Name);
            // }
            if (OperatorValidate.ValidateOperator(operatorModel))
            {
                AddAndRemoveOperator.AddOperator(operatorModel);
                return Request.CreateResponse(HttpStatusCode.OK, operatorModel.Login);
            }
            return null;
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

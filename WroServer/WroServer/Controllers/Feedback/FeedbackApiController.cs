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
        
        [ActionName("RateEvent")]
        [HttpPost]
        public HttpResponseMessage RateEvent(Models.FeedbackModels.FeedbackModel model)
        {
            
            if (model == null)
                return Request.CreateResponse(HttpStatusCode.OK, new Models.SpecjalnyModelNaMarudzenieGrzesiaIOdbieranieJegoJSONow()
                {
                    SpecjalnyModelDlaGrzesia = "Nie przekazano żadnych danych."
                });

            #region pobranie ID usera na podstawie nazwy
            var idUsera = WroBL.DAL.DatabaseUtils.GetOneElement("select ID from USERS where NAME='" + model.UserName + "'");

            if(String.IsNullOrEmpty(idUsera))
                return Request.CreateResponse(HttpStatusCode.OK, new Models.SpecjalnyModelNaMarudzenieGrzesiaIOdbieranieJegoJSONow()
                {
                    SpecjalnyModelDlaGrzesia = "Podany użytkownik nie istnieje w bazie."
                });
            #endregion
            
            //Jeśli dano rate spoza zakresu 1-5, to ograniczamy go do wartości granicznych (1 lub 5)
            var rate = model.Rate < 1 ? 1 : (model.Rate > 5 ? 5 : model.Rate);

            if (model.EventId < 0)
                ZapiszOceneAplikacji(model.Description, idUsera, rate);
            else
            {
                #region pobranie id event2user
                var idPolaczenia = WroBL.DAL.DatabaseUtils.GetOneElement("select ID from EVENT2USER u WHERE u.\"USER\" = " + idUsera + " AND u.\"EVENT\" = " + model.EventId);

                if (idPolaczenia == null || idPolaczenia.Equals(string.Empty))
                    return Request.CreateResponse(HttpStatusCode.OK, new Models.SpecjalnyModelNaMarudzenieGrzesiaIOdbieranieJegoJSONow()
                    {
                        SpecjalnyModelDlaGrzesia = "Podany użytkownik nie ma w zapisanych tego wydarzenia."
                    });
                #endregion

                ZapiszOceneEventu(model.Description, rate, idPolaczenia);
            }
            return Request.CreateResponse(HttpStatusCode.OK, new Models.SpecjalnyModelNaMarudzenieGrzesiaIOdbieranieJegoJSONow()
            {
                SpecjalnyModelDlaGrzesia = "OK"
            });
        }

        private void ZapiszOceneAplikacji(string desc, string userId, int rate)
        {
            var id = WroBL.DAL.DatabaseUtils.GetOneElement("select ID from FEEDBACK_APP f where f.\"USER\" like " + userId + "");

            if (id != null && !id.Equals(string.Empty))
            {
                //ocena juz istniała, trzeba ją zaktualizować
                WroBL.DAL.DatabaseUtils.DatabaseCommand("update FEEDBACK_APP f set f.\"DESCRIPTION\"='"+desc+"',f.\"RATE\"="+rate+",f.\"USER\"="+userId+" where f.id="+id);
            }
            else
            {
                //oceny jeszcze nie było, trzeba ją utworzyć
                WroBL.DAL.DatabaseUtils.DatabaseCommand("insert into FEEDBACK_APP (\"DESCRIPTION\",\"RATE\",\"USER\") values ('"+desc+"',"+rate+","+userId+");");
            }
        }

        private void ZapiszOceneEventu(string desc, int rate, string idPolaczenia)
        {
            WroBL.DAL.DatabaseUtils.DatabaseCommand("update EVENT2USER o set o.DESCRIPTION='" + desc + "',o.RATE=" + rate + " where o.id=" + idPolaczenia);
        }
    }
}

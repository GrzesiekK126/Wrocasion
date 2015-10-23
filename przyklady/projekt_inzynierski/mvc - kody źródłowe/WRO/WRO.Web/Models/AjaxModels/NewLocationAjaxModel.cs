using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WRO.Web.Models.AjaxModels
{
    public class NewLocationAjaxModel
    {
        public int id { get; set; }
        public string latitude { get; set; }
        public string longitude { get; set; }
        public string name { get; set; }
        public int task_id { get; set; }
    }
}
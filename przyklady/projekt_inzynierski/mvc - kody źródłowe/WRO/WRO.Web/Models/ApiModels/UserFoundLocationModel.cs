using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WRO.Web.Models.ApiModels
{
    public class UserFoundLocationModel
    {
        public int user_id { get; set; }
        public int location_id { get; set; }
        public string photo_path { get; set; }
    }
}
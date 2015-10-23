using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WRO.Web.Models.ApiModels
{
    public class Resource
    {
        public int id { get; set; }
        public string path { get; set; }
        public int resource_type_id { get; set; }
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WroServer.Models.EventModels
{
    public class FromAndroidModel
    {
        public string UserName { get; set; }
        public decimal Longtitude { get; set; }
        public decimal Latitude { get; set; }
        public List<string> CategoriesList { get; set; } 
    }
}
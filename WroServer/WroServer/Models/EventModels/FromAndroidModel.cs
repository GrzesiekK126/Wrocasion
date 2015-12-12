using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WroServer.Models.EventModels
{
    public class FromAndroidModel
    {
        public string UserName { get; set; }
        public double Longtitude { get; set; }
        public double Latitude { get; set; }
        public List<string> Categories { get; set; }
    }
}
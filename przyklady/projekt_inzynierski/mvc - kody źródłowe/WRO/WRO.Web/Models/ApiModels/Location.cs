using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WRO.Web.Models.ApiModels
{
    public class Location
    {
        public int id { get; set; }
        public double latitude { get; set; }
        public double longitude { get; set; }
        public string name { get; set; }
        public bool is_found { get; set; }
    }
}
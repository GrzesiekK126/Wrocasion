using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WRO.Web.Models.ApiModels
{
    public class TaskContent
    {
        public int id { get; set; }
        public string name { get; set; }
        public int type_id { get; set; }
        public int description_id { get; set; }
        public int minimum_to_find { get; set; }
        public ICollection<Models.ApiModels.Location> locations { get; set; }
        public ICollection<Models.ApiModels.Resource> resources { get; set; }
    }
}
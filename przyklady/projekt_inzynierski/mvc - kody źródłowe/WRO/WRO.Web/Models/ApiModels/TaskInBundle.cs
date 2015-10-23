using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WRO.Web.Models.ApiModels
{
    public class TaskInBundle
    {
        public int id { get; set; }
        public string name { get; set; }
        public int type_id { get; set; }
       // public int description_id { get; set; }
        public bool is_solved { get; set; }

      /*  public ICollection<Location> locations { get; set; }
        public virtual ICollection<Resource> resources { get; set; }*/
    }
}
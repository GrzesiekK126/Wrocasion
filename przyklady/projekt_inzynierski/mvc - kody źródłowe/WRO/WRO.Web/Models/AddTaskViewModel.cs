using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WRO.Web.Models
{
    public class AddTaskViewModel
    {
        public string Name { get; set; }
        public int TypeId { get; set; }
        public int BundleId { get; set; }
    }
}
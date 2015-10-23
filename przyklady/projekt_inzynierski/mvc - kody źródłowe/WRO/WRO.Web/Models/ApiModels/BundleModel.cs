using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WRO.Web.Models.ApiModels
{
    public class BundleModel
    {
        public int id { get; set; }
        public string name { get; set; }

        public int solved_riddles { get; set; }
        public int all_riddles { get; set; }

    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WRO.Web.Models.ApiModels
{
    public class BundleContent
    {
        public int id { get; set; }
        public string name { get; set; }

       /* public int solved_riddles { get; set; }
        public int all_riddles { get; set; }*/

        public ICollection<TaskInBundle> tasks { get; set; }
    }
}
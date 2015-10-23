using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WRO.DAL;
namespace WRO.Web.Models
{
    public class BundlesModel
    {
        public ICollection<Bundle> bundles { get; set; }
        public IDictionary<int, int> number_of_tasks { get; set; }
    }
}
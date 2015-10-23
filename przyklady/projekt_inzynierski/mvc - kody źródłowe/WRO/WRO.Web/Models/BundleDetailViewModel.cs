using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WRO.DAL;
namespace WRO.Web.Models
{
    public class BundleDetailViewModel
    {
        public Bundle BundleObject { get; set; }
        public ICollection<TaskType> TaskTypes { get; set; }
    }
}
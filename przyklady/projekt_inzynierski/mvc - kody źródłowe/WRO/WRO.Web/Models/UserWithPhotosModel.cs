using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WRO.Web.Models
{
    public class UserWithPhotosModel
    {
        public string name { get; set; }
        public ICollection<string> photos { get; set; }
    }
}
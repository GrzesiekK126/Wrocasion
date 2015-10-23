using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WRO.Web.Models
{
    public class UserListModel
    {
        public ICollection<UserInListModel> users { get; set; }
    }
}
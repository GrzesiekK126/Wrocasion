using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WroServer.Models
{
    public class UserModels
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Password { get; set; }
        public string Email { get; set; }
    }
}
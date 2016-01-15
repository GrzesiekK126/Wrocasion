using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WroServer.Models.UserModel
{
    public class UserEventModel
    {
        public string Username { get; set; }
        public List<string> Events { get; set; }
        public bool TakingPart { get; set; }
        public int EventIdToTakingPart { get; set; }
    }
}
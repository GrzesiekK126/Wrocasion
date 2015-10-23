using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WRO.Web.Models
{
	public class UserInListModel
	{
        public string name { get; set; }
        public int solved_bundles { get; set; }
        public int solved_riddles { get; set; }
        public int found_locations { get; set; }
	}
}
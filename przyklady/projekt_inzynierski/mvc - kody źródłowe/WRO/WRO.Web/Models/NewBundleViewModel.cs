using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace WRO.Web.Models
{
    public class NewBundleViewModel
    {
        [Display(Name = "Name")]
        [Required]
        public string Name { get; set; }
    }
}
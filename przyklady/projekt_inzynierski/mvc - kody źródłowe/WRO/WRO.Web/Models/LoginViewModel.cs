using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace WRO.Web.Models
{
    public class LoginViewModel
    {
        [Required(ErrorMessage = "Wartość loginu nie może być pusta")]
        public string LoginVal { get; set; }
        [Required(ErrorMessage = "Wartość hasła nie może być pusta")]
        public string Password { get; set; }
        public bool RememberMe { get; set; }
    }
}
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;
using WRO.DAL;

namespace WRO.Web.Models
{
    public class TaskDetailViewModel
    {
        //ICollection<Bundle> bundles;
        public int BundleId { get; set; }
        public int TypeId { get; set; }
        public int Id { get; set; }
        [Required]
        public string Name { get; set; }

        [Range(0, int.MaxValue, ErrorMessage = "Please enter a value bigger than {1}")]
        public int MinimumToFound { get; set; }

    }
}
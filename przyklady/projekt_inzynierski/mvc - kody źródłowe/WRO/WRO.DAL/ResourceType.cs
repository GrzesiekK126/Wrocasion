//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace WRO.DAL
{
    using System;
    using System.Collections.Generic;
    
    public partial class ResourceType
    {
        public ResourceType()
        {
            this.Resource = new HashSet<Resource>();
        }
    
        public int id { get; set; }
        public string name { get; set; }
    
        public virtual ICollection<Resource> Resource { get; set; }
    }
}

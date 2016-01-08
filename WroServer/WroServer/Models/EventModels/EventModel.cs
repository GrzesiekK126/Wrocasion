using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.AccessControl;
using System.Web;

namespace WroServer.Models.EventModels
{
    public class EventModel
    {
        public int Id { get; set; }
        public string Nazwa { get; set; }
        public DateTime Data { get; set; }
        public string Street { get; set; }
        public string City { get; set; }
        public string ZipCode { get; set; }
        public decimal Price { get; set; }
        public string Image { get; set; }
        public int Operator { get; set; }
        public DateTime AddData { get; set; }
        public string Link { get; set; }
        public string Categories { get; set; }
        public int LocationId { get; set; }
        public decimal Longtitude { get; set; }
        public decimal Latitude { get; set; }
        public int TakingPart { get; set; } 
        public string Description { get; set; }  
        public string LocationName { get; set; }          
    }   
    public class EventsList
    {
        public List<EventModel> ListOfEventModels;
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WroServer.Models.WidokWydarzen
{
    public class LokacjaAjaxModel
    {
        public int Id { get; set; }
        public double Lat { get; set; }
        public double Lng { get; set; }

        public string Ulica;

        public string KodPocztowy;

        public string Miasto;
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WroServer.Models.WidokWydarzen
{
    public class LokacjaAjaxModel
    {
        public int Id { get; set; }
        public decimal Lat { get; set; }
        public decimal Lng { get; set; }

        public string Nazwa { get; set; }

        public string Ulica;

        public string KodPocztowy;

        public string Miasto;
    }
}
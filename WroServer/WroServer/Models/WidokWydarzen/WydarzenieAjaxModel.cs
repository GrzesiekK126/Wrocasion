using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WroServer.Models.WidokWydarzen
{
    public class WydarzenieAjaxModel
    {
        public int Id { get; set; }

        public string Nazwa { get; set; }
     
        public string Data { get; set; }

        public decimal Cena { get; set; }

        public LokacjaAjaxModel Lokacja { get; set; }

        public string NazwaOperatora { get; set; }

        public string DataDodania { get; set; }

        public string Opis { get; set; }

        public string Link { get; set; }

        public string Kategoria { get; set; }
        
        public List<string> LinkiDoObrazkow { get; set; }
    }
}
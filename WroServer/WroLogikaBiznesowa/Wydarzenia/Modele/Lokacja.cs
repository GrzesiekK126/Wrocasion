using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WroBL.Wydarzenia.Modele
{
    public class Lokacja
    {
        public int? Id { get; set; }
        public decimal Lat { get; set; }
        public decimal Lng { get; set; }

        private string name;

        public string Nazwa
        {
            get {
                return this.name;
            }
            set
            {
                if (value != null)
                {

                    this.name = (value.Length > 255) ? value.Substring(0, 255) : value;
                }
            }
        }

        private string ulica;
        public string Ulica//dodac nazwe 
        {
            get
            {
                return this.ulica;
            }
            set
            {
                if (value != null)
                {
                    
                    this.ulica = (value.Length>255)?value.Substring(0, 255):value;
                }
            }
        }

        private string kodPocztowy;
        public string KodPocztowy
        {
            get
            {
                return this.kodPocztowy;
            }
            set
            {
                if (value != null)
                {

                    this.kodPocztowy = (value.Length > 60) ? value.Substring(0, 60) : value;
                }
            }
        }

        private string miasto;
        public string Miasto
        {
            get
            {
                return this.miasto;
            }
            set
            {
                if (value != null)
                {

                    this.miasto = (value.Length > 60) ? value.Substring(0, 60) : value;
                }
            }
        }
    }
}

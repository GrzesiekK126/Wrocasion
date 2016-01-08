using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WroBL.Wydarzenia.Modele
{
    public class Wydarzenie
    {
        public int Id { get; set; }

        private string nazwa;
        public string Nazwa
        {
            get
            {
                return this.nazwa;
            }
            set
            {
                if (value != null)
                {

                    this.nazwa = (value.Length > 60) ? value.Substring(0, 60) : value;
                }
            }
        }

        public DateTime Data { get; set; }

        public decimal Cena { get; set; }

        public int IdLokacji { get; set; }

        public int IdOperatora { get; set; }

        //tymczasowo
        public int IdKategorii { get; set; }

        public List<string> ListaKategorii { get; set; }

        public Lokacja Lokalizacja { get; set;}

        public DateTime DataDodania { get; set; }

        private string opis;
        public string Opis
        {
            get
            {
                return this.opis;
            }
            set
            {
                if (value != null)
                {

                    this.opis = (value.Length > 8192) ? value.Substring(0, 8192) : value;
                }
            }
        }

        private string link;
        public string Link
        {
            get
            {
                return this.link;
            }
            set
            {
                if (value != null)
                {

                    this.link = (value.Length > 60) ? value.Substring(0, 60) : value;
                }
            }
        }

        public List<string> LinkiDoObrazkow { get; set; }
    }
}

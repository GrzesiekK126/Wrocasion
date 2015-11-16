using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WroServer.Models
{
    public class JednaKategoriaModel
    {
        public int Id { get; set; }
        public string Nazwa { get; set; }
        public string LinkDoObrazka { get; set; }
    }

    public class ListaKategoriiModel
    {
        public List<JednaKategoriaModel> ListaKategorii { get; set; }
    }
}
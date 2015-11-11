using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WroBL
{
    public static class ExampleService
    {
        public static string PrzywitajSie(string imie)
        {
            return string.Format("Witaj, {0}. :)", imie);
        }

        /*public static List<string> Testowy()
        {
            return WroBL.DAL.DatabaseUtils.FillCombobox("select password from Operator");
        }*/
    }
}

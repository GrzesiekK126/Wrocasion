using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WroBL
{
    public class Logowanie
    {
        public enum Rola { Guru, Admin, Janusz, Gosc };

        public static int NumerRoli(Rola rola)
        {
            switch (rola)
            {
                case Rola.Guru:
                    return 0;
                case Rola.Admin:
                    return 1;
                case Rola.Janusz:
                    return 2;
                default:
                    return 10;
            }
        }

        public static string NazwaRoli(Rola rola)
        {
            switch (rola)
            {
                case Rola.Guru:
                    return "Guru";
                case Rola.Admin:
                    return "Administrator";
                case Rola.Janusz:
                    return "Dodawacz";
                default:
                    return "Gość";
            }
        }
        /*
        public static Dictionary<Rola, string> PobierzNazwyRol()
        {
            return new Dictionary<Rola, string>(){
                {Rola.Admin,"Administrator"},
                {Rola.Guru,"Guru"},
                {Rola.Janusz,"Dodawacz"},
                {Rola.Gosc,"Gość"}
            };
        }*/

        //TODO dopisac obsluge z bazy
        public static Rola RolaUzytkownika(string nazwaUzytkownika)
        {
            if ("trolol".Equals(nazwaUzytkownika))
                return Rola.Guru;
            return Rola.Janusz;
        }

        //TODO dopisac obsluge z bazy
        public static bool UzytkownikIstnieje(string nazwaUzytkownika)
        {
            if("trolol".Equals(nazwaUzytkownika))
                return true;
            return false;
        }

        //TODO dopisac obsluge z bazy
        public static void ZaktualizujDateOstatniegoLogowania(string nazwaUzytkownika)
        {

        }

        public static bool Waliduj(string nazwaUzytkownika, string haslo)
        {
            if("trolol".Equals(nazwaUzytkownika) && "trolol".Equals(haslo))
                return true;
            return false;
        }
    }
}

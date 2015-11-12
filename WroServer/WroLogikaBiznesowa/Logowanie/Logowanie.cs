using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace WroBL
{
    public class Logowanie
    {
        public enum Rola { Guru, Admin, Janusz, Gosc };

        public static Rola RolaFromId(string numerRoli) {
            switch (numerRoli)
            {
                case "0":
                    return Rola.Guru;
                case "1":
                    return Rola.Admin;
                case "2":
                    return Rola.Janusz;
                default:
                    return Rola.Gosc;         

            }
        }

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
        //WAŻNE we wszystkich metodach jako nazwe użytkownika przyjąłem login, zawsze będzie można zmienić
        //TODO dopisac obsluge z bazyy !!DONE!!
        //TODO TESTY
        public static Rola RolaUzytkownika(string nazwaUzytkownika)
        {
            string rola=DAL.DatabaseUtils.GetOneElement("select o.role from operator o where o.login='" + nazwaUzytkownika+"'");
            return RolaFromId(rola);
        }

        //TODO dopisac obsluge z bazy

        public static bool UzytkownikIstnieje(string nazwaUzytkownika)
        {
           return DAL.DatabaseUtils.ExistsElement("select first 1 1 from operator where login='" + nazwaUzytkownika+"'");
        }

        //TODO dopisac obsluge z bazy !!DONE!! 
        //TODO ale slabo to nazwaleś hahahhahaaa

        public static void ZaktualizujDateOstatniegoLogowania(string nazwaUzytkownika,bool login)
        {
            
            string ID = DAL.DatabaseUtils.GetOneElement("select o.id from operator o where o.login='"+nazwaUzytkownika+"'");
            if (login)
            {
                //DAL.DatabaseUtils.DatabaseCommand("INSERT INTO HISTORY_LOGIN (OPERATOR,STATUS) VALUES ("+ID+", 0);");  
            }
            else if(!login)
            {
                //DAL.DatabaseUtils.DatabaseCommand("INSERT INTO HISTORY_LOGIN (OPERATOR,STATUS) VALUES (" + ID + ", 1);");
            }
        }

        

        public static bool Waliduj(string nazwaUzytkownika, string haslo)
        {
            //obsługa szyfrowania
            var rm = new RijndaelManaged();
            var pass = DAL.DatabaseUtils.GetOneElement("select o.password from operator o where o.login='"+nazwaUzytkownika+"'");
            var bytePassword = DAL.Crytography.GetBytes(pass);
            var password = DAL.Crytography.DecryptRijndaelManaged(bytePassword, rm.Key, rm.IV);
            //

            if (UzytkownikIstnieje(nazwaUzytkownika))
            {
                if ("trolol".Equals(nazwaUzytkownika) && password.Equals(haslo))
                    return true;
                return false;
            }
            else
                return false;
        }
    }
}

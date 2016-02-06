using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WroBL.DAL
{
    public static class ConStr
    {
        public static string ConnectionString 
        {
			//testowy komentarz
            get 
            {
                /*rafal*/    return @"character set=WIN1250;initial catalog=C:\Users\Rafał\Documents\Wrocasion\Baza\WROCASION_DB;user id=SYSDBA;password=masterkey;data source=localhost";
                /*Serwer*/ // return @"character set=WIN1250;initial catalog=C:\EventFinderDatabase\WROCASION_DB;user id=SYSDBA;password=masterkey;data source=localhost";
                /*maciek*/ //return @"character set=WIN1250;initial catalog=C:\Baza\WROCASION_DB;user id=SYSDBA;password=masterkey;data source=localhost";
               /*Grzesiu*/ //return @"character set=WIN1250;initial catalog=D:\Projekt_zespolowy\Baza\WROCASION_DB;user id=SYSDBA;password=masterkey;data source=localhost";
            }
        }

    }
}

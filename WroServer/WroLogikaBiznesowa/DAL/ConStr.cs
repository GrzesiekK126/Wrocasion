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
                /*Serwer*/ //return @"character set=WIN1250;initial catalog=C:\WrocasionDatabase\WROCASION_DB;user id=SYSDBA;password=masterkey;data source=localhost";
                /*rafal*/  //return @"character set=WIN1250;initial catalog=C:\Users\Lenovo\Documents\Wrocasion\Baza\WROCASION_DB;user id=SYSDBA;password=masterkey;data source=localhost";
               /*maciek*/ return @"character set=WIN1250;initial catalog=C:\Baza\WROCASION_DB;user id=SYSDBA;password=masterkey;data source=localhost";
            }
        }

    }
}

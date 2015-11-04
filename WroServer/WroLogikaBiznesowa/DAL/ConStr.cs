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
            get 
            {
                return @"character set=WIN1250;initial catalog=C:\Baza\WROCASION_DB;user id=SYSDBA;password=masterkey;data source=localhost";
            }
        }

    }
}

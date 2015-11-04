using FirebirdSql.Data.FirebirdClient;
using System;
using System.Collections.Generic;
using System.Data;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WroBL.DAL
{
    public static class DatabaseUtils
    {
          public static void DatabaseCommand(string command)
        {
            FbConnection con = new FbConnection(ConStr.ConnectionString);
            con.Open();
            FbTransaction transaction = con.BeginTransaction();
            FbCommand com = new FbCommand(command, con, transaction);
            com.ExecuteNonQuery();
            transaction.Commit();
            con.Close();
        }

        public static List<string> GetAllOperators()
        {
            using (FbConnection con = new FbConnection(ConStr.ConnectionString))
            {
                con.Open();


            }

            return null;
        }

        public static List<string> FillCombobox(string com)
        {
            var ds = new DataTable();
            FbConnection con = new FbConnection(ConStr.ConnectionString);
            con.Open();
            FbDataAdapter da = new FbDataAdapter(com, con);
            da.Fill(ds);

            var toReturn = new List<string>();

            foreach (DataRow row in ds.Rows)
            {
                foreach (DataColumn column in ds.Columns)
                {
                    toReturn.Add(row[column].ToString());
                }
            }
            return toReturn;
        }
    }
}

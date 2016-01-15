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
        public static string GetUserId(string username)
        {
            var userId =
                WroBL.DAL.DatabaseUtils.GetOneElement("SELECT U.ID FROM USERS U WHERE U.NAME = '" + username + "'; ");
            return userId;
        }
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

        //REMEMBER!!!! Type data and string are in '' f.e 'Adam', '2015-01-02'

        public static List<string> ListOfElementsFromDatabase(string com)
        {

            var ds = new DataTable();
            FbConnection con = new FbConnection(ConStr.ConnectionString);
            con.Open();
            FbDataAdapter da = new FbDataAdapter(com, con);
            da.Fill(ds);
            con.Close();
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

        public static string GetOneElement(string command)
        {
            string element = "";
            FbConnection con = new FbConnection(ConStr.ConnectionString);
            con.Open();
            FbTransaction transaction = con.BeginTransaction();
            FbCommand com = new FbCommand(command, con, transaction);
            com.ExecuteNonQuery();
            FbDataReader datareader;
            datareader = com.ExecuteReader();
            while (datareader.Read())
            {
                element = datareader[0].ToString();
            }
            con.Close();
            return element;

        }

        public static bool ExistsElement(string command)
        {
            string element = "";
            FbConnection con = new FbConnection(ConStr.ConnectionString);
            con.Open();
            FbTransaction transaction = con.BeginTransaction();
            FbCommand com = new FbCommand(command, con, transaction);
            com.ExecuteNonQuery();
            FbDataReader datareader;
            datareader = com.ExecuteReader();
            while (datareader.Read())
            {
                element = datareader[0].ToString();
            }
            con.Close();
            if (element != null && element != "")
                return true;
            return false;
        }

        public static DataTable EleentsToDataTable(string command)
        {
            var dt = new DataTable();
            FbConnection con = new FbConnection(ConStr.ConnectionString);
            con.Open();
            FbDataAdapter da = new FbDataAdapter(command, con);
            da.Fill(dt);
            con.Close();
            return dt;
        }
    }
}

using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace WroBL.DAL
{
    /**************************
        EXAMPLE OF USE
                    using (var rm = new RijndaelManaged())
            {
                byte[] encrypted = EncryptRijndaelManaged("Ala ma kota", rm.Key, rm.IV);
                string decrypted = DecryptRijndaelManaged(encrypted, rm.Key, rm.IV);
                Console.WriteLine(decrypted);
            }

    Metody GetByte i GetString służą tylko i wyłącznie do zapisu danych do bazy
    ***************************/
    public static class Crytography
    {

        public static byte[] ElChupacabra = new byte[]
        {
            0xB6, 0xF1, 0x94, 0x58, 0xDE, 0xA7, 0x93, 0x16, 0x9D, 0xC7, 0x46, 0xE2, 0x12, 0x30, 0x93, 0x79, 0x42, 0x87, 0x61, 0xF4, 0xDB, 0x68, 0xF5, 0x38, 0xEC, 0xBA, 0x7D, 0x74, 0x84, 0x86, 0x7D, 0xEF

        };

        public static byte[] ElMariachi = new byte[]
        {
            0xB9, 0xC, 0xA9, 0x96, 0xB1, 0xC9, 0xC5, 0x7E, 0x27, 0xD6, 0x8, 0x3F, 0xB5, 0xA4, 0xD5, 0xA5
        };

        //From string to byte
        public static byte[] GetBytes(string str)
        {
            return Convert.FromBase64String(str);
        }

        //From string to byte[]
        public static string GetString(byte[] bytes)
        {
            return Convert.ToBase64String(bytes);
        }

        //szyfrowanie
        public static byte[] EncryptRijndaelManaged(string input, byte[] key, byte[] IV)
        {
            byte[] encrypted;
            using (var rm = new RijndaelManaged())
            {
                rm.Key = key;
                rm.IV = IV;
                ICryptoTransform encryptor = rm.CreateEncryptor(rm.Key, rm.IV);
                using (var ms = new MemoryStream())
                {
                    using (var cs = new CryptoStream(ms, encryptor, CryptoStreamMode.Write))
                    {
                        using (var swEncrypt = new StreamWriter(cs))
                        {
                            swEncrypt.Write(input);
                        }
                        encrypted = ms.ToArray();
                    }
                }
            }

            return encrypted;
        }

        //Deszyfrowanie
        public static string DecryptRijndaelManaged(byte[] input, byte[] key, byte[] IV)
        {
            string decrypted ="";
           


            using (var rm = new RijndaelManaged())
            {
                rm.Key = key;
                rm.IV = IV;
                ICryptoTransform decryptor = rm.CreateDecryptor(rm.Key, rm.IV);
                using (var ms = new MemoryStream(input))
                {
                    using (var cs = new CryptoStream(ms, decryptor, CryptoStreamMode.Read))
                    {
                        using (var swDecrypt = new StreamReader(cs))
                        {
                            decrypted = swDecrypt.ReadToEnd();
                        }
                    }
                }
            }

            return decrypted;
        }
    }
}

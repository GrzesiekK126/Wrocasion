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
        //From string to byte
        public static byte[] GetBytes(string str)
        {
            byte[] bytes = new byte[str.Length * sizeof(char)];
            System.Buffer.BlockCopy(str.ToCharArray(), 0, bytes, 0, bytes.Length);
            return bytes;
        }

        //From string to byte[]
        public static string GetString(byte[] bytes)
        {
            char[] chars = new char[bytes.Length / sizeof(char)];
            System.Buffer.BlockCopy(bytes, 0, chars, 0, bytes.Length);
            return new string(chars);
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
            string decrypted;
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

using System;
using System.Text;
using System.Collections.Generic;
using System.Security.Cryptography;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace WroServerUnitTest.LoginTest
{
    /// <summary>
    /// Summary description for Crypted
    /// </summary>
    [TestClass]
    public class Crypted
    {
        [TestMethod]
        public void TestMethod1()
        {
            var input = new byte[]
            {
                0x03, 0x45, 0x31, 0x35, 0x42
            };
            
            CollectionAssert.AreEqual(input,
                WroBL.DAL.Crytography.GetBytes(WroBL.DAL.Crytography.GetString(input)));

            }

        [TestMethod]
        public void TestMethodCrypt()
        {
            var admin = "admin";
            var rm = new RijndaelManaged();
            var kk=WroBL.DAL.Crytography.EncryptRijndaelManaged(admin, WroBL.DAL.Crytography.ElChupacabra, rm.IV);
            var kk1=WroBL.DAL.Crytography.GetString(kk);
            var rm1 = new RijndaelManaged();
            var kkdupa=WroBL.DAL.Crytography.GetBytes(kk1);
            var ll=WroBL.DAL.Crytography.DecryptRijndaelManaged(kkdupa, WroBL.DAL.Crytography.ElChupacabra, rm1.IV);
            Assert.AreEqual(ll,admin);

        }


    }
}

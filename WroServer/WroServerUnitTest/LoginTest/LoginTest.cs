using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace WroServerUnitTest.LoginTest
{
    [TestClass]
    public class LoginTest
    {
        [TestMethod]
        public void Role()
        {
            var aa=WroBL.Logowanie.RolaUzytkownika("login");
            Assert.AreEqual(WroBL.Logowanie.Rola.Admin, aa);
        }
        [TestMethod]
        public void ExistOperator()
        {
            var a = WroBL.Logowanie.UzytkownikIstnieje("miranda");
            Assert.AreEqual(true, a);
        }
    }
}

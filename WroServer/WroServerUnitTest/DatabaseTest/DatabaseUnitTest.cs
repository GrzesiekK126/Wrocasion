using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace WroServerUnitTest.DatabaseTest
{
    [TestClass]
    public class DatabaseUnitTest
    {
        [TestMethod]
        public void UnitTestMethod()
        {
            var UserName = "asd";
            Assert.AreEqual(WroBL.DAL.DatabaseUtils.ExistsElement("select first 1 1 from users u where u.name='" + UserName + "'"), false);
        }
    }
}

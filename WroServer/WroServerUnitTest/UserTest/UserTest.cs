using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace WroServerUnitTest.UserTest
{
    [TestClass]
    public class UserTest
    {
        [TestMethod]
        public void PasswordCompare()
        {
            Assert.AreEqual(true,WroBL.UserLogin.ComparePassword("abcd","Rafał"));
        }
    }

}
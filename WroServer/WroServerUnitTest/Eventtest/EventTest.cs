using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace WroServerUnitTest.Eventtest
{
    [TestClass]
    public class EventTest
    {
        [TestMethod]
        public void EventTestMethod1()
        {
            Assert.IsNotNull(WroBL.Wydarzenia.WydarzeniaService.PobierzWydarzenia(10, 0));
        }
    }
}

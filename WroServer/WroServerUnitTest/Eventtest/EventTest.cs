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

        [TestMethod]
        public void WydarzeniaServiceTest()
        {
            WroBL.Wydarzenia.Modele.Lokacja lokacja = new WroBL.Wydarzenia.Modele.Lokacja
            {
                Nazwa = "Maciektesttest",
                Lat=1.1m,
                Lng=3.3m,
                Ulica="Sezamkowa 1",
                Miasto="Muminkowo",
                KodPocztowy="55-555"
                
            };

            int i;
            string message;
            Assert.IsNotNull(WroBL.Wydarzenia.WydarzeniaService.DodajLubEdytuj(lokacja,out i,out message));
        }
    }
}

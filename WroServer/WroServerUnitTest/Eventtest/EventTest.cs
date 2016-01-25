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
        public void LokacjaWydarzeniaServiceTest()
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

        [TestMethod]
        public void NearbyLocations()
        {
            Assert.IsNotNull(WroBL.Wydarzenia.WydarzeniaService.PobierzBliskieLokacje(51.1059861m, 17.0480885m));
        }

        [TestMethod]
        public void EditLocations()
        {
            int i = 0;
            string msg = "";
            var model = new WroBL.Wydarzenia.Modele.Wydarzenie
            {
                Cena = 10,
                Nazwa = "Testowe zmienianie nazwy",
                Opis = "Jednak tutaj bedzie inny opis",
                Link = "www.wp.pl",
                IdLokacji = 1,
                Data =DateTime.Now,
                IdKategorii = 1,
                IdOperatora = 7,
                

            };
            Assert.IsTrue(WroBL.Wydarzenia.WydarzeniaService.DodajLubEdytuj(model,out i,out msg));
        }
    }
    
}

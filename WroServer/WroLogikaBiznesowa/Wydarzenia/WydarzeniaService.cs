using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;

namespace WroBL.Wydarzenia
{
    public static class WydarzeniaService
    {
        /// <summary>
        /// (Tutaj na pewno trzeba będzie dodać jeszcze obsługę filtrów, ale jeszcze nie myślałem jak to najlepiej zrobić)
        /// </summary>
        /// <param name="cnt"></param>
        /// <param name="offset"></param>
        /// <returns></returns>
        /// 
        private static List<string> ImgList(string listFromDatabase)
        {

            string[] tabOfStrings = listFromDatabase.Split(new string[] { "/files" }, StringSplitOptions.None);
            List<string> result = new List<string>();

            foreach (var item in tabOfStrings)
            {
                result.Add("/files"+item);
            }

            return result;
        }

        private static List<string> CategoriesList(string listFromDatabase)
        {

            string[] tabOfStrings = listFromDatabase.Split(new string[] { "," }, StringSplitOptions.None);
            List<string> result = new List<string>();

            foreach (var item in tabOfStrings)
            {
                result.Add(item);
            }

            return result;
        }

        public static List<Modele.Wydarzenie> PobierzWydarzenia(int cnt, int offset, string categoryList="null", string name = "null",
                                                                Nullable<DateTime> fromDate = null, Nullable<DateTime> toDate = null )
        {
            var _catList = categoryList == "null" ? "null" : ("'" + categoryList + "'");
            var _fromDate = fromDate == null ? "null" : ("'" + fromDate + "'");
            var _toDate = toDate == null ? "null" : ("'" + toDate + "'");
            var _name = name == "null" ? "null" : ("'" + name + "'");
            var wyarzeniaDataTable =
                DAL.DatabaseUtils.EleentsToDataTable(
                    "select e.id, e.nazwa, e.data, e.price, e.locationid, e.outlongtitude, e.outlatitude," +
                           "e.street, e.zipcode, e.city, e.image, e.operator, e.adddata, e.categoriesout," +
                           "e.link, e.description, e.locationname " +
                        "from event_select(" + _fromDate + ", " + _toDate + "," +
                                            _catList+ "," + _name + "," +cnt+", "+offset+") e")
                 //from EVENT_SELECT(<OdDaty>,<DoDaty>,<ListaKategorii>,<Nazwa>,<IlośćWydarzeń>,<Offset>)
                    .AsEnumerable();
            List<Modele.Wydarzenie> listaWydarzeń= new List<Modele.Wydarzenie>();
            listaWydarzeń = (from item in wyarzeniaDataTable
                             select new Modele.Wydarzenie
                             {
                                 Id = item.Field<int>("id"),
                                 Nazwa = item.Field<string>("nazwa"),
                                 Data = item.Field<DateTime>("data"),
                                 Cena = item.Field<decimal>("price"),
                                 IdLokacji = item.Field<int>("locationid"),
                                 Lokalizacja = new Modele.Lokacja() {
                                     Id=item.Field<int>("locationid"),
                                     KodPocztowy = item.Field<string>("zipcode"),
                                     Miasto = item.Field<string>("city"),
                                     Nazwa = item.Field<string>("locationname"),
                                     Ulica = item.Field<string>("street"),
                                     Lat = item.Field<decimal>("outlatitude"),
                                     Lng = item.Field<decimal>("outlongtitude")                                     
                                 },
                                 LinkiDoObrazkow = ImgList(item.Field<string>("image")),
                                 IdOperatora = item.Field<int>("operator"),
                                 DataDodania = item.Field<DateTime>("adddata"),
                                 Link = item.Field<string>("link"),
                                 Opis = item.Field<string>("description"),
                                 ListaKategorii = CategoriesList(item.Field<string>("categoriesout"))
                             }).ToList();
            return listaWydarzeń;
        }

        /// <summary>
        /// Dla zadanych szerokości i wysokości geograficznej wyszukuje w bazie i zwraca lokacje, które znajdują się w pobliżu.
        /// (Może dla testów na razie niech zwraca po prostu wszystkie lokacje?)
        /// </summary>
        /// <param name="lat"></param>
        /// <param name="lng"></param>
        /// <returns></returns>
        public static List<Modele.Lokacja> PobierzBliskieLokacje(int lat, int lng)
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// Dodaje lokację do bazy.
        /// 
        /// </summary>
        /// <param name="lokacja">model lokacji do dodania</param>
        /// <param name="id">jesli się powiodło - tutaj zostanie zwrócony ID dodanej lokacji</param>
        /// <param name="wiadomosc">W razie niepowodzenia - tutaj zostanie zwrócona informacja o tym, co poszło nie tak</param>
        /// <returns>true jeśli się powiodło, false jeśli nie</returns>
        public static bool Dodaj(Modele.Lokacja lokacja, out int id, out string wiadomosc)
        {
            id = -1;
            wiadomosc = "Ta metoda nie jest jeszcze zaimplementowana.";

            return false;
        }

        /// <summary>
        /// Dodaje wydarzenie do bazy
        /// </summary>
        /// <param name="wydarzenie">model wydarzenia, które ma zostać dodane</param>
        /// <param name="id">jesli się powiodło - tutaj zostanie zwrócony ID dodanego wydarzenia</param>
        /// <param name="wiadomosc">W razie niepowodzenia - tutaj zostanie zwrócona informacja o tym, co poszło nie tak</param>
        /// <returns>true jeśli się powiodło, false jeśli nie</returns>
        public static bool Dodaj(Modele.Wydarzenie wydarzenie, out int id, out string wiadomosc)
        {
            id = -1;
            wiadomosc = "Ta metoda nie jest jeszcze zaimplementowana.";

            return false;
        }

        /// <summary>
        /// Zwraca model lokacji o zadanym ID.
        /// </summary>
        /// <param name="id"></param>
        /// <returns>Obiekt lokacji, lub null jeśli lokacji o takim ID nie ma w bazie</returns>
        public static Modele.Lokacja PobierzLokacje(int id)
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// Pobiera z bazy i zwraca nazwę operatora o zadanym ID.
        /// Jeśli operatora nie ma w bazie, zwraca pustego stringa.
        /// </summary>
        /// <param name="id">ID operatora, którego nazwa ma zostać pobrana</param>
        /// <returns>nazwa operatora lub pusty string</returns>
        public static string NazwaOperatora(int id)
        {
            return string.Empty;
        }

        /// <summary>
        /// Pobiera z bazy i zwraca nazwę kategorii o podanym ID.
        /// Jeśli podanej kategorii nie ma w bazie, zwraca pustego stringa.
        /// </summary>
        /// <param name="id">ID kategorii</param>
        /// <returns>nazwa kategorii lub pusty string</returns>
        public static string NazwaKategorii(int id)
        {
            return string.Empty;
        }
    }
}

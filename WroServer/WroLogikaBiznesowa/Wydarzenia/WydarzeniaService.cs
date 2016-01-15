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
        public static List<Modele.Wydarzenie> PobierzWydarzenia(int cnt, int offset)
        {
            return new List<Modele.Wydarzenie>(){
                new Modele.Wydarzenie(){
                    Id = 0,
                    Nazwa = "Konferencja Projektów Zespołowych",
                    IdLokacji = 3,
                    IdOperatora = 3,
                    Data = new DateTime(2015,11,15,22,34,45),
                    IdKategorii = 1,
                    Link = "http://pwr.edu.pl",
                    LinkiDoObrazkow = new List<string>(){
                        "files/ar.jpg"
                    },
                    Cena = 15.50m,
                    Opis = "W konferencji wezmą udział wszystkie grupy zapisane na kurs Projekt Zespołowy.\t\nKażda z grup zaprezentuje wyniki swojej pracy.\r\nWśród uczestników zostaną rozlosowane nagrody."
                },
                new Modele.Wydarzenie(){
                    Id = 2,
                    Nazwa = "Spotkanie z twórcami serwisu JakDojade.pl",
                    IdLokacji = 2,
                    IdOperatora = 5,
                    IdKategorii = 2,
                    LinkiDoObrazkow = new List<string>(){
                        "files/aw.jpg",
                        "files/aq.jpg",
                        "files/ae.jpg"
                    },
                    Link = "http://jakdojade.pl",
                    Data = new DateTime(2014,1,2,13,10,5)
                },
                new Modele.Wydarzenie(){
                    Id = 1,
                    Nazwa = "Koduj z Tedem - prelekcja",
                    IdLokacji = 8,
                    IdOperatora = 2,
                    IdKategorii = 1,
                    Link = "http://wp.pl",
                    Data = new DateTime(2011,10,11,9,45,45)
                }
            };
            //throw new NotImplementedException();
        }
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
        public static bool DodajLubEdytuj(Modele.Wydarzenie wydarzenie, out int id, out string wiadomosc)
        {
            /*id = -1;
            wiadomosc = "Ta metoda nie jest jeszcze zaimplementowana.";

            return false;*/

            id = wydarzenie.Id==null?0:wydarzenie.Id;
            wiadomosc = "Edycja poprawna.";
            return true;
        }

        /// <summary>
        /// Zwraca model lokacji o zadanym ID.
        /// </summary>
        /// <param name="id"></param>
        /// <returns>Obiekt lokacji, lub null jeśli lokacji o takim ID nie ma w bazie</returns>
        public static Modele.Lokacja PobierzLokacje(int id)
        {
            switch (id)
            {
                case 2:
                    return new Modele.Lokacja()
                    {
                        Id = 2,
                        KodPocztowy = "22-333",
                        Lat = 23442,
                        Lng = 3243,
                        Miasto = "Wrocław",
                        Ulica = "Hala Stulecia"
                    };
                case 3:
                    return new Modele.Lokacja()
                    {
                        Id = 3,
                        KodPocztowy = "33-333",
                        Lat = 734423,
                        Lng = 424333,
                        Miasto = "Wrocław",
                        Ulica = "Aula Główka PWr"
                    };
                default:
                    return new Modele.Lokacja()
                    {
                        Id = 8,
                        KodPocztowy = "88-333",
                        Lat = 8344223,
                        Lng = 824333,
                        Miasto = "Wrocław",
                        Ulica = "Sala 31, bud. C-3 PWr"
                    };
            }
        }

        /// <summary>
        /// Pobiera z bazy i zwraca nazwę operatora o zadanym ID.
        /// Jeśli operatora nie ma w bazie, zwraca pustego stringa.
        /// </summary>
        /// <param name="id">ID operatora, którego nazwa ma zostać pobrana</param>
        /// <returns>nazwa operatora lub pusty string</returns>
        public static string NazwaOperatora(int id)
        {
            return "Operator domyślny";
            //return string.Empty;
        }

        /// <summary>
        /// Pobiera z bazy i zwraca nazwę kategorii o podanym ID.
        /// Jeśli podanej kategorii nie ma w bazie, zwraca pustego stringa.
        /// </summary>
        /// <param name="id">ID kategorii</param>
        /// <returns>nazwa kategorii lub pusty string</returns>
        public static string NazwaKategorii(int id)
        {
            switch (id)
            {
                case 1:
                    return "Teatr";
                case 2:
                    return "Sztuka nowoczesna";
                default:
                    return "Koncerty";
            }
           // return string.Empty;
        }

        public static int IdKategorii(string nazwa)
        {
            switch (nazwa)
            {
                case "Teatr":
                    return 1;
                case "Sztuka nowoczesna":
                    return 2;
                default:
                    return 0;
            }
            throw new NotImplementedException();
        }

        public static int IdOperatora(string nazwa)
        {
            return 0;
        }
    }
}

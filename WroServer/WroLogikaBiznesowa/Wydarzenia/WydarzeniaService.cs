using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

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
            /*return new List<Modele.Wydarzenie>(){
                new Modele.Wydarzenie(){
                    Id = 0,
                    Nazwa = "wydarzenie",
                    IdLokacji = 3,
                    IdOperatora = 3
                },
                new Modele.Wydarzenie(){
                    Id = 2,
                    Nazwa = "konfa u borza",
                    IdLokacji = 2,
                    IdOperatora = 5
                },
                new Modele.Wydarzenie(){
                    Id = 1,
                    Nazwa = "koks tedego",
                    IdLokacji = 8,
                    IdOperatora = 2
                }
            };*/
            throw new NotImplementedException();
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

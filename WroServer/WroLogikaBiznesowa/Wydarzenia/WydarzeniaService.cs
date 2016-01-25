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
        public static List<Modele.Wydarzenie> PobierzWydarzenia2(int cnt, int offset)
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
            if (listFromDatabase == null)
                return new List<string>();

            string[] tabOfStrings = listFromDatabase.Split(new string[] { "/files" }, StringSplitOptions.None);
            List<string> result = new List<string>();

            foreach (var item in tabOfStrings)
            {
                if(!string.IsNullOrWhiteSpace(item))
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
                                                                DateTime? fromDate = null, DateTime? toDate = null )
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

        /// <summary>
        /// Dodaje lokację do bazy.
        /// 
        /// </summary>
        /// <param name="lokacja">model lokacji do dodania</param>
        /// <param name="id">jesli się powiodło - tutaj zostanie zwrócony ID dodanej lokacji</param>
        /// <param name="wiadomosc">W razie niepowodzenia - tutaj zostanie zwrócona informacja o tym, co poszło nie tak</param>
        /// <returns>true jeśli się powiodło, false jeśli nie</returns>
        public static bool DodajLubEdytuj(Modele.Lokacja lokacja, out int id, out string wiadomosc)
        {
            id = -1;
            wiadomosc = "Ta metoda nie jest jeszcze zaimplementowana.";
            if (lokacja == null)
            {
                wiadomosc = "Obiekt lokacja nie jest zdefiniowany";
                return false;
            }
            else
            {
            //edycja
            if (lokacja.Id != null)
            {
                if (DAL.DatabaseUtils.ExistsElement("SELECT FIRST 1 1 FROM LOCATION L WHERE L.ID = '" + lokacja.Id + "';") 
                    && !DAL.DatabaseUtils.ExistsElement("SELECT FIRST 1 1 FROM LOCATION L WHERE L.NAZWA = '" + lokacja.Nazwa + "';"))
                {
                    //edycja
                    DAL.DatabaseUtils.DatabaseCommand(
                        string.Format("UPDATE LOCATION SET NAZWA = '{0}', LONGITUDE = {1}, LATITUDE = {2}, STREET = '{3}', ZIP_CODE = '{4}', CITY = '{5}' WHERE(ID = {6})",
                        lokacja.Nazwa,
                        lokacja.Lng.ToString(System.Globalization.CultureInfo.InvariantCulture),
                        lokacja.Lat.ToString(System.Globalization.CultureInfo.InvariantCulture),
                        lokacja.Ulica??"",
                        lokacja.KodPocztowy??"",
                        lokacja.Miasto??"",
                        lokacja.Id
                        ));
                    return true;
                }
            }
            else
                return false;
            

            if (!DAL.DatabaseUtils.ExistsElement("SELECT FIRST 1 1 FROM LOCATION L WHERE L.NAZWA = '" + lokacja.Nazwa + "';"))
            {
                DAL.DatabaseUtils.DatabaseCommand("INSERT INTO LOCATION(NAZWA, LONGITUDE, LATITUDE, STREET, ZIP_CODE, CITY)"+
                    " VALUES('" 
                    + lokacja.Nazwa + "', " 
                    + lokacja.Lng.ToString(System.Globalization.CultureInfo.InvariantCulture) + ", " 
                    + lokacja.Lat.ToString(System.Globalization.CultureInfo.InvariantCulture) + ", '"  
                    + lokacja.Ulica + "', '"  
                    + lokacja.KodPocztowy + "', '"  
                    + lokacja.Miasto + "'); ");

                var success = Int32.TryParse(DAL.DatabaseUtils.GetOneElement("SELECT L.ID FROM LOCATION L WHERE L.NAZWA = '" + lokacja.Nazwa + "'"), out id);
                wiadomosc = "OK";
                return true;
            }
            else {
                wiadomosc = "Lokacja z taką nazwą już istnieje";
                return false;
            }
            }
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
            id = -1;
            if ( wydarzenie == null)
            {
                wiadomosc = "Obiekt wydarzenia jest pusty.";
                return false;
            }

            //dodawanie lokacji
            int idLokacji;
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //Maciusiu, ttutaj został ten if wyrzucony, bo edytujemyt wydarzenie, a nie edytujemy lokacji, to ten if sie wysypuje i rzuca falsem i kończy działanie metody
            //if (DodajLubEdytuj(wydarzenie.Lokalizacja, out idLokacji, out wiadomosc) == false)
            //    return false;
            DodajLubEdytuj(wydarzenie.Lokalizacja, out idLokacji, out wiadomosc);


            if ( wydarzenie.Id == null)
            {
                var dataDodania = DateTime.Now;

                //dodawanie wydarzenia
                DAL.DatabaseUtils.DatabaseCommand("INSERT INTO EVENT(NAME, DATE, PRICE, LOCATION, DESCRIPTION, OPERATOR, ADD_DATE, LINK)" +
                    " VALUES('"
                    + wydarzenie.Nazwa + "', "
                    + wydarzenie.Data + ", "
                    + wydarzenie.Cena.ToString(System.Globalization.CultureInfo.InvariantCulture) + ", '"
                    + idLokacji + "', '"
                    + wydarzenie.Opis + "', '"
                    + wydarzenie.IdOperatora + "', '"
                    + dataDodania + "', '"
                    + wydarzenie.Link + "'); ");

                if(! Int32.TryParse(DAL.DatabaseUtils.GetOneElement("SELECT L.ID FROM EVENT L WHERE L.NAME = '" + wydarzenie.Nazwa + "' AND L.ADD_DATE = '"+ dataDodania + "'"), out id))
                {
                    wiadomosc = "Problem z dodaniem wydarzenia.";
                    return false;
                }
                wiadomosc = "OK";
           

                
            }
            else
            {
                //Ten update tak dziwnie wygląda, bo nie może być tak, że: LOCATION=,DESCRIPTION....[PUSTE PO LOCATION],
                //ale update działa poprawnie, ewentualnie można ejszcze bardzoej potestować.
                //Add date bedzie działało, że to jets data ostatniej edycji, bo inaczej jest ciężko tego update napisać,

                DAL.DatabaseUtils.DatabaseCommand(string.Format("UPDATE \"EVENT\" SET {0} {1}"
                                                               +"{2} {3} {4}"
                                                               +"{5} {6} ADD_DATE='"+DateTime.Now+"' WHERE (ID = {7});",
                                                               !String.IsNullOrEmpty(wydarzenie.Nazwa)? "NAME = '"+wydarzenie.Nazwa+"',":"",
                                                               wydarzenie.Data.ToString()!= "01.01.0001 00:00:00" ? "\"DATE\" = '"+wydarzenie.Data+"',":"",
                                                               wydarzenie.Cena!=0?"PRICE = "+wydarzenie.Cena+",":"",
                                                               wydarzenie.IdLokacji!=null?"LOCATION = "+wydarzenie.IdLokacji+",":"",
                                                               !String.IsNullOrEmpty(wydarzenie.Opis)? "DESCRIPTION = '"+wydarzenie.Opis+"',":"",
                                                               wydarzenie.IdOperatora!=0? "OPERATOR ="+wydarzenie.IdOperatora+" ,":"",
                                                               !String.IsNullOrEmpty(wydarzenie.Link)? "LINK = '"+wydarzenie.Link+"',":"",
                                                               wydarzenie.Id));
                Int32.TryParse(wydarzenie.Id.ToString(), out id);

            }
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //delikatnie poprawiłem funkcję dodawania obrazków tak, że może byc pusta lista,
            //można sprawdzić
            //dodawanie obrazkow
            if (id != -1) {
                if (! Dodaj(wydarzenie.LinkiDoObrazkow, id, out wiadomosc))
                {
                    wiadomosc = "Wydarzenie zostało dodane, ale wystąpił problem z dodaniem obrazów.";
                    return false;
                }
                return true;
            }
            return true;
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

        public static int IdKategorii(string nazwa)
        {
            switch (nazwa)
            {
                case "Teatr":
                    return 1;
                case "Sztuka nowoczesna":
                    return 2;
                default:
                    return 5;
            }
            throw new NotImplementedException();
        }

        public static int IdOperatora(string nazwa)
        {
            return 0;
        }

        public static bool Dodaj(List<string> linkiDoObrazkow, int idWydarzenia, out string wiadomosc)
        {
            if (linkiDoObrazkow != null)
            {
                foreach (var item in linkiDoObrazkow)
                {
                    if (
                        DAL.DatabaseUtils.ExistsElement("SELECT FIRST 1 1 FROM IMAGES WHERE EVENT = '" + idWydarzenia +
                                                        "' AND LINK = '" + item + "'"))
                        continue;

                    DAL.DatabaseUtils.DatabaseCommand("INSERT INTO IMAGES(EVENT, LINK)" +
                                                      " VALUES('"
                                                      + idWydarzenia + "', "
                                                      + item + "'); ");
                }

                wiadomosc = "OK";
                return true;
            }
            else
            {
                wiadomosc = "Brak elementów w liście obrazków";
                return false;
            }
        }

        public static bool UsunWydarzenie(Modele.Wydarzenie wydarzenie, out int id) {
            id = 0;
            if (DAL.DatabaseUtils.ExistsElement("SELECT FIRST 1 1 FROM EVENT E WHERE E.ID="+wydarzenie.Id)) {
                DAL.DatabaseUtils.DatabaseCommand("DELETE FROM EVENT WHERE ID=" + wydarzenie.Id + ";");
                Int32.TryParse(wydarzenie.Id.ToString(), out id);
                return true;
            }
            return false;
        }
        
        public static List<Modele.Lokacja> PobierzBliskieLokacje(decimal lng, decimal lat)
        {
            //lokacja.Lng.ToString(System.Globalization.CultureInfo.InvariantCulture),
            //lokacja.Lat.ToString(System.Globalization.CultureInfo.InvariantCulture),
            var lokalizacjeDataTable = DAL.DatabaseUtils.EleentsToDataTable(string.Format("SELECT N.IDLOC, N.NAME, N.LONGTITUDE, N.LATITUDE, N.STREET, N.ZIPCODE, N.CITY FROM NEARBY_LOCATIONS({0}, {1}) N",
                                                                                          string.IsNullOrEmpty(lng.ToString())?"null":lng.ToString(System.Globalization.CultureInfo.InvariantCulture),
                                                                                          string.IsNullOrEmpty(lat.ToString()) ? "null" : lat.ToString(System.Globalization.CultureInfo.InvariantCulture))).AsEnumerable();
            var lokalizacje = new List<Modele.Lokacja>();

            lokalizacje = (from item in lokalizacjeDataTable
                           select new Modele.Lokacja
                           {
                               Id=item.Field<int>("IDLOC"),
                               Nazwa=item.Field<string>("NAME"),
                               Lng=item.Field<decimal>("LONGTITUDE"),
                               Lat = item.Field<decimal>("LATITUDE"),
                               Ulica = item.Field<string>("STREET"),
                               Miasto = item.Field<string>("CITY"),
                               KodPocztowy = item.Field<string>("ZIPCODE")
                               
                           }).ToList();
            return lokalizacje;
        }
    }
}

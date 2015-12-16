using System.Collections.Generic;
using System.Runtime.Remoting.Messaging;

namespace WroServer.Models
{
    public class OperatorModel
    {
        public int Id { get; set; } 
        public string Login { get; set; }   
        public string Password { get; set; } 
        public string Name { get; set; } 
        public string Surname { get; set; } 
        public string Contact { get; set; } 
        public int ContactForm { get; set; } 
        public int Role { get; set; } 
    }

    public class OperatorList
    {
        public List<OperatorModel> listOfOperators;
    }
}
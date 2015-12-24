using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web;

namespace WroServer.Controllers.Operators
{
    public static class OperatorValidate
    {
        
        private static bool ValidateLogin(string login)
        {
            if (login.Length > 2 &&
                !WroBL.DAL.DatabaseUtils.ExistsElement("select first 1 1 from operator o where o.name = '" + login + "'"))
                return true;
            return false;
        }

        private static bool ValidateName(string name)
        {
             var isValid = Regex.IsMatch(name, @"^[a-zA-Z]+$");
            if (name.Length > 3 &&
                isValid)
                return true;
            return false;
        }

        private static bool ValidateSurname(string surname)
        {
            var isValid = Regex.IsMatch(surname, @"^[a-zA-Z]+$");
            if (surname.Length > 3 &&
                isValid)
                return true;
            return false;
        }

        private static bool ValidatePassword(string password)
        {
            var isValid = Regex.IsMatch(password, @"^[a-zA-Z0-9\@\$\&\*\(\)]+$");
            
            if (password.Length>6 && isValid)
                return true;
            return false;
        }

        private static bool ValidateContact(string contact)
        {
            if (!string.IsNullOrWhiteSpace(contact))
                return true;
            return false;
        }

        public static bool ValidateOperator(Models.OperatorModel newOperator)
        {
            var login = ValidateLogin(newOperator.Name);
            var name = ValidateName(newOperator.Name);
            var surname = ValidateSurname(newOperator.Surname);
            var password = ValidatePassword(newOperator.Password);
            var contact = ValidateContact(newOperator.Contact);
            return login && name && surname && password && contact;
        }

    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WRO.BL
{
    public interface IAccountService
    {
        //returns false if user isn't in base or the password is wrong;
        //returns true is user & pass are correct
        bool Authenticate(string email, string password);

        //returns id of user if success
        //returns -1 if failed
        int AddUser(string email, string password);

        int GetUserId(string email);

        bool DoesUserExist(string email);
    }
}

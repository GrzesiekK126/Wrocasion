using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WRO.DAL;
using WRO.DAL.Repository;

namespace WRO.BL
{
    public class AccountService : IAccountService
    {
        IRepository<User> userRepository;
        WRO_DBEntities context;
        public AccountService()
        {
            context = new WRO_DBEntities();
            userRepository = new EFRepository<User>(context);
        }

        public bool Authenticate(string email, string password)
        {
            User user = userRepository.FindBy(x => x.email == email);

            //user exists and password is correct
            if (user != null && user.password == password)
                return true;

            //user not exists or password is incorrect
            return false;
        }

        public int AddUser(string email, string password)
        {
            User user = userRepository.FindBy(x => x.email == email);

            //register new user
            if (user == null)
            {
                User new_user = new User()
                {
                    email = email,
                    password = password,
                    date_joined = DateTime.Now
                };
                userRepository.Add(new_user);
                return new_user.id;
            }
            else
            {
                return -1;
            }
                
        }

        public bool DoesUserExist(string email)
        {
            User user = userRepository.FindBy(x => x.email == email);

            //user exists
            if (user != null)
                return true;
            return false;
        }

        public int GetUserId(string email)
        {
            User user = userRepository.FindBy(x => x.email == email);
            if (user == null)
                return -1;
            return user.id;
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WRO.DAL;

namespace WRO.BL
{
    public interface IUserService
    {
        ICollection<User> GetUsers();
        int GetUserSolvedBundlesCount(int userId);
        int GetUserSolvedRiddlesCount(int userId);
        int GetUserFoundLocationsCount(int userId);

        ICollection<User> GetUsersWithTaskId(int taskId);
        ICollection<string> GetUserPhotosWithTaskId(int userId, int taskId);
    }
}

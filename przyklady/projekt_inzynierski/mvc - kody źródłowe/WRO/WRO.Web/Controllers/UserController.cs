using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WRO.Web.Models;
using WRO.BL;

namespace WRO.Web.Controllers
{
    public class UserController : Controller
    {
        IUserService userService;

        public UserController()
        {
            userService = new UserService();

        }

        public ActionResult List()
        {
            var model = new UserListModel();
            model.users = new List<UserInListModel>();
            var users = userService.GetUsers();

            foreach(WRO.DAL.User user in users){
                model.users.Add(new UserInListModel()
                {
                    name = user.email,
                    solved_bundles = userService.GetUserSolvedBundlesCount(user.id),
                    solved_riddles = userService.GetUserSolvedRiddlesCount(user.id),
                    found_locations = userService.GetUserFoundLocationsCount(user.id)
                });
            }

            return View(model);
        }
    }
}
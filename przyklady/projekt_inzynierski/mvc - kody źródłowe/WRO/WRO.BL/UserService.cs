using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WRO.DAL;
using WRO.DAL.Repository;

namespace WRO.BL
{
    public class UserService : IUserService
    {
        IRepository<Bundle> bundleRepository;
        IRepository<Location> locationRepository;
        IRepository<Visit> visitRepository;
        IRepository<User> userRepository;
        IRepository<WRO.DAL.Task> taskRepository;
        IRepository<SolvedByUser> solvedByUserRepository;
        WRO_DBEntities context;

        public UserService()
        {
            context = new WRO_DBEntities();
            locationRepository = new EFRepository<Location>(context);
            visitRepository = new EFRepository<Visit>(context);
            bundleRepository = new EFRepository<Bundle>(context);
            userRepository = new EFRepository<User>(context);
            taskRepository = new EFRepository<WRO.DAL.Task>(context);
            solvedByUserRepository = new EFRepository<SolvedByUser>(context);
        }

        public ICollection<DAL.User> GetUsers()
        {
            return userRepository.GetAll().ToList();
        }

        private bool IsBundleSolvedByUser(int bundleId, int userId)
        {
            var tasks = taskRepository.GetBy(x => x.bundle_id == bundleId).ToList();

            IList<int> task_ids = new List<int>();
            foreach (DAL.Task task in tasks)
            {
                task_ids.Add(task.id);
            }

            int number = tasks.Count();

            var user_solvings = solvedByUserRepository.GetBy(x => x.user_id == userId);

            foreach (SolvedByUser solving in user_solvings)
            {
                for (int i = 0; i < task_ids.Count(); i++)
                {
                    if (solving.task_id == task_ids[i])
                        number--;
                }
            }

            if (number <= 0)
                return true;
            return false;
        }

        public int GetUserSolvedBundlesCount(int userId)
        {
            var bundles = bundleRepository.GetAll();
            int number = 0;
            foreach (Bundle bundle in bundles)
            {
                if (IsBundleSolvedByUser(bundle.id, userId))
                    number++;
            }
            return number;
        }

        public int GetUserSolvedRiddlesCount(int userId)
        {
            return solvedByUserRepository.GetBy(x => x.user_id == userId).Count();
        }

        public int GetUserFoundLocationsCount(int userId)
        {
            return visitRepository.GetBy(x => x.user_id == userId).Count();
        }

        public ICollection<DAL.User> GetUsersWithTaskId(int taskId)
        {
            var solvings = solvedByUserRepository.GetBy(x => x.task_id == taskId);
            IList<User> users = new List<User>();
            foreach (SolvedByUser solving in solvings)
            {
                users.Add(userRepository.FindBy(x => x.id == solving.user_id));
            }
            return users;
        }

        public ICollection<string> GetUserPhotosWithTaskId(int userId, int taskId)
        {
            var locations = locationRepository.GetBy(x => x.task_id == taskId);
            IList<string> photos = new List<string>();
            foreach (var location in locations)
            {
                var visit = visitRepository.FindBy(x => x.user_id == userId && x.location_id == location.id);
                if (visit != null)
                    photos.Add(visit.photo_path);
            }
            return photos;
        }
    }
}

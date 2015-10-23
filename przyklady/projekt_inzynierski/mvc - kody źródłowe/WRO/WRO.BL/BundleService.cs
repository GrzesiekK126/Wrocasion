using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WRO.DAL;
using WRO.DAL.Repository;

namespace WRO.BL
{
    public class BundleService : IBundleService
    {
        IRepository<Bundle> bundleRepository;
        IRepository<WRO.DAL.Task> taskRepository;
        IRepository<TaskType> taskTypeRepository;
        IRepository<Location> locationRepository;
        IRepository<Visit> visitRepository;
        IRepository<WRO.DAL.SolvedByUser> solvedByUserRepository;
        IRepository<Resource> resourceRepository;
        IRepository<User> userRepository;

        WRO_DBEntities context;
        public BundleService()
        {
            context = new WRO_DBEntities();
            bundleRepository = new EFRepository<Bundle>(context);
            taskRepository = new EFRepository<WRO.DAL.Task>(context);
            taskTypeRepository = new EFRepository<TaskType>(context);
            locationRepository = new EFRepository<Location>(context);
            visitRepository = new EFRepository<Visit>(context);
            solvedByUserRepository = new EFRepository<SolvedByUser>(context);
            resourceRepository = new EFRepository<Resource>(context);
            userRepository = new EFRepository<User>(context);
        }

        public ICollection<DAL.Bundle> GetAllBundles()
        {
            return bundleRepository.GetAll(x=> x.Task).ToList();
        }

        public DAL.Bundle AddNewBundle(DAL.Bundle newBundle)
        {
            bundleRepository.Add(newBundle);
            return bundleRepository.FindBy(x => x.id == newBundle.id);
        }

        public void SetReadyFlag(int bundleId)
        {
            Bundle bundle = bundleRepository.FindBy(x => x.id == bundleId);

            if( bundle != null)
            {
                bundle.ready = true;
                bundleRepository.Update(bundle);
            }
        }

        public ICollection<DAL.Bundle> GetAllReadyBundles()
        {
            return bundleRepository.GetBy(x => x.ready == true).ToList();
        }

        public Bundle GetBundleWithId(int bundleId)
        {
            return bundleRepository.FindBy(x => x.id == bundleId, x=> x.Task);
        }


        public DAL.Task AddTask(DAL.Task newTask)
        {
            taskRepository.Add(newTask);
            return taskRepository.FindBy(x => x.id == newTask.id);
        }

        public ICollection<WRO.DAL.Task> GetTasksWithBundleId(int bundleId)
        {
            return taskRepository.GetBy(x => x.bundle_id == bundleId).ToList();
        }

        public WRO.DAL.Task GetTaskWithId(int id)
        {
            return taskRepository.FindBy(x => x.id == id);
        }

        public ICollection<TaskType> GetTaskTypes()
        {
            return taskTypeRepository.GetAll().ToList();
        }

        public TaskType GetTaskTypeWithId(int taskTypeId)
        {
            return taskTypeRepository.FindBy(x => x.id == taskTypeId);
        }

        public bool IsTaskSolved(int taskId, int userId)
        {
            if (solvedByUserRepository.FindBy(x => x.task_id == taskId && x.user_id == userId) != null)
                return true;
            return false;

            /*var locations = locationRepository.GetBy(x => x.task_id == taskId);
            foreach (Location location in locations)
            {
                if (!DidUserVisitLocation(userId, location.id))
                    return false;
            }
            return true;*/
        }

        public int GetTasksCount(int bundleId)
        {
            var tasks = taskRepository.GetBy(x => x.bundle_id == bundleId);
            if (tasks == null)
                return 0;
            return tasks.Count();
        }

        public int GetSolvedTasksCount(int bundleId,int userId)
        {
            var tasks = taskRepository.GetBy(x => x.bundle_id == bundleId);
            if (tasks == null)
                return 0;
            int number = 0;
            foreach (WRO.DAL.Task task in tasks)
            {
                if (IsTaskSolved(task.id, userId))
                    number++;
            }
            return number;
        }


        public void DeleteBundle(int id)
        {
            var tasks = taskRepository.GetBy(x => x.bundle_id == id);
            foreach (var task in tasks.ToList())
            {
                DeleteTask(task.id);
            }

            var bundle = bundleRepository.FindBy(x=>x.id == id);
            if( bundle != null)
                bundleRepository.Delete(bundle);
        }

        public void DeleteTask(int id)
        {
            var locations = locationRepository.GetBy(x => x.task_id == id);
            foreach (var location in locations.ToList())
            {
                locationRepository.Delete(location);
            }

            var resources = resourceRepository.GetBy(x => x.task_id == id);
            foreach (var resource in resources.ToList())
            {
                resourceRepository.Delete(resource);
            }

            var task = taskRepository.FindBy(x => x.id == id);
            if (task != null)
                taskRepository.Delete(task);
        }


        public DAL.Task UpdateTask(DAL.Task task)
        {
            taskRepository.Update(task);
            return task;
        }

        public int GetMinimumToFound(int taskId)
        {
            var task = taskRepository.FindBy(x=>x.id == taskId);

            if(task.minimum_to_find == null)
                return locationRepository.GetBy(x=>x.task_id==taskId).Count();
            else
                return (int) task.minimum_to_find;
        }


        public bool SetSolved(int userId, int taskId)
        {
            var task = taskRepository.FindBy(x => x.id == taskId);
            if (task == null)
                return false;
            var user = userRepository.FindBy(x => x.id == userId);
            if (user == null)
                return false;
            solvedByUserRepository.Add(new SolvedByUser()
            {
                task_id = taskId,
                user_id = userId
            });
            return true;
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WRO.DAL;
namespace WRO.BL
{
    public interface IBundleService
    {
        ICollection<Bundle> GetAllBundles();
        Bundle AddNewBundle(Bundle newBundle);
        void SetReadyFlag(int bundleId);
        ICollection<DAL.Bundle> GetAllReadyBundles();
        Bundle GetBundleWithId(int bundleId);
        void DeleteBundle(int bundleId);

        WRO.DAL.Task AddTask(WRO.DAL.Task newTask);
        void DeleteTask(int id);
        WRO.DAL.Task UpdateTask(WRO.DAL.Task task);

        ICollection<WRO.DAL.Task> GetTasksWithBundleId(int bundleId);
        WRO.DAL.Task GetTaskWithId(int id);

        ICollection<TaskType> GetTaskTypes();
        TaskType GetTaskTypeWithId(int taskTypeId);

        bool IsTaskSolved(int taskId, int userId);
        int GetTasksCount(int bundleId);
        int GetSolvedTasksCount(int bundleId,int userId);
        int GetMinimumToFound(int taskId);
        bool SetSolved(int userId, int taskId);
    }
}

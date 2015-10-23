using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WRO.DAL;
using WRO.DAL.Repository;

namespace WRO.BL
{
    public class LocationService : ILocationService
    {
        IRepository<Location> locationRepository;
        IRepository<Visit> visitRepository;
        IRepository<User> userRepository;
        WRO_DBEntities context;

        public LocationService()
        {
            context = new WRO_DBEntities();
            locationRepository = new EFRepository<Location>(context);
            visitRepository = new EFRepository<Visit>(context);
            userRepository = new EFRepository<User>(context);
        }

        public ICollection<DAL.Location> GetLocationsWithTaskId(int taskId)
        {
            return locationRepository.GetBy(x => x.task_id == taskId).ToList();
        }

        public Location AddLocation(Location location)
        {
            locationRepository.Add(location);
            return location;
        }

        public Location GetLocationWithId(int id)
        {
            return locationRepository.FindBy(x => x.id == id);
        }

        public bool DidUserVisitLocation(int userId, int locationId)
        {
            var visit = visitRepository.FindBy(x => x.user_id == userId && x.location_id == locationId);
            if (visit != null)
                return true;
            return false;
        }

        public void DeleteLocation(int id)
        {
            locationRepository.Delete(locationRepository.FindBy(x=>x.id==id));
        }

        public bool SetVisit(int userId, int locationId, string photoPath)
        {
            var user = userRepository.FindBy(x => x.id == userId);
            if (user == null)
                return false;
            var location = locationRepository.FindBy(x => x.id == locationId);
            if (location == null)
                return false;
            visitRepository.Add(new Visit()
            {
                date = DateTime.Now,
                location_id = locationId,
                photo_path = photoPath,
                user_id = userId
            });
            return true;
        }
    }
}

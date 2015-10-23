using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WRO.DAL;

namespace WRO.BL
{
    public interface ILocationService
    {
        ICollection<Location> GetLocationsWithTaskId(int taskId);

        Location AddLocation(Location location);

        Location GetLocationWithId(int id);

        void DeleteLocation(int id);

        bool DidUserVisitLocation(int userId, int locationId);

        bool SetVisit(int userId, int locationId, string photoPath);
    }
}

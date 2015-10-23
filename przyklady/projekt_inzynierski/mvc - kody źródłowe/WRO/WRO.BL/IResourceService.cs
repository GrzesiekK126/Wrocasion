using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WRO.DAL;

namespace WRO.BL
{
    public interface IResourceService
    {
        ICollection<Resource> GetResourcesWithTaskId(int task_id);

        Resource AddResource(Resource resource);
        Resource GetResourceWithId(int id);
        void DeleteResource(int id);
        Boolean IsResourceADescription(int resource_id);
        void SelectResourceAsADescription(int? resource_id);
        ResourceType MimeToResourceType(string mimeType);
    }
}

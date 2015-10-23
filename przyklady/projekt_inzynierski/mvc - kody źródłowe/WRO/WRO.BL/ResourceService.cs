using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WRO.DAL;
using WRO.DAL.Repository;

namespace WRO.BL
{
    public class ResourceService : IResourceService
    {
        IRepository<Resource> resourceRepository;
        IRepository<ResourceType> resourceTypeRepository;
        IRepository<WRO.DAL.Task> taskRepository;
        WRO_DBEntities context;

        public ResourceService()
        {
            context = new WRO_DBEntities();
            resourceRepository = new EFRepository<Resource>(context);
            resourceTypeRepository = new EFRepository<ResourceType>(context);
            taskRepository = new EFRepository<WRO.DAL.Task>(context);
        }

        public ICollection<DAL.Resource> GetResourcesWithTaskId(int task_id)
        {
            return resourceRepository.GetBy(x => x.task_id == task_id).ToList();
        }

        public Resource AddResource(Resource resource)
        {
            resourceRepository.Add(resource);
            return resource;
        }
        public Resource GetResourceWithId(int id)
        {
            return resourceRepository.FindBy(x => x.id == id);
        }

        public ResourceType MimeToResourceType(string mimeType)
        {
            if (mimeType.Contains("image"))
                return resourceTypeRepository.FindBy(x => x.name == "image");
            if ( mimeType.Contains("audio/mp3"))
                return resourceTypeRepository.FindBy(x => x.name == "sound");
            if (mimeType.Contains("text/plain"))
                return resourceTypeRepository.FindBy(x => x.name == "text");
            if (mimeType.Contains("text/html"))
                return resourceTypeRepository.FindBy(x => x.name == "html");
            else return null;
        }


        public void DeleteResource(int id)
        {
            if (IsResourceADescription(id))
                SelectResourceAsADescription(null);

            var res = resourceRepository.FindBy(x => x.id == id);

            if(res != null)
                resourceRepository.Delete(res);
        }


        public bool IsResourceADescription(int resource_id)
        {
            var res = resourceRepository.FindBy(x=>x.id == resource_id);
            var task = taskRepository.FindBy(x => x.id == res.task_id);

            if (task.description_id == resource_id)
                return true;
            return false;
        }


        public void SelectResourceAsADescription(int? resource_id)
        {
            var res = resourceRepository.FindBy(x=>x.id == resource_id);
            var task = taskRepository.FindBy(x => x.id == res.task_id);
            task.description_id = resource_id;
            taskRepository.Update(task);
        }
    }
}

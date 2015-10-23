using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using AutoMapper;
using WRO.BL;
using System.Globalization;
namespace WRO.Web
{
    public class MapperConfig
    {
        public static void RegisterMappings()
        {
            ILocationService locationService = new LocationService();
            IResourceService resourceService = new ResourceService();
            IBundleService bundleService = new BundleService();
            //AUTOMAPPER
            Mapper.CreateMap<DAL.TaskType, Web.Models.TaskTypeForDropdown>()
                .ForMember(dest => dest.Name, opts => opts.MapFrom(src => src.type_name))
                .ForMember(dest => dest.Id, opts => opts.MapFrom(src => src.id));

            Mapper.CreateMap<DAL.Bundle, Web.Models.ApiModels.BundleModel>()
                .ForMember(dest=>dest.all_riddles, opts=>opts.MapFrom(src=> bundleService.GetTasksCount(src.id)));
            Mapper.CreateMap<DAL.Resource, Web.Models.ApiModels.Resource>();
            Mapper.CreateMap<DAL.Location, Web.Models.ApiModels.Location>();
            Mapper.CreateMap<DAL.Task, Web.Models.ApiModels.TaskInBundle>()
                .ForMember(dest => dest.name, opts => opts.MapFrom(src => src.task_name))
                .ForMember(dest => dest.type_id, opts => opts.MapFrom(src => src.task_type_id));
            Mapper.CreateMap<DAL.Bundle, Web.Models.ApiModels.BundleContent>()
                .ForMember(dest => dest.tasks, opts => opts.MapFrom(src => bundleService.GetTasksWithBundleId(src.id)));
            Mapper.CreateMap<DAL.Task, Web.Models.ApiModels.TaskContent>()
                .ForMember(dest => dest.name, opts => opts.MapFrom(src => src.task_name))
                .ForMember(dest=>dest.type_id, opts=>opts.MapFrom(src=>src.task_type_id))
                .ForMember(dest=>dest.minimum_to_find,opts=>opts.MapFrom(src=> bundleService.GetMinimumToFound(src.id)))
                .ForMember(dest => dest.locations, opts => opts.MapFrom(src => locationService.GetLocationsWithTaskId(src.id)))
                .ForMember(dest => dest.resources, opts => opts.MapFrom(src => resourceService.GetResourcesWithTaskId(src.id)));
            Mapper.CreateMap<Web.Models.AjaxModels.NewLocationAjaxModel, DAL.Location>()
                .ForMember(dest => dest.latitude, opts => opts.MapFrom(src => Double.Parse(src.latitude, new CultureInfo("en"))))
                .ForMember(dest => dest.longitude, opts => opts.MapFrom(src => Double.Parse(src.longitude, new CultureInfo("en"))));
            Mapper.CreateMap<DAL.Location, Web.Models.AjaxModels.NewLocationAjaxModel>()
                .ForMember(dest => dest.latitude, opts => opts.MapFrom(src => src.latitude.ToString(new CultureInfo("en"))))
                .ForMember(dest => dest.longitude, opts => opts.MapFrom(src => src.longitude.ToString(new CultureInfo("en"))));
            Mapper.CreateMap<DAL.Resource, Web.Models.AjaxModels.ResourceAjaxModel>()
                .ForMember(dest => dest.name, opts => opts.MapFrom(src => src.path))
                .ForMember(dest => dest.type, opts => opts.MapFrom(src => src.resource_type_id));
        }
    }
}
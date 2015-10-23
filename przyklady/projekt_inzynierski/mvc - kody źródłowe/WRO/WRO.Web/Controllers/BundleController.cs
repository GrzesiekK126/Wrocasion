using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WRO.BL;
using WRO.Web.Models;
using WRO.DAL;
using Newtonsoft.Json;
using System.IO;
using System.Drawing;
using System.Drawing.Imaging;
using System.Text;

namespace WRO.Web.Controllers
{
    public class BundleController : Controller
    {
        IBundleService bundleService;
        ILocationService locationService;
        IResourceService resourceService;
        IUserService userService;

        public BundleController()
        {
            bundleService = new BundleService();
            locationService = new LocationService();
            resourceService = new ResourceService();
            userService = new UserService();
        }

        #region bundles

 
        public ActionResult List()
        {
            bundleService = new BundleService();
            var model = new BundlesModel();
            model.bundles = bundleService.GetAllBundles();
            model.number_of_tasks = new Dictionary<int, int>();
            foreach (Bundle bundle in model.bundles)
            {
                model.number_of_tasks.Add(bundle.id, bundleService.GetTasksCount(bundle.id));
            }

            return View(model);
        }

        public ActionResult Detail(int id)
        {
            id = Int32.Parse((string)RouteData.Values["id"]);

            var model = new BundleDetailViewModel();
            model.BundleObject = bundleService.GetBundleWithId(id);
            model.TaskTypes = bundleService.GetTaskTypes();
            return View(model);
        }

        public ActionResult Publish(int id)
        {
            id = Int32.Parse((string)RouteData.Values["id"]);

            bundleService.SetReadyFlag(id);

            return RedirectToAction("List");
        }

        public ActionResult NewBundle()
        {
            return View();
        }

        [HttpPost]
        public ActionResult NewBundle(NewBundleViewModel model)
        {
            if ( ! ModelState.IsValid)
            {
                return View();
            }

            Bundle newBundle = new Bundle()
            {
                name = model.Name,
                ready = false
            };
            bundleService.AddNewBundle(newBundle);
            ViewBag.Message = "New bundle created successfully";
            return RedirectToAction("List");
        }

        public ActionResult DeleteBundle(int id)
        {
            bundleService.DeleteBundle(id);
            return RedirectToAction("List");
        }

        #endregion bundles

        #region tasks

        public ActionResult AddTask(int id)
        {
            if (id == 0)
                id = RouteData.Values["id"] as int? ?? 0;

            var model = new AddTaskViewModel();

            model.BundleId = id;

            return View(model);
        }

        [HttpPost]
        public ActionResult AddTask(AddTaskViewModel model)
        {
            var task = new DAL.Task()
            {
                bundle_id = model.BundleId,
                task_type_id = model.TypeId,
                task_name = model.Name,
                TaskType = bundleService.GetTaskTypeWithId(model.TypeId),
                Bundle = bundleService.GetBundleWithId(model.BundleId)
            };

            var new_task = bundleService.AddTask(task);
            return RedirectToAction("TaskDetail", new { id = new_task.id });
        }

        public ActionResult GetTaskTypesList()
        {
            var task_types = bundleService.GetTaskTypes();

            ICollection<Web.Models.TaskTypeForDropdown> types_for_model = new List<Web.Models.TaskTypeForDropdown>();

            AutoMapper.Mapper.Map(task_types, types_for_model);

            return Json(JsonConvert.SerializeObject(types_for_model), JsonRequestBehavior.AllowGet);
        }

        public ActionResult DeleteTask(int id)
        {
            var task = bundleService.GetTaskWithId(id);
            if (task != null)
            {
                int bundle_id = task.bundle_id;
                bundleService.DeleteTask(id);

                return RedirectToAction("Detail", new { id = bundle_id });
            }
            else
                return RedirectToAction("List");

        }

        
        public ActionResult TaskDetail(int id)
        {
            id = Int32.Parse((string)RouteData.Values["id"]);

            DAL.Task task = bundleService.GetTaskWithId(id);

            if( task == null)
                return HttpNotFound("Item Not Found");

            TaskDetailViewModel model = new TaskDetailViewModel()
            {
                Name = task.task_name,
                TypeId = task.task_type_id,
                BundleId = task.bundle_id,
                Id = task.id,
                MinimumToFound = (task.minimum_to_find == null) ? 0 : (int)task.minimum_to_find
            };

            return View(model);
        }

        [HttpPost]
        public ActionResult TaskDetail(TaskDetailViewModel model)
        {
            DAL.Task task = bundleService.GetTaskWithId(model.Id);

            if (task == null)
                return HttpNotFound("Item Not Found");

            task.task_name = model.Name;
            task.task_type_id = model.TypeId;
            if (model.MinimumToFound == 0)
                task.minimum_to_find = null;
            else
                task.minimum_to_find = model.MinimumToFound;


            bundleService.UpdateTask(task);

            return View(model);
        }
        #endregion

        #region resources in task details

        [AllowAnonymous]
        public JsonResult Upload(FormCollection form)
        {
            for (int i = 0; i < Request.Files.Count; i++)
            {
                HttpPostedFileBase file = Request.Files[i];
                string fileName = file.FileName;
                string mimeType = file.ContentType;
                ResourceType resType = resourceService.MimeToResourceType(mimeType);

                if (resType == null)
                    continue;

                System.IO.Stream fileContent = file.InputStream;
                string savePath = Server.MapPath("/files/") + fileName;
                file.SaveAs(savePath);

                var task_id = form["task_id"];
                var parsed_task_id = Int32.Parse(task_id);
                resourceService.AddResource(new Resource()
                {
                    path = fileName,
                    resource_type_id = resType.id,
                    task_id = parsed_task_id
                });
            }
            return Json("Uploaded " + Request.Files.Count + " files", JsonRequestBehavior.AllowGet);
        }

        public JsonResult GetResources(int id)
        {
            ICollection<Resource> resources = resourceService.GetResourcesWithTaskId(id);
            ICollection<Models.AjaxModels.ResourceAjaxModel> ajax_resources = new List<Models.AjaxModels.ResourceAjaxModel>();
            AutoMapper.Mapper.Map(resources, ajax_resources);
            foreach (var res in ajax_resources)
            {
                res.is_description = resourceService.IsResourceADescription(Int32.Parse(res.id))?"True":"False";
            }
            return Json(JsonConvert.SerializeObject(ajax_resources), JsonRequestBehavior.AllowGet);
        }

        public JsonResult GetUsersWithPhotos(int id)
        {
            var model = new List<UserWithPhotosModel>();

            ICollection<User> users = userService.GetUsersWithTaskId(id);
            foreach (var user in users)
            {
                model.Add(new UserWithPhotosModel()
                {
                    name = user.email,
                    photos = userService.GetUserPhotosWithTaskId(user.id,id)
                });
            }

            return Json(JsonConvert.SerializeObject(model), JsonRequestBehavior.AllowGet);
        }

        [HttpPost]

        public JsonResult DeleteResource(int id)
        {
            resourceService.DeleteResource(id);

            return Json("ok", JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public JsonResult SelectDescription(int id)
        {
            resourceService.SelectResourceAsADescription(id);

            return Json("ok", JsonRequestBehavior.AllowGet);
        }

        #endregion

        #region location in task details

        [HttpPost]
        public JsonResult AddLocation(Models.AjaxModels.NewLocationAjaxModel model)
        {
            Location location = new Location();
            AutoMapper.Mapper.Map(model, location);
            locationService.AddLocation(location);

            return Json("ok", JsonRequestBehavior.AllowGet);
        }

        public JsonResult GetLocations(int id)
        {
            ICollection<Location> locations = locationService.GetLocationsWithTaskId(id);
            ICollection<Models.AjaxModels.NewLocationAjaxModel> ajax_locations = new List<Models.AjaxModels.NewLocationAjaxModel>();
            AutoMapper.Mapper.Map(locations, ajax_locations);

            return Json(JsonConvert.SerializeObject(ajax_locations), JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public JsonResult DeleteLocation(int id)
        {
            locationService.DeleteLocation(id);

            return Json("ok", JsonRequestBehavior.AllowGet);
        }

        #endregion
    }
}
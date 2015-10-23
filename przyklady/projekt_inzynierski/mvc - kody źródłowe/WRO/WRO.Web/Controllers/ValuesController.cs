using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Security.Principal;
using System.Text;
using System.Web.Http;
using WRO.BL;
using WRO.DAL;

namespace WRO.Web.Controllers
{
    public class ValuesController : ApiController
    {
        IBundleService bundleService;
        IAccountService accountService;
        ILocationService locationService;

        public ValuesController()
        {
            bundleService = new BundleService();
            accountService = new AccountService();
            locationService = new LocationService();
        }

        [HttpGet]
        public HttpResponseMessage bundles(int id)
        {
            int userId = id;
            var bundles = bundleService.GetAllReadyBundles();

            var bundles_for_api = new List<Web.Models.ApiModels.BundleModel>();

            foreach (var bun in bundles)
            {
                var new_bun = new Models.ApiModels.BundleModel();

                new_bun.id = bun.id;
                new_bun.name = bun.name;
                new_bun.solved_riddles = bundleService.GetSolvedTasksCount(new_bun.id, userId);
                new_bun.all_riddles = bundleService.GetTasksCount(new_bun.id);

                bundles_for_api.Add(new_bun);
            }

            //AutoMapper.Mapper.Map(bundles, bundles_for_api);    

           /* foreach (Web.Models.ApiModels.BundleModel bundle in bundles_for_api)
            {
                bundle.solved_riddles = bundleService.GetSolvedTasksCount(bundle.id,userId);
            }*/

            return Request.CreateResponse(HttpStatusCode.OK, bundles_for_api);
        }

        [HttpGet]
        public HttpResponseMessage bundle(int id, int user_id)
        {
            var bundle = bundleService.GetBundleWithId(id);

            var bundle_for_api = new Web.Models.ApiModels.BundleContent();

            AutoMapper.Mapper.Map(bundle, bundle_for_api);

            foreach (Models.ApiModels.TaskInBundle task in bundle_for_api.tasks)
            {
                task.is_solved = bundleService.IsTaskSolved(task.id, user_id);
            }

            return Request.CreateResponse(HttpStatusCode.OK, bundle_for_api);
        }

        [HttpGet]
        public HttpResponseMessage riddle(int id, int user_id)
        {
            var riddle = bundleService.GetTaskWithId(id);
            var riddle_for_api = new Models.ApiModels.TaskContent();
            AutoMapper.Mapper.Map(riddle, riddle_for_api);

            riddle_for_api.minimum_to_find = bundleService.GetMinimumToFound(riddle.id);
            foreach (Web.Models.ApiModels.Location location in riddle_for_api.locations)
            {
                location.is_found = locationService.DidUserVisitLocation(user_id, location.id);
            }

            return Request.CreateResponse(HttpStatusCode.OK, riddle_for_api);

        }

        [HttpGet]
        public HttpResponseMessage login()
        {
            var credentials = GetCredentials(Request.Headers.Authorization);

            int user_id = accountService.GetUserId(credentials[0]);
            if (user_id == -1)
                return Request.CreateResponse(HttpStatusCode.Unauthorized);
            return Request.CreateResponse(HttpStatusCode.OK, user_id);
        }

        [AllowAnonymous]
        [HttpGet]
        public HttpResponseMessage register()
        {
            var authHeader = Request.Headers.Authorization;

            if (authHeader != null)
            {
                if (authHeader.Scheme.Equals("basic", StringComparison.OrdinalIgnoreCase) && !string.IsNullOrWhiteSpace(authHeader.Parameter))
                {
                    
                    var credentials = GetCredentials(authHeader);

                    IAccountService accountService = new AccountService();

                    if (credentials.Length != 2)
                    {
                        return Request.CreateResponse(HttpStatusCode.OK, "no");
                    }
                    else if (accountService.DoesUserExist(credentials[0]))
                    {
                        return Request.CreateResponse(HttpStatusCode.OK, "exist");
                    }
                    else if (accountService.AddUser(credentials[0], credentials[1]) > -1)
                    {
                        return Request.CreateResponse(HttpStatusCode.OK, "done");
                    }
                }
            }
            return Request.CreateResponse(HttpStatusCode.OK, "no");
        }

        private string[] GetCredentials(AuthenticationHeaderValue authHeader)
        {
            var raw = authHeader.Parameter;
            var encoding = Encoding.ASCII;
            var credentials = encoding.GetString(Convert.FromBase64String(raw));
            return credentials.Split(':');
        }

        [HttpPost]
        public HttpResponseMessage UserFoundLocation(Models.ApiModels.UserFoundLocationModel model)
        {
            var success = locationService.SetVisit(model.user_id, model.location_id, model.photo_path);

            if(success)
                return Request.CreateResponse(HttpStatusCode.OK, "done");
            return Request.CreateResponse(HttpStatusCode.OK, "problem");
        }

        [HttpPost]
        public HttpResponseMessage RiddleSolved(Models.ApiModels.RiddleSolvedModel model)
        {
            var success = bundleService.SetSolved(model.user_id, model.riddle_id);

            if (success)
                return Request.CreateResponse(HttpStatusCode.OK, "done");
            return Request.CreateResponse(HttpStatusCode.OK, "problem");
        }


        [HttpPost]
        [AllowAnonymous]
        public HttpResponseMessage UploadText(WRO.Web.Models.ApiModels.UploadedPhotoModel model)
        {
            string photo = model.encoded_data;
            string fileName = new Random().Next(999999).ToString() + ".txt";

            string savePath = System.Web.Hosting.HostingEnvironment.MapPath("/files/") + fileName;

            byte[] converted = Convert.FromBase64CharArray(photo.ToCharArray(), 0, photo.Length);

            System.IO.File.WriteAllBytes(@savePath, converted);
            return Request.CreateResponse(HttpStatusCode.OK, fileName);
        }

        [HttpPost]
        [AllowAnonymous]
        public HttpResponseMessage UploadJpeg(WRO.Web.Models.ApiModels.UploadedPhotoModel model)
        {
            string photo = model.encoded_data;
            string fileName = new Random().Next(999999).ToString() + ".jpg";

            string savePath = System.Web.Hosting.HostingEnvironment.MapPath("/files/") + fileName;

            byte[] converted = Convert.FromBase64CharArray(photo.ToCharArray(), 0, photo.Length);

            System.IO.File.WriteAllBytes(@savePath, converted);
            return Request.CreateResponse(HttpStatusCode.OK, fileName);
        }
    }
}

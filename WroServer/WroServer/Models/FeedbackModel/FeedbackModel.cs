using System.Collections.Generic;

namespace WroServer.Models.FeedbackModel
{
    public class FeedbackModel
    {
        public int Stars { get; set; }
        public List<string> EventsId { get; set; }
        public string Description { get; set; }
        public string Username { get; set; }
    }
}
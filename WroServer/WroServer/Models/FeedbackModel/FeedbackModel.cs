using System.Collections.Generic;

namespace WroServer.Models.FeedbackModels
{
    public class FeedbackModel
    {
        public int Rate { get; set; }
        public int EventId { get; set; }
        public string Description { get; set; }
        public string UserName { get; set; }
    }
}
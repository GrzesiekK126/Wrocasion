namespace WroServer.Models
{
    public class UserResponseModel
    {
        public int Id { get; set; }
        public string Message { get; set; } 
        public bool CorrectLogin { get; set; }
    }
}
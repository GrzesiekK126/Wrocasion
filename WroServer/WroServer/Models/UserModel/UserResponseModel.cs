namespace WroServer.Models.UserModel
{
    public class UserResponseModel
    {
        public int Id { get; set; }
        public string Message { get; set; } 
        public bool CorrectLogin { get; set; }
    }
}
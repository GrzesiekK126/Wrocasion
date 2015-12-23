namespace WroBL
{
    public static class UserLogin
    {
        public static bool ComparePassword(string passwordFromAdnroid, string userName)
        {
            var passwordFromDatabase =
                WroBL.DAL.DatabaseUtils.GetOneElement("select u.password from users u where u.name='" + userName + "';");
            var encPassword =
                WroBL.DAL.Crytography.DecryptRijndaelManaged((WroBL.DAL.Crytography.GetBytes(passwordFromDatabase)),
                    WroBL.DAL.Crytography.ElChupacabra, WroBL.DAL.Crytography.ElMariachi);
            if (encPassword==passwordFromAdnroid)
            {
                return true;
            }
            else
            {
                return false;
            }
         }         
    }
}
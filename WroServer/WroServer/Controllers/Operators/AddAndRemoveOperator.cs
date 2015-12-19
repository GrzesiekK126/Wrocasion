namespace WroServer.Controllers.Operators
{
    public static class AddAndRemoveOperator
    {
        public static void AddOperator(Models.OperatorModel newOperator)
        {
            //CRYPTION PASSWORD
            var enPassword =WroBL.DAL.Crytography.EncryptRijndaelManaged(newOperator.Password, WroBL.DAL.Crytography.ElChupacabra, WroBL.DAL.Crytography.ElMariachi);
            var password = WroBL.DAL.Crytography.GetString(enPassword);

            WroBL.DAL.DatabaseUtils.DatabaseCommand("INSERT INTO OPERATOR (LOGIN, \"PASSWORD\", NAME, SURNAME, CONTACT, CONTACT_FORM, \"ROLE\")"
                                                                  +"VALUES('"+newOperator.Login+"', '"+password+"', '"+newOperator.Name+"', '"+newOperator.Surname+"', '"+newOperator.Contact+"', "+newOperator.ContactForm+", "+newOperator.Role+"); ");


        }

        public static void RemoveOperator(Models.OperatorModel removeOperator)
        {

        }
    }
}
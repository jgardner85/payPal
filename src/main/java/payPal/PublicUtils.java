package payPal;

import javax.servlet.http.HttpServletRequest;

public class PublicUtils {
	private static String user="AUyTSBC2rmrtzBBBMtBjM-9EpzzjmjW9L4Dt3mZqvQtlc4CwJ__A21rhSeOy";
	private static String pass="EA_e1hCcRSeMYzzDconoTBlVP4evgY09xl8AjPAE7xGYjbzweVYQpLB33Ywa";
	public static final int MAX_ARR_SIZE=1000;
	public static final int MAX_ARR_COL=7;
	public static final String CARD_FILE="/Users/jonathangardner/Documents/Eclipse/workspace/payPal/cardInfo.txt";
	public static String getUser()
	{
		return user;
	}
	public static String getPass()
	{
		return pass;
	}
	public static void handleException(Exception e,String linePrefix,HttpServletRequest req)
	{
		String error=e.getMessage();
		req.setAttribute("error", error);
		// log errors by writing to error file
		String[][] errors=new String[1][1];
		//errors[0][0]=user;
		//errors[0][1]=pass;
		//errors[0][2]=error;
		errors[0][0]=linePrefix+";"+error;
		try
		{
			char delim=(char) 9;
			InputOutput.writeFile("/Users/jonathangardner/Documents/Eclipse/workspace/payPal/errorLog.txt",delim,errors);
		}
		catch(Exception err)
		{
			System.out.println(err.getMessage());
		}
	}
}
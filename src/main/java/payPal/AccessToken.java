package payPal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import payPal.InputOutput;

import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;

public class AccessToken {
	private String accessToken;
	private APIContext context;
	public AccessToken(String user,String pass,HttpServletRequest req, HttpServletResponse resp)
	{
		this.init(user,pass,req,resp);
	}
	public String getToken()
	{
		return accessToken;
	}
	public APIContext getContext()
	{
		return context;
	}
	public void init(String user,String pass,HttpServletRequest req, HttpServletResponse resp)
	{
		try
		{
			accessToken=new OAuthTokenCredential(user, pass).getAccessToken();
			context = new APIContext(accessToken);
		}
		catch(PayPalRESTException e)
		{
			String error=e.getMessage();
			req.setAttribute("error", error);
			// log errors by writing to error file
			String[][] errors=new String[1][3];
			errors[0][0]=user;
			errors[0][1]=pass;
			errors[0][2]=error;
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
}

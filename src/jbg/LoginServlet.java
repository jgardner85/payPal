package jbg;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import payPal.PublicUtils;


public class LoginServlet extends HttpServlet
{
		private static final long serialVersionUID=1;
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
		{
			PrintWriter out = resp.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println("Made it to LoginServlet");
			out.println("<form action=\"LoginServlet\" method=\"post\">");
			out.println("<table>");
			out.println("<tr><td>");
			out.println("User<td><input name=\"user\" ");
			out.println("value=" +PublicUtils.getUser()+" />");
			out.println("<tr><td>");
			out.println("Pass<td><input name=\"pass\" ");
			out.println("value=" +PublicUtils.getPass()+" />");
			out.println("</table>");
			out.println("<input type=\"submit\" />");
			out.println("</form></body></html>");
		}
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
		{
			String user=req.getParameter("user");
			String pass=req.getParameter("pass");
			PrintWriter out = resp.getWriter();
			boolean userBlank,passBlank;
			userBlank=isBlank(user);
			passBlank=isBlank(pass);
			if(userBlank||passBlank)
			{
				out.println("<html>");
				out.println("<body>");
				out.println("Made it to LoginServlet");
				out.println("<form action=\"LoginServlet\" method=\"post\">");
				out.println("<table>");
				out.println("<tr><td>");
				if(userBlank)
				{
					out.println("MISSING! ");
					out.println("User<td><input name=\"user\" />");
				}
				else
				{
					out.println("User<td><input name=\"user\" ");
					out.println("value=" +user+" />");
				}
				out.println("<tr><td>");
				if(passBlank)
				{
					out.println("MISSING! ");
					out.println("Pass<td><input name=\"pass\" />");
				}
				else
				{
					out.println("Pass<td><input name=\"pass\" ");
					out.println("value=" +pass+" />");
				}
				out.println("</table>");
				out.println("<input type=\"submit\" />");
				out.println("</form></body></html>");
			}
			else //successful login
			{
				out.println("<html><body>");
				out.println("User: "+user);
				out.println("Pass: "+pass);
				out.println("SUCCESS");
				printPaymentInfoForm(out,user,pass);
				out.println("</body></html>");
			}
			
		}
		private void printPaymentInfoForm(PrintWriter out,String user,String pass)
		{
			out.println("<form action=\"PaymentInfoServlet2\" method=\"post\"><table>");
			out.println("<tr><td><input type=\"hidden\" name=\"user\" value=\""+user+"\" />");
			out.println("<tr><td><input type=\"hidden\" name=\"pass\" value=\""+pass+"\" />");
			out.println("<tr><td>Amount<td><input name=\"amount\" value=\"10\" />");
			out.println("<tr><td><input type=\"submit\" value=\"Go to payPal\" />");
			out.println("</table></form>");
		}
		private boolean isBlank(String str)
		{
			if(str==null || str.length()<=0){return true;}
			return false;
		}
}
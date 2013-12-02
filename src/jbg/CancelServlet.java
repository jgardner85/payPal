package jbg;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.paypal.api.payments.Payment;

public class CancelServlet  extends HttpServlet
{
	private static final long serialVersionUID=1;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		String sessID=req.getParameter("sessID");
		PrintWriter out = resp.getWriter();
		out.println("Session ID is "+sessID);
		HttpSession sess=SessionStore.map.get(sessID);
		out.println("user: "+sess.getAttribute("user"));
		out.println("pass: "+sess.getAttribute("pass"));
		out.println("token: "+sess.getAttribute("token"));
		out.println("context: "+sess.getAttribute("context"));
		out.println("fart: "+sess.getAttribute("fart"));
		@SuppressWarnings("unchecked")
		ArrayList<Payment> cPay=(ArrayList<Payment>)sess.getAttribute("createdPayments");
		if (cPay==null){out.println("cPay is null");}
		else
		{
			Payment payment=(Payment)cPay.get(0);
			out.println("Payment state is "+payment.getState());
			out.println("Payment intent is "+payment.getIntent());
			out.println("Payment amount is "+payment.getTransactions().get(0).getAmount());
			//payment.execute(req.getParameter("token"), paymentExecution)
		}
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		doGet(req,resp);
	}
}

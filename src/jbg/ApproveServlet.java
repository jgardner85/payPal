package jbg;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import payPal.AccessToken;
import payPal.PublicUtils;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.core.rest.PayPalRESTException;

public class ApproveServlet  extends HttpServlet
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
			//Payment newP=new Payment();
			//newP.setId(payment.getId());
			Payment newP=payment;
			AccessToken access=new AccessToken(PublicUtils.getUser(),PublicUtils.getPass(),req,resp);
			out.println("Payment state is "+newP.getState());
			out.println("Payment intent is "+newP.getIntent());
			//out.println("Payment amount is "+newP.getTransactions().get(0).getAmount());
			//payment.execute(req.getParameter("token"), paymentExecution)
			String payerID=req.getParameter("PayerID");
			PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerID);
            out.println("EXECUTEDPayment payerID is "+payerID);
            out.println("EXECUTEDPayment paymentID is "+newP.getId());
            newP.setIntent("sale");
            String token1=req.getParameter("token");
            try {
            		//String context=sess.getAttribute("context").toString();
                   // payment.execute(context, paymentExecution);
            	newP.execute(access.getContext(), paymentExecution);
                    out.println("EXECUTEDPayment state is "+newP.getState());
        			out.println("EXECUTEDPayment intent is "+newP.getIntent());
        			Payment ppNN = payment.execute(access.getContext(), paymentExecution);
                //  req.setAttribute("response",Payment.getLastResponse());

                  System.out.println(ppNN.getId());
                  System.out.println(Payment.getLastResponse());
  //

                  payment = Payment.get(access.getToken(),req.getParameter("token"));
         
                  payment = Payment.get(access.getToken(), ppNN.getId());
                  System.out.println("Payment retrieved ID = " +payment.getId()
                		  + ", status = " + payment.getState());

        			//out.println("EXECUTEDPayment amount is "+newP.getTransactions().get(0).getAmount());
                    req.setAttribute("response", Payment.getLastResponse());
            } catch (PayPalRESTException e) {
            	out.println("ERROR:"+ e.getMessage());
                    req.setAttribute("error", e.getMessage());
            }
		}
	}
}

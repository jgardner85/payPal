package jbg;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.core.rest.PayPalRESTException;

import payPal.AccessToken;

public class PaymentInfoServlet extends HttpServlet
{
	private static final long serialVersionUID=1;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		PrintWriter out=resp.getWriter();
		out.println("Word.");
		String user=req.getParameter("user");
		String pass=req.getParameter("pass");
		AccessToken access=new AccessToken(user,pass,req,resp);
		HttpSession sess=req.getSession();
		sess.setAttribute("token",access.getToken());
		sess.setAttribute("context",access.getContext());
		sess.setAttribute("user",user);
		sess.setAttribute("pass",pass);
		sess.setAttribute("fart", "It was him");
		String sessID=sess.getId();
		out.println("Session is "+sessID);
		out.println("Context is "+sess.getAttribute("context"));
		out.println("Token is "+sess.getAttribute("token"));
		
		if(false) //only used if payment already created
		{
			Payment payment = new Payment();
		      //  if (req.getParameter("guid") != null) {
		      ///          payment.setId(map.get(req.getParameter("guid")));
		     //   }

	        PaymentExecution paymentExecution = new PaymentExecution();
	        paymentExecution.setPayerId(req.getParameter(user));
	        try {
	                payment.execute(access.getContext(), paymentExecution);
	                req.setAttribute("response", Payment.getLastResponse());
	        } catch (PayPalRESTException e) {
	                req.setAttribute("error", e.getMessage());
	        }
		}
		else
		{
        
	        // if payment not already created?
	     // ###Details
	        // Let's you specify details of a payment amount.
	        Details details = new Details();
	        details.setShipping("1");
	        details.setSubtotal("5");
	        details.setTax("1");
	
	        // ###Amount
	        // Let's you specify a payment amount.
	        Amount amount = new Amount();
	        amount.setCurrency("USD");
	        // Total must be equal to sum of shipping, tax and subtotal.
	        amount.setTotal("7");
	        amount.setDetails(details);
	
	        // ###Transaction
	        // A transaction defines the contract of a
	        // payment - what is the payment for and who
	        // is fulfilling it. Transaction is created with
	        // a `Payee` and `Amount` types
	        Transaction transaction = new Transaction();
	        transaction.setAmount(amount);
	        transaction
	                        .setDescription("This is the payment transaction description.");
	
	        // The Payment creation API requires a list of
	        // Transaction; add the created `Transaction`
	        // to a List
	        List<Transaction> transactions = new ArrayList<Transaction>();
	        transactions.add(transaction);
	
	        // ###Payer
	        // A resource representing a Payer that funds a payment
	        // Payment Method
	        // as 'paypal'
	        Payer payer = new Payer();
	        payer.setPaymentMethod("paypal");
	
	        // ###Payment
	        // A Payment Resource; create one using
	        // the above types and intent as 'sale'
	        Payment payment = new Payment();
	        payment.setIntent("authorize");
	        payment.setPayer(payer);
	        payment.setTransactions(transactions);
	
	        // ###Redirect URLs
	        RedirectUrls redirectUrls = new RedirectUrls();
	        String cancelURL,approveURL;
	    //    cancelURL=resp.encodeRedirectURL(req.getScheme() + "://"
          //          + req.getServerName() + ":" + req.getServerPort()
            //        + req.getContextPath() +"/cancelURL.html");
	   //     approveURL=resp.encodeRedirectURL(req.getScheme() + "://"
         //           + req.getServerName() + ":" + req.getServerPort()
           //         + req.getContextPath() +"/approveURL.html");
	        approveURL=req.getScheme() + "://"
	                           + req.getServerName() + ":" + req.getServerPort()
	                        + req.getContextPath() +"/ApproveServlet?sessID=" + sess.getId();
	        cancelURL=req.getScheme() + "://"
                    + req.getServerName() + ":" + req.getServerPort()
                 + req.getContextPath() +"/CancelServlet?sessID=" + sess.getId();
	      //  String guid = UUID.randomUUID().toString().replaceAll("-", "");
	        redirectUrls.setCancelUrl(cancelURL);
	        redirectUrls.setReturnUrl(approveURL);
	        out.println("cancelURL "+cancelURL);
	        out.println("approveURL "+approveURL);
	        payment.setRedirectUrls(redirectUrls);
	        String url="";
	        // Create a payment by posting to the APIService
	        // using a valid AccessToken
	        // The return object contains the status;
	        try {
	                Payment createdPayment = payment.create(access.getContext());
	                System.out.println(createdPayment.getState());
	               // LOGGER.info("Created payment with id = "
	                             //   + createdPayment.getId() + " and status = "
	                              //  + createdPayment.getState());
	                // ###Payment Approval Url
	                Iterator<Links> links = createdPayment.getLinks().iterator();
	                boolean found=false;
	                while (links.hasNext()) {
	                	
	                        Links link = links.next();
	                        System.out.println(link.getRel());
	                        if (link.getRel().equalsIgnoreCase("approval_url")) {
	                                req.setAttribute("redirectURL", link.getHref());
	                                //resp.sendRedirect(approveURL);
	                                url=link.getHref();
	                                resp.sendRedirect(url);
	                                found=true;
	                        }
	                }
	                if(!found){
	                	//resp.sendRedirect(cancelURL);
	                	resp.sendRedirect(url);
	                }
	                //System.out.println("when does it make it here?");
	                SessionStore.map.put(sessID, sess);
	                req.setAttribute("response", Payment.getLastResponse());
	                ArrayList<Payment> createdPayments=new ArrayList<Payment>();
	                createdPayments.add(createdPayment);
	                sess.setAttribute("createdPayments", createdPayments);
	                System.out.println("when does it make it here?");
	               // map.put(guid, createdPayment.getId());
	        } catch (PayPalRESTException e) {
	                req.setAttribute("error", e.getMessage());
	        }
		}
	}
}

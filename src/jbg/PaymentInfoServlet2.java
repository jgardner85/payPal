package jbg;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;

import payPal.AccessToken;
import payPal.PublicUtils;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
//import com.paypal.api.payments.util.GenerateAccessToken;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.core.rest.PayPalResource;

/**
 * @author lvairamani
 * 
 */
public class PaymentInfoServlet2 extends HttpServlet {

        private static final long serialVersionUID = 1L;

       // private static final Logger LOGGER = Logger
         //               .getLogger(PaymentWithPayPalServlet.class);
     //   Map<String, String> map = new HashMap<String, String>();

        public void init(ServletConfig servletConfig) throws ServletException {
                // ##Load Configuration
                // Load SDK configuration for
                // the resource. This intialization code can be
                // done as Init Servlet.
                InputStream is = PaymentInfoServlet2.class
                                .getResourceAsStream("/sdk_config.properties");
                try {
                        PayPalResource.initConfig(is);
                } catch (PayPalRESTException e) {
                      //  LOGGER.fatal(e.getMessage());
                }

        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                        throws ServletException, IOException {
                doPost(req, resp);
        }

        // ##Create
        // Sample showing to create a Payment using PayPal
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                        throws ServletException, IOException {
                // ###AccessToken
                // Retrieve the access token from
                // OAuthTokenCredential by passing in
                // ClientID and ClientSecret
                APIContext apiContext = null;
             //   String accessToken = null;
                try {
                	AccessToken access=new AccessToken(PublicUtils.getUser(),PublicUtils.getPass(),req,resp);

                        // ### Api Context
                        // Pass in a `ApiContext` object to authenticate
                        // the call and to send a unique request id
                        // (that ensures idempotency). The SDK generates
                        // a request id if you do not pass one explicitly.
                        apiContext = access.getContext();
                        // Use this variant if you want to pass in a request id
                        // that is meaningful in your application, ideally
                        // a order id.
                        /*
                         * String requestId = Long.toString(System.nanoTime(); APIContext
                         * apiContext = new APIContext(accessToken, requestId ));
                         */
                } catch (Exception e) {
                        req.setAttribute("error", e.getMessage());
                }
                if (req.getParameter("PayerID") != null) {
                   //     Payment payment = new Payment();
                   //     if (req.getParameter("guid") != null) {
                   //             payment.setId(map.get(req.getParameter("guid")));
                  //      }
                	AccessToken ac=new AccessToken(PublicUtils.getUser(),PublicUtils.getPass(),req,resp);
                	String sessID=req.getParameter("sessID");
                	HttpSession sess=SessionStore.map.get(sessID);
                	@SuppressWarnings("unchecked")
					ArrayList<Payment> cPay=(ArrayList<Payment>)sess.getAttribute("createdPayments");
                	Payment payment=(Payment)cPay.get(0);
                        PaymentExecution paymentExecution = new PaymentExecution();
                        paymentExecution.setPayerId(req.getParameter("PayerID"));
                        Payment temp=new Payment();
                        try {
                              // temp=payment.execute(req.getParameter("token"), paymentExecution);
                            temp=payment.execute(ac.getContext(), paymentExecution);
                        	req.setAttribute("response", Payment.getLastResponse());
                        } catch (PayPalRESTException e) {
                                req.setAttribute("error", e.getMessage());
                        }
                        PrintWriter out=resp.getWriter();
                        out.println("This is the returnURL page.");
                        out.println("paymentID="+payment.getId());
                        out.println("paymentState="+payment.getState());
                        out.println("executedPayerID="+paymentExecution.getPayerId());
                  //     out.println("executedTransaction: "+paymentExecution.getTransactions().get(0).toString());
                        out.println("paymentCreateTime="+payment.getCreateTime());
                        out.println("paymentIntent="+payment.getIntent());
                        out.println("paymentUpdateTime="+payment.getUpdateTime());
                        out.println("paymentPayer="+payment.getPayer());
                        out.println("paymentLinks="+payment.getLinks());
                        out.println("paymentTransactions="+payment.getTransactions());
                        
                        try{
                     //   Payment temp=Payment.get(token, payment.getId());
                        if(temp==null){
                        		out.println("temp is null");
                        }
                        else
                        {
                        	out.println("tempID="+temp.getId());
                        	out.println("tempState="+temp.getState());
                        }
                        }
                        catch(Exception e)
                        {
                        	System.out.println(e.getMessage());
                        }
                      //  String token=req.getParameter("token");
                        out.println("blah: "+temp.getTransactions().get(0).getRelatedResources().get(0).getSale().getId());
                        
                   
                } else {
                	HttpSession sess=req.getSession();
                	String sessID=sess.getId();
                	SessionStore.map.put(sessID, sess);
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
                        payment.setIntent("sale");
                        payment.setPayer(payer);
                        payment.setTransactions(transactions);

                        // ###Redirect URLs
                        RedirectUrls redirectUrls = new RedirectUrls();
                  //      String guid = UUID.randomUUID().toString().replaceAll("-", "");
                   //     redirectUrls.setCancelUrl(req.getScheme() + "://"
                   //                     + req.getServerName() + ":" + req.getServerPort()
                   //                     + req.getContextPath() + "/CancelServlet?guid=" + guid);
                   //     redirectUrls.setReturnUrl(req.getScheme() + "://"
                    //                    + req.getServerName() + ":" + req.getServerPort()
                  //                      + req.getContextPath() + "/PaymentInfoServlet2?guid=" + guid);
                  //      payment.setRedirectUrls(redirectUrls);
                        String approveURL=req.getScheme() + "://"+ req.getServerName() + ":" +req.getServerPort()+ req.getContextPath() +"/PaymentInfoServlet2?sessID=" +sess.getId();
                        String cancelURL=req.getScheme() + "://"+ req.getServerName() + ":" + req.getServerPort()+ req.getContextPath() +"/CancelServlet?sessID=" +sess.getId();
                        redirectUrls.setCancelUrl(cancelURL);
                        redirectUrls.setReturnUrl(approveURL);
                        payment.setRedirectUrls(redirectUrls);
                        // Create a payment by posting to the APIService
                        // using a valid AccessToken
                        // The return object contains the status;
                        try {
                                Payment createdPayment = payment.create(apiContext);
                                ArrayList<Payment> createdPayments=new ArrayList<Payment>();
                                createdPayments.add(createdPayment);
                                sess.setAttribute("createdPayments", createdPayments);
                             //   LOGGER.info("Created payment with id = "
                               //                 + createdPayment.getId() + " and status = "
                                 //               + createdPayment.getState());
                                // ###Payment Approval Url
                                Iterator<Links> links = createdPayment.getLinks().iterator();
                                while (links.hasNext()) {
                                        Links link = links.next();
                                        if (link.getRel().equalsIgnoreCase("approval_url")) {
                                                req.setAttribute("redirectURL", link.getHref());
                                                resp.sendRedirect(link.getHref());
                                        }
                                }
                                req.setAttribute("response", Payment.getLastResponse());
                         //       map.put(guid, createdPayment.getId());
                        } catch (PayPalRESTException e) {
                                req.setAttribute("error", e.getMessage());
                        }
                }
                req.setAttribute("request", Payment.getLastRequest());
                //req.getRequestDispatcher("response.jsp").forward(req, resp);
        }
}
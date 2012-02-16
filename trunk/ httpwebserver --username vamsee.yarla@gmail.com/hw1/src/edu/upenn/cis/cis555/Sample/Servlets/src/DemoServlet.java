package edu.upenn.cis.cis555.Sample.Servlets.src;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class DemoServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<HTML><HEAD><TITLE>Simple Servlet</TITLE></HEAD><BODY>");
		out.println("<P>Hello!</P>");
		out.println("</BODY></HTML>");		
		System.out.println("VAMSEE KRISHNA");
	//	 System.out.println(request.getAsyncContext());
         System.out.println(request.getAttributeNames().toString());
         System.out.println(request.getAuthType());
         System.out.println(request.getCharacterEncoding());
         System.out.println(request.getClass());
         System.out.println(request.getContentLength());
         System.out.println(request.getContentType());
         System.out.println(request.getContextPath());
   //      System.out.println(request.getDateHeader("Date"));
         System.out.println(request.getHeader("Host:"));
         
        
         System.out.println(request.getHeaderNames().toString());
        
         System.out.println(request.getHeaders("Host"));
         System.out.println(request.getLocalAddr());
         System.out.println(request.getLocalName());
         System.out.println(request.getLocalPort());
         System.out.println(request.getLocale());
         System.out.println("KRISH1");
      //   System.out.println(request.getLocales().toString());
         System.out.println("KRISH2");
         System.out.println(request.getMethod());
         System.out.println("KRISH3");
         System.out.println(request.getParameterNames().toString());
        // System.out.println(request.getParts().toString());
         System.out.println(request.getPathInfo());
         System.out.println(request.getPathTranslated());
         System.out.println(request.getProtocol());
         System.out.println(request.getQueryString());
         System.out.println(request.getRemoteAddr());
         System.out.println(request.getRemoteHost());
         System.out.println(request.getRemotePort());
         System.out.println(request.getRemoteUser());
         System.out.println(request.getRequestURI());
         System.out.println(request.getRequestURL().toString());
         System.out.println(request.getRequestedSessionId());
         System.out.println(request.getScheme());
         System.out.println(request.getServerName());
         System.out.println(request.getServerPort());
       //  System.out.println(request.getServletContext().toString());
         System.out.println(request.getSession().toString());
         System.out.println(request.getSession(false));
         System.out.println("VAMSEE");
         System.out.println(request.getServletPath());
		
		
	}
}

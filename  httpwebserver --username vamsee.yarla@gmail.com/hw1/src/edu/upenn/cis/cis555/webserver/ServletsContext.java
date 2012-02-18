package edu.upenn.cis.cis555.webserver;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;

import java.util.*;

/**
 * @author Nick Taylor
 */
class ServletsContext implements ServletContext {
	private HashMap<String,Object> attributes;
	private HashMap<String,String> initParams;
	private HashMap<String,HttpServlet> servlets;
	private String displayName;
	
	public ServletsContext() {
		attributes = new HashMap<String,Object>();
		initParams = new HashMap<String,String>();
	}
	
	public void StoreServlets(HashMap<String,HttpServlet> arg0)
	{
		servlets=arg0;
	}
	
	public void servername(String arg0)
	{
		displayName=arg0;
	}
	
	public Object getAttribute(String name) {
		return attributes.get(name);
	}
	
	public Enumeration getAttributeNames() {
		Set<String> keys = attributes.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}
	
	public ServletContext getContext(String name) {
		return this;
	}
	
	public String getInitParameter(String name) {
		return initParams.get(name);
	}
	
	public Enumeration getInitParameterNames() {
		Set<String> keys = initParams.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}
	
	public int getMajorVersion() {
		return 2;
	}
	
	public String getMimeType(String file) {
		return null;
	}
	
	public int getMinorVersion() {
		return 4;
	}
	
	public RequestDispatcher getNamedDispatcher(String name) {
		return null;
	}
	
	public String getRealPath(String path) {
		
		String xpath="http://localhost:".concat(HttpServer.sport);
		if(xpath.charAt(0)!='/')
		{
			return xpath.concat("/").concat(path);
		}
		else
		{
			return xpath.concat(path);
		}
	}
	
	public RequestDispatcher getRequestDispatcher(String name) {
		return null;
	}
	
	public java.net.URL getResource(String path) {
		return null;
	}
	
	public java.io.InputStream getResourceAsStream(String path) {
		return null;
	}
	
	public java.util.Set getResourcePaths(String path) {
		return null;
	}
	
	public String getServerInfo() {
		return "HTTPServer";
	}
	
	public Servlet getServlet(String name) {
		return servlets.get(name);
	}
	
	public String getServletContextName() {
		//TODO
		return "/";
	}
	
	public Enumeration getServletNames() {
		
	
		Vector<String> e=new Vector<String>();
		for (String servletName : servlets.keySet())
		{
			e.add(servletName);
		}
	    return e.elements();
		
	}
	
	public Enumeration getServlets() {
		
		Vector<HttpServlet> e=new Vector<HttpServlet>();
		for (String servletName : servlets.keySet())
		{
			e.add(servlets.get(servletName));
		}
	    return e.elements();
	}
	
	public void log(Exception exception, String msg) {
		log(msg, (Throwable) exception);
	}
	
	public void log(String msg) {
		System.err.println(msg);
	}
	
	public void log(String message, Throwable throwable) {
		System.err.println(message);
		throwable.printStackTrace(System.err);
	}
	
	public void removeAttribute(String name) {
		attributes.remove(name);
	}
	
	public void setAttribute(String name, Object object) {
		attributes.put(name, object);
	}
	
	void setInitParam(String name, String value) {
		initParams.put(name, value);
	}
}

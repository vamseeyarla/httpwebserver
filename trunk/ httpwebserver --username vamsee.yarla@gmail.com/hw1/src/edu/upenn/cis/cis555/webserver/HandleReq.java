/*


 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upenn.cis.cis555.webserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.activation.MimetypesFileTypeMap;



/*
/**
 *
 * @author VamseeKYarlagadda
 */

public class HandleReq {
    Socket client;
    String met;

    String modDate=null;
	int modifiedDate=0;

    public void parseReq(Socket req)
    {
       String mainHeader=null;
       String tempHeader=null;
       client=req;
        int i=0;
        
        Scanner br=null;
        int pointer=8;
      
         try {
			Thread.currentThread().sleep(15);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
		//	e2.printStackTrace();
		}
        
        
        try {
       
           br=new Scanner(req.getInputStream());
           String temperHeader = null;
           mainHeader=null;
      	   temperHeader=br.nextLine();
      	   mainHeader=temperHeader;
           try
          {

        	   String temp;
        	   int tracker=0;
        	   
        	  
        	   while((temp=br.nextLine()).equalsIgnoreCase("")==false)
       		{
       			if(temp.indexOf("Host:")!=-1 || temp.indexOf("host:")!=-1)
       			{
       				tracker=1;
       			}
       			if((temp.indexOf("If-Modified-Since: ")!=-1 || temp.indexOf("If-Unmodified-Since: ")!=-1) && mainHeader.indexOf("GET ")!=-1)
       			{
       				
       				if(temp.indexOf("If-Modified-Since: ")!=-1)
       				{
       					modifiedDate=1;
       					modDate=temp.substring(19,temp.length()-4);
       					modDate.trim();
       					
       				}
       				else
       				{
       					modifiedDate=2;
       					modDate=temp.substring(21,temp.length()-4);
       					modDate.trim();
       				}
       				
       			}
       		}
       		
        	   if(mainHeader!=null)
        	   {
        		   pointer=headerErrorFn(temperHeader,tracker);
        	   }
        	 
          }
           catch(Exception e1)
           {
        //   e1.printStackTrace();
           }
         
       if(mainHeader!=null && pointer==2){
    	   
       execReq(mainHeader);
       }
               
       else if(pointer==8)
       {
    	  BufferedWriter bw=null;
           try{
        	   	bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        	   	bw.write("HTTP/1.0 "+"400 BAD REQUEST\n");
        	   	
        	   	Date headDate =new Date(System.currentTimeMillis());
        	   	DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        	   	String date=headformatter.format(headDate).concat(" GMT");
        	   	bw.write("Date: "+date+"\n");
        	   	
        	   	bw.write("Server: HTTPServer\n");
        	   	bw.write("Connection: close\n");
        	   	bw.write("\n");
           }
           catch(Exception e)
           { 
        	//   e.printStackTrace();
           }
           finally
           {
        	   bw.flush();
        	   bw.close();
           }
       }
     
  
         } catch (IOException ex) {
             try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
			}
   		//  ex.printStackTrace();
        }
         finally
         {
        	 try {
        
				br.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
			}
         }
        
       
    }
    
    public void execReq(String header)
    {
        String method=null,file = null,htmlVersion = null;
      try{
    	  	method=header.substring(0, header.indexOf(" "));
    	  	int last=header.indexOf(" ");
    	  	file=header.substring(last+1, header.indexOf(" ", last+1));
    	  	last=header.indexOf(" ", last+1);
    	  	htmlVersion=header.substring(last+1);
    	  	met=method;
    	  	ThreadPool.status.set(Integer.parseInt(Thread.currentThread().getName().substring(7,Thread.currentThread().getName().length())),file );
      	}
      catch(Exception e)
      {

		//  e.printStackTrace();
          htmlVersion=file;
          file="NOPE";
      }
      if(file.equalsIgnoreCase("/favicon.ico"))
      {
           try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
      }
      else if(file.equalsIgnoreCase("/shutdown"))
       {
    	  BufferedWriter bw=null;
           try{
        	   		bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        	   
        	   		if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
        	   		{
        	   			bw.write(htmlVersion+" "+"100 Continue\n");
        	   			bw.write("\n");
        	   		}
        	   		       	   		
        	   		bw.write(htmlVersion+" "+"200 OK\n");
        	 
        	   	  	Date headDate =new Date(System.currentTimeMillis());
            	             	   	
            	   	DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            	   	String date=headformatter.format(headDate).concat(" GMT");
            	   	
            	   	bw.write("Date: "+date+"\n");  		
        	   		
            	   	bw.write("Content-Type: text/html\n");
        	   		bw.write("Content-Length: 1354\n");
        	   		bw.write("Server: HTTPServer\n");
        	   		bw.write("Connection: close\n");
        	   		bw.write("\n");
       
        	   		if(met.equalsIgnoreCase("GET"))
        	   			bw.write("<html><head><title>Server Shutdown</title></head><body><h1>Server is Down</h1></body></html>\n".toString());
       
        new ThreadPool().killThreads();
           }
           catch(Exception e)
           {

     //		  e.printStackTrace();
               
           }
           finally
           {
        	   
        	   try {
        		   bw.flush();
        		   bw.close();
				 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();

      	//	  e.printStackTrace();
			}
			finally
			{
				 try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
			//		e.printStackTrace();
				}
			}
             
           }
       
          
       }
       else if(file.equalsIgnoreCase("/control"))
       {
           BufferedWriter bw=null;
              try{
                  
            	  	bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            	
            		if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
        	   		{
        	   			bw.write(htmlVersion+" "+"100 Continue\n");
        	   			bw.write("\n");
        	   		}
            	  	
            	  	bw.write(htmlVersion+" "+"200 OK\n");
            	  	
            	  	Date headDate =new Date(System.currentTimeMillis());
            	  	DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            	   	String date=headformatter.format(headDate).concat(" GMT");
            	   	bw.write("Date: "+date+"\n"); 
            	  	
       				bw.write("Content-Type: text/html\n");
       				bw.write("Content-Length: 5354\n");
       				bw.write("Server: HTTPServer\n");
       				bw.write("Connection: close\n");
       				bw.write("\n");
    
       				if(met.equalsIgnoreCase("GET")){
       					bw.write("<html><head><title>Server Report</title></head>");
       
       					bw.write("<body><h1>             Server Report</h1>");
       					bw.write("</br> </br> <h3> Vamsee K. Yarlagadda    (vamsee)</h3></br></br>");
       					bw.write("<h2>Thread Statistics</h2></br></br>");
       					
       					for(int i=0;i<ThreadPool.threadCount;i++)
       					{
       						int j=i+1;
       						bw.write(j+")     ");
       						bw.write("Name-> "+ThreadPool.threadClientReqHandle[i].getName()+"    ");
       						if(ThreadPool.threadClientReqHandle[i].getState().toString().equalsIgnoreCase("WAITING"))
       						{
       							bw.write("Status-> "+ThreadPool.threadClientReqHandle[i].getState()+"    ");
       						}
       						else
       						{
       							bw.write("Status-> "+"RUNNING"+"    ");
       						}	
       						
           int ixy = 0;
           
           try{
          
               if(!ThreadPool.threadClientReqHandle[i].getState().toString().equalsIgnoreCase("WAITING"))
               {
                bw.write("                URL-> "+ThreadPool.status.get(i) +"    ");
               }
              
           
           }
           catch(Exception e)
           {
                bw.write("                URL-> "+"Currently Parsing the URL"+"    ");

      		  //e.printStackTrace();
           }
          
      
           bw.write("</br>");           
       }
       
       bw.write("<a href=\"http://localhost:"+ServerHandler.serverPort+"/shutdown\"><input type=\"button\" name=\"shutdown\" value=\"Shutdown\"></a>");
       
       
       bw.write("</br></br></body></html>");
       }
           }
           catch(Exception e)
           {
     
   //  		  e.printStackTrace();
           }
           finally
           {
        	   
        	   try {
				bw.flush();
        		   bw.close();
				 
			} catch (IOException e) {
				// TODO Auto-generated catch block

     			//e.printStackTrace();
			}
			finally
			{
				 try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
	//				e.printStackTrace();
				}
			}
             
           }
       
       }
       
       else if(file.equalsIgnoreCase("/"))
       {
             directoryfn(1,new File(HttpServer.source),htmlVersion,"/");
       
       }
      

       else if(file.equalsIgnoreCase("nope"))
       {
       BufferedWriter bw=null;
    	   try{
       bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
      
   	if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
		{
			bw.write(htmlVersion+" "+"100 Continue\n");
			bw.write("\n");
		}
       
       
       bw.write(htmlVersion+" "+"200 OK\n");
       
       Date headDate =new Date(System.currentTimeMillis());
       DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
	   	String date=headformatter.format(headDate).concat(" GMT");	
       bw.write("Date: "+date+"\n"); 
       
       bw.write("Content-Type: text/html\n");
       bw.write("Content-Length: 2354\n");
       bw.write("Server: HTTPServer\n");
       bw.write("Connection: close\n");
       bw.write("\n");
      
       if(met.equalsIgnoreCase("GET"))
       {
      
        bw.write("<html><head><title>Server Status</title></head>");
        bw.write("<body><h1>Server Report</h1>");
        bw.write("</br></br><h3> Vamsee K. Yarlagadda    (vamsee)</h3></br></br>");
        bw.write("<h3>Server Up and Running</h3>");
        bw.write("</br></br></body></html>");
      
       }
  
       }
           catch(Exception e)
           {
 //    		  e.printStackTrace();
           }
           finally
           {
        	   
        	   try {
				bw.flush();
        		   bw.close();
				 
			} catch (IOException e) {
				// TODO Auto-generated catch block
	//			e.printStackTrace();
			}
			finally
			{
				 try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
		//			e.printStackTrace();
				}
			}
             
           }
       
       
       }
       
       else
       {
    	   
    	   /* Finding whether this is Windows/Linux and changing paths according to req format  */
    	   
    	   String path=HttpServer.source;
           boolean windows=false;
           boolean linux=false;
           
          
           
           if(path.indexOf("\\")!=-1)
                 {
             //  System.out.println("windows");
               windows=true;
                 if(path.indexOf(path.length()-1)!=(int)'\\')
               {
                   path=path.concat("\\");
               }
           }
               
         
           else
           {
           //    System.out.println("linux");
               linux=true;
               if(path.charAt(path.length()-1)!='/')
               {
          
                   path=path.concat("/");
                   
               }
           }
          
           if(windows==true){
                  file=file.replace('/', '\\');
                  if(file.charAt(0)=='\\')
                  {
                      file=file.substring(1,file.length());
                  }
           }
           else
           {
               if(file.charAt(0)=='/')
                  {
                      file=file.substring(1,file.length());
                  }
           }
           
           //END OF FORMATTING PATH , FILE STRINGS & OS RELATED STUFF
           
           try{
           File reader=new File(path+file);
           
           if(reader.exists())
           {
          //      System.out.println("Entered File EXISTS");
           
        	   if(file.indexOf("..")!=-1)
        	   {
        		   fileforbiddenfn(reader, htmlVersion, file);
        	   }
           else if(reader.isDirectory())    // The path is a directory
             {
          //        System.out.println("Entered DIRECTORY");
                 directoryfn(0,reader,htmlVersion,file);
             }
             else
             {
         //         System.out.println("Entered File");
            	 filefn(reader,htmlVersion,file);
             }
           
           
           
           }
           
           else
           {
        	   notfoundfn(reader,htmlVersion,file);
           }
           
           }
           catch(Exception e)
           {
        	   try {
				client.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
		//		e1.printStackTrace();
			}
    // 		  e.printStackTrace();
        	   servererrorfn(htmlVersion);
           }
           
           
       }//end of loop   
    	   
    }//END OF FN:EXECREQ	   
    	     
    public void directoryfn(int stat,File reader, String htmlVersion, String file)
    {
    	BufferedWriter bw=null;
        try{
            
             bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
         	
             if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
	   		{
	   			bw.write(htmlVersion+" "+"100 Continue\n");
	   			bw.write("\n");
	   		}
             
             
             
             boolean http11=false;
             
             Date SysDate =new Date(reader.lastModified()); //comp
             Date userdate = new Date(reader.lastModified()); //user
             
            
             DateFormat sysformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
	     	 //sysformatter.parse();
             String sydate=sysformatter.format(SysDate);
             SysDate=sysformatter.parse(sydate);
	            
             //System.out.println(modifiedDate);
             if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
 	   		{ 
            	 http11=true;
            if(modDate!=null)
             {
            	//System.out.println("Vamsee: "+modDate);
            	
            	   try{
                      	userdate=sysformatter.parse(modDate);
                   	   }
                   	   catch(Exception ecv)
                   	   {
                   		   System.out.println("INVALID DATE");
                   		   System.out.println("FORMAT: EEE, dd MMM yyyy HH:mm:ss Z");
                   	   }
            	
            	// System.out.println("Working");
             }
             
 	   		} 
             //System.out.println(SysDate);
             //System.out.println(userdate);
             if(http11 && modifiedDate==1 && SysDate.compareTo(userdate)<0)
             {
            	 bw.write(htmlVersion+" "+"304 Not Modified\n");
 	   			 Date headDate =new Date(System.currentTimeMillis());
 	             DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
 	     	   	 String date=headformatter.format(headDate).concat(" GMT");
 	             bw.write("Date: "+date+"\n"); 
 	             bw.write("\n");
             }
             else if(http11 && modifiedDate==2 && SysDate.compareTo(userdate)>=0)
             {
            	 bw.write(htmlVersion+" "+"412 Precondition Failed\n");
            	 bw.write("\n");
             }
             
             
             else
             {
             bw.write(htmlVersion+" "+"200 OK\n");
           
             Date headDate =new Date(System.currentTimeMillis());
             DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
     	   	String date=headformatter.format(headDate).concat(" GMT");
             bw.write("Date: "+date+"\n"); 
             
             bw.write("Content-Type: text/html\n");
             bw.write("Content-Length: 16354\n");
             
             
             Date myDate=new Date(reader.lastModified());
            
            DateFormat formatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
     	   	String formattedDate=headformatter.format(myDate).concat(" GMT");
             
             bw.write("Last-Modified: "+formattedDate+"\n");
             bw.write("Server: HTTPServer\n");
             bw.write("Connection: close\n");
             bw.write("\n");
     
             if(met.equalsIgnoreCase("GET")){
            	 if(stat==0){
             bw.write("<html><head><title>Server Status</title></head><body>");
             bw.write("<h1>The path is a directory</h1></br>");
             bw.write("<h2>List of directory</h2></br>");
            	 }
            	 else
            	 {
            		 bw.write("<html><head><title>Server Home</title></head><body>");
            		 bw.write("<h1>Server Running! :)</h1></br>");
            		 bw.write("<h1>Home Directory</h1></br>");
                     bw.write("<h2>List of files in this directory..</h2></br>");
                    
            	 }
             File [] listFiles=reader.listFiles(); 
            
             for(int i=0;i<listFiles.length;i++)
             {
                 boolean r=listFiles[i].canRead();
                 boolean w=listFiles[i].canWrite();
                 boolean x=listFiles[i].canExecute();
                 
                 Date filemyDate=new Date(listFiles[i].lastModified());
                
                DateFormat fileformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
         	   	String fileformattedDate=fileformatter.format(filemyDate).concat(" GMT");
                 
                 
                 bw.write("Read: <b>"+r+"</b>  Write: <b>"+w+"</b>  Execute: <b>"+x+"</b>          "+"<b>"+"<a href=\"http://localhost:"+ServerHandler.serverPort+"/"+file+"/"+listFiles[i].getName()+"\">"+listFiles[i].getName()+"</a>"+"</b>"+"    "+"Last Modified: <b>"+fileformattedDate+"</b></br></br>");
                 
         
             }
              bw.write("</br></br></body></html>");
             
             }//HEAD/GET LOOP
             }
             
        }//try
        catch(Exception e)
        {
  		//  e.printStackTrace();
        	servererrorfn(htmlVersion);
        }
        finally
        {
     	   
     	   try {
				bw.flush();
        		   bw.close();
				 
			} catch (IOException e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
			}
			finally
			{
				 try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
			//		e.printStackTrace();
				}
			}
          
        }
        
        
    }//directory fn
    	      
    public void filefn(File reader, String htmlVersion, String file)
    {
    	if(reader.canRead())
        {
           fileexistfn(reader, htmlVersion, file);
        }
    	 else
         {
     //        System.out.println("FORBIDDEN SECTION");
             fileforbiddenfn(reader,htmlVersion, file);
         }
    }//file fn
    
    public void fileexistfn(File reader, String htmlVersion,String file)
    {
    	OutputStream bw=null;
        try{
            
             bw=(client.getOutputStream());
        
         	if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
	   		{
	   			bw.write((String.valueOf(htmlVersion)+" "+"100 Continue\n").toString().getBytes());
	   			bw.write("\n".toString().getBytes());
	   		}
         	
         	
         	

            boolean http11=false;
            
            Date SysDate =new Date(reader.lastModified()); //comp
            Date userdate = new Date(reader.lastModified()); //user
            
           
            DateFormat sysformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
	     	 //sysformatter.parse();
            String sydate=sysformatter.format(SysDate);
            SysDate=sysformatter.parse(sydate);
	            
            //System.out.println(modifiedDate);
            if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
	   		{ 
           	 http11=true;
           if(modDate!=null)
            {
       //    	System.out.println("Vamsee: "+modDate);
        	   try{
           	userdate=sysformatter.parse(modDate);
        	   }
        	   catch(Exception ecv)
        	   {
        		   System.out.println("INVALID DATE");
        		   System.out.println("FORMAT: EEE, dd MMM yyyy HH:mm:ss Z");
        	   }
           	// userdate=DateFormat.getDateInstance().parse(modDate);
          // 	 System.out.println("Working");
            }
            
	   		} 
          //System.out.println(SysDate);
          ///System.out.println(userdate);
            if(http11 && modifiedDate==1 && SysDate.compareTo(userdate)<0)
            {
           	 bw.write((String.valueOf(htmlVersion)+" "+"304 Not Modified\n").toString().getBytes());
	   			 Date headDate =new Date(System.currentTimeMillis());
	             DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
	     	   	 String date=headformatter.format(headDate).concat(" GMT");
	             bw.write(("Date: "+String.valueOf(date)+"\n").toString().getBytes()); 
	             bw.write("\n".toString().getBytes());
            }
            else if(http11 && modifiedDate==2 && SysDate.compareTo(userdate)>=0)
            {
           	 bw.write((String.valueOf(htmlVersion)+" "+"412 Precondition Failed\n").toString().getBytes());
           	 bw.write("\n".toString().getBytes());
            }
            
            else{        	
         	
             bw.write((String.valueOf(htmlVersion)+" "+"200 OK\n").toString().getBytes());
           
             Date headDate =new Date(System.currentTimeMillis());
     	   
     	   	 DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
    	   	String date=headformatter.format(headDate).concat(" GMT");
     	   	 
     	   	 bw.write(("Date: "+String.valueOf(date)+"\n").toString().getBytes()); 
             
             if(file.indexOf(".htm")!=-1 || file.indexOf(".html")!=-1)
            {
            	 bw.write(("Content-Type: text/html\n").toString().getBytes());
                 
            }
            else if(file.indexOf(".txt")!=-1)
            {
            	bw.write(("Content-Type: text/plain\n").toString().getBytes());
                
            }
            else if(new MimetypesFileTypeMap().getContentType(reader).equalsIgnoreCase("image/jpeg"))
            {
            	bw.write(("Content-Type: image/jpeg\n").toString().getBytes());
               
            }
            else if(new MimetypesFileTypeMap().getContentType(reader).equalsIgnoreCase("image/png"))
            {
            	bw.write(("Content-Type: image/png\n").toString().getBytes());
                
            }
            else{
            	bw.write(("Content-Type: "+String.valueOf(new MimetypesFileTypeMap().getContentType(reader)) +"\n").toString().getBytes());
                
            }
             
             bw.write(("Content-Length: "+String.valueOf(reader.length())+"\n").toString().getBytes());
             
             
             Date myDate=new Date(reader.lastModified());
            
             DateFormat formatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
     	    	String formattedDate=formatter.format(myDate).concat(" GMT");
             
             bw.write(("Last-Modified: "+String.valueOf(formattedDate)+"\n").toString().getBytes());
             bw.write("Connection: close\n".toString().getBytes());
             bw.write("Server: HTTPServer\n".toString().getBytes());
             bw.write(("\n").toString().getBytes());
             
           
             if(met.equalsIgnoreCase("GET")){
            
            	 int x=Integer.parseInt(String.valueOf(reader.length()+1));
                 byte Bytes[]=new byte[x];
                 
                                 
                FileInputStream read=new FileInputStream(reader);
                read.read(Bytes, 0, x-1);
            
               bw.write(Bytes);
               
                read.close();
             }
            }
        }
        catch(Exception e)
        {
  		//  e.printStackTrace();
        	servererrorfn(htmlVersion);
        }
        finally
        {
     	   
     	   try {
     		  
				bw.flush();
        		   bw.close();
				 
			} catch (IOException e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
			}
			finally
			{
				 try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
			//		e.printStackTrace();
				}
			}
          
        }
        
     }//fileexist fn
    
    public void fileforbiddenfn(File reader, String htmlVersion, String file)
    {
    	
    	BufferedWriter bw=null;
        try{
            
             bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        
         	if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
	   		{
	   			bw.write(htmlVersion+" "+"100 Continue\n");
	   			bw.write("\n");
	   		}
             
             bw.write(htmlVersion+" "+"403 Forbidden\n");
             bw.write("Content-Type: text/html\n");
             bw.write("Content-Length: 1354\n");
             
                Date headDate =new Date(System.currentTimeMillis());
                DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        	   	String date=headformatter.format(headDate).concat(" GMT");
                
                bw.write("Date: "+date+"\n"); 
     	   	
             bw.write("Server: HTTPServer\n");
             bw.write("Connection: close\n");
             bw.write("\n");
            
            if(met.equalsIgnoreCase("GET")){
            bw.write("<html><head><title>403 Forbidden</title></head><body>");
             bw.write("<h1>403 Forbidden</h1></br>");
             bw.write("<h3>The URL "+file+" is not under your privileges</h3></br>");
           bw.write("</body></html>");           
           }

        }
        catch(Exception e)
        {
  		 // e.printStackTrace();
        	servererrorfn(htmlVersion);
        }
        finally
        {
     	   
     	   try {
     		  
				bw.flush();
        		   bw.close();
				 
			} catch (IOException e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
			}
			finally
			{
				 try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
			//		e.printStackTrace();
				}
			}
          
        }
    	
    	
    	
    }//file forbidden fn
    	   
    public void notfoundfn(File reader, String htmlVersion, String file)
    {
    	 BufferedWriter bw=null;
         try{
             
              bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
             
          	if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
	   		{
	   			bw.write(htmlVersion+" "+"100 Continue\n");
	   			bw.write("\n");
	   		}
              
              bw.write(htmlVersion+" "+"404 Not Found\n");
                  bw.write("Content-Type: text/html\n");
                  bw.write("Content-Length: 1354\n");
                  
                  Date headDate =new Date(System.currentTimeMillis());
                  DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
          	   	String date=headformatter.format(headDate).concat(" GMT");
                  
                  bw.write("Date: "+date+"\n"); 
          	   	
                  bw.write("Server: HTTPServer\n");
                  bw.write("Connection: close\n");
                  bw.write("\n");
                   
       if(met.equalsIgnoreCase("GET")){
                   bw.write("<html><head><title>404 Not Found</title></head><body>");
                    bw.write("<h1>404 Not Found</h1></br>");
                    bw.write("<h3>The URL "+file+" is not found on the server</h3></br>");
                  bw.write("</body></html>");
       }
       
    }
         catch(Exception e)
         {
   		//  e.printStackTrace();
         	servererrorfn(htmlVersion);
         }
         finally
         {
      	   
      	   try {
      		  
 				bw.flush();
        		bw.close();
 				 
 			} catch (IOException e) {
 				// TODO Auto-generated catch block
 				//e.printStackTrace();
 			}
 			finally
 			{
 				 try {
 					client.close();
 				} catch (IOException e) {
 					// TODO Auto-generated catch block
 				//	e.printStackTrace();
 				}
 			}
           
         }
         
    }//notfound function close
    
    public void servererrorfn(String htmlVersion)
    	   {
    		   BufferedWriter bw=null;
    	         try{
    	             
    	              bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    	     
    	          	if(htmlVersion.equalsIgnoreCase("HTTP/1.1"))
        	   		{
        	   			bw.write(htmlVersion+" "+"100 Continue\n");
        	   			bw.write("\n");
        	   		}
    	              
    	              bw.write(htmlVersion+" "+"500 Internal Server Error\n");
                      bw.write("Content-Type: text/html\n");
                      bw.write("Content-Length: 1354\n");
                      
                      Date headDate =new Date(System.currentTimeMillis());
                      DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
              	   	String date=headformatter.format(headDate).concat(" GMT");
                      bw.write("Date: "+date+"\n"); 
              	   	
                      bw.write("Server: HTTPServer\n");
                      bw.write("Connection: close\n");
                      bw.write("\n");
                       
           if(met.equalsIgnoreCase("GET")){
                       bw.write("<html><head><title>500 Internel Server Error</title></head><body>");
                       bw.write("<h1>500 Internel Server error while displaying file/directory info</h1></br>");
                       bw.write("</body></html>");
           }
    	         }
    	         catch(Exception e)
    	         {

           		  //e.printStackTrace();
    	         }
    	         finally
    	         {
    	      	   
    	      	   try {
    	      		  
    	 				bw.flush();
    	 				bw.close();
    	 				 
    	 			} catch (IOException e) {
    	 				// TODO Auto-generated catch block
    	 				//e.printStackTrace();
    	 			}
    	 			finally
    	 			{
    	 				 try {
    	 					client.close();
    	 				} catch (IOException e) {
    	 					// TODO Auto-generated catch block
    	 					//e.printStackTrace();
    	 				}
    	 			}
    	           
    	         }
    	         
           
    	   }//server error fn
    	   	   
    public int headerErrorFn(String header,int tracker)
	   {
    	
       	
    	
    	int pointer=2;
    	int index=0,occ=0;
    	while((index=header.indexOf(" ",index+1 ))!=-1)
    	{
    		occ++;
    	}
    	if(occ!=2 || header.substring(0, 1).equalsIgnoreCase(" "))
    	{
    		header="HTTP/1.0";
    		pointer=0;
    	}
    	    	
    	else if((header.indexOf(" HTTP/1.1")==-1 && header.indexOf(" HTTP/1.0")==-1) && ((header.indexOf("GET ")!=-1) || header.indexOf("HEAD ")!=-1))
    	{
    		header="HTTP/1.0";
    		pointer=0;
    	}
    	else if ((header.indexOf("GET ")==-1) && header.indexOf("HEAD ")==-1)
    	{    	   	
    	if ((header.indexOf(" HTTP/1.0")==-1 && header.indexOf(" HTTP/1.1")!=-1))
    	{
    		header="HTTP/1.1";
    		pointer=1;
    	}
    	
    	else if (header.indexOf(" HTTP/1.0")!=-1 && header.indexOf(" HTTP/1.1")==-1)
    	{
    		header="HTTP/1.0";
    		pointer=1;
    	}
    	}
   	else if(header.indexOf(" HTTP/1.1")!=-1 && tracker==0)
    	{
    		header="HTTP/1.1";
    		pointer=0;
    	}
	   if(pointer!=2)
	   {
		   
    	
    	BufferedWriter bw=null;
	         try{
	             
	              bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
	              if(pointer==0)
	              {
	              bw.write(header+" "+"400 Bad Request\n");
	              }
	              else
	              {
	              bw.write(header+" "+"501 Not Implemented\n"); 
	              }
               bw.write("Content-Type: text/html\n");
               bw.write("Content-Length: 1354\n");
               
               Date headDate =new Date(System.currentTimeMillis());
               DateFormat headformatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
       	   	String date=headformatter.format(headDate).concat(" GMT");
               
               bw.write("Date: "+date+"\n"); 
       	   	
               bw.write("Server: HTTPServer\n");
               bw.write("Connection: close\n");
                bw.write("\n");
                
    if(met.equalsIgnoreCase("GET")){
    	if(pointer==0)
    	{
                bw.write("<html><head><title>400 Bad Request</title></head><body>");
                 bw.write("<h1>400 BAD REQUEST.. CHECK YOUR HEADERS</h1></br>");
    	}
    	else
    	{
    		bw.write("<html><head><title>501 NOT IMPLEMENTED</title></head><body>");
            bw.write("<h1>501 METHOD NOT IMPLEMENTED.. CHECK YOUR HEADERS</h1></br>");
	
    	}
               bw.write("</body></html>");
    }
	         }
	         catch(Exception e)
	         {

    		//  e.printStackTrace();
	         }
	         finally
	         {
	        	 
	        	 try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
		//			e.printStackTrace();
				}
				
	         }
	        return pointer;
	   }
	   else
		   return pointer;
    
	   }//header error fn
	   
	   

       
       
       
    
    
    
}//HandleReq class close





















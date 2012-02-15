/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upenn.cis.cis555.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VamseeKYarlagadda
 */
public class ServerHandler {
    
    public static String serverPort;
  //  public static SocketVecQueue queuestuff=new SocketVecQueue();
    public static int count=1; 
    
    public void ListenReq(String args)
    {
              ServerSocket serSoc;
            try {
                System.out.println("Starting Server");
                
               
                     int backlog=0;  //Max Waiting Requests in queue.
                     InetAddress address= InetAddress.getByName("localhost");
                     serSoc=new ServerSocket(Integer.parseInt(args),backlog,address);
                     serverPort=args;
                     System.out.println("Server Started at local host at the port: "+args);
                     
                   //   System.out.println("Open for Client Requests");
                     while(true)
                     {
                    	
                         // System.out.println(ThreadPool.threadQueueHandleStatus);
                             if(ThreadPool.threadQueueHandleStatus==true)
                             {
                                 break;
                             }
                             
                             try{
                         serSoc.setSoTimeout(2000);
                         Socket s=serSoc.accept();
                         boolean status;
                         
                        status=new SocketVecQueue().enqueue(s);
                        
                         
                         
                         if(status)
                         {
                             
                      //       System.out.println("Please Wait..You are being processed");
                         }
                         else
                         {
                             System.out.println("Too many Req's... Please try later");
                         }
                         
                             }
                             catch(Exception e)
                             {
                            	 if(ThreadPool.threadQueueHandleStatus==true)
                                 {
                                     break;
                                 }
                            	 else
                            		 continue;
                            	 
                             }
                 
                                      
                     }                    
                    
           
            } catch (UnknownHostException ex) {
                Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
            }
              catch (IOException ex) {
                Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
           }
        
    }
    
 
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upenn.cis.cis555.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author VamseeKYarlagadda
 */
public class HttpServer implements Runnable {
    
    public static String source;
    public static void main(String[] args) {
        
       	
         if(args.length==0)
         {
             System.out.println("Vamsee K. Yarlagadda   (vamsee)");
         }
        
         else if(args.length<2 || args.length>2)
        {
            System.out.println("Invalid Set of Arguments");
            System.out.println("Failed to Start HttpServer");
           System.out.println("\n");
           System.out.println("CORRECT FORMAT:  Port RootDirectory");
        }
        
        else
        {
            if((!(new File(args[1]).exists())) || (!(new File(args[1]).isDirectory())))
            {
                System.out.println("The specified path does not exist or it is not a valid directory");
            }
                 
            else if(new ThreadPool().genThreads())
            {
                source=args[1];
                System.out.println("Threads Intialized");       
                
                  try{
                    Thread.currentThread().sleep(3000);
                    System.out.println("Proceeding with creation of Queue Handler thread");
                    Thread.currentThread().sleep(500);
                    ThreadPool.threadQueueHandle=new Thread(new HttpServer(),"QueueThread."+args[0]);
                    ThreadPool.threadQueueHandle.start();
                    ThreadPool.threadQueueHandleStatus=false;        
            }
            catch(Exception e)
            {
                    System.out.println(e.toString()+". PROBLEM IN CREATION OF QUEUE THREAD");
            }
                  
            }
            else
            {
                System.out.println("System cannot have necessary resources to start the required # of threads.");
                System.out.println("Failed to start HttpServer");
                new ThreadPool().killThreads();
            }
             
           
             
            
               
        }
    
}//Main 

    @Override
    public void run() {
        
      // Thread x=Thread.currentThread();
       
        new ServerHandler().ListenReq( ThreadPool.threadQueueHandle.getName().substring(ThreadPool.threadQueueHandle.getName().indexOf(".")+1, ThreadPool.threadQueueHandle.getName().length()));
        System.out.println("QUEUE THREAD STOPPED");   
    }
    
        
}

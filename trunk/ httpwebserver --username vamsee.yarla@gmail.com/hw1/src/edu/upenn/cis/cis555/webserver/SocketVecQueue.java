/*
 * To change this template, choose Tools | Templates

 * and open the template in the editor.
 */

package edu.upenn.cis.cis555.webserver;


import java.net.Socket;
import java.util.LinkedList;
import edu.upenn.cis.cis555.webserver.HandleReq;
/**
 *
 * @author VamseeKYarlagadda
 */

public class SocketVecQueue implements Runnable {

     public static int MAX=1500;
     public static int CURRENT=0;
     static int count=0;
     public static LinkedList<Socket> socQueue=new LinkedList<Socket>();
     Socket client;  
     String met;
    

    public boolean enqueue(Socket req)
    {
    	
    	synchronized(socQueue)
    	{
         
           
           socQueue.addLast(req);
           CURRENT++;
           socQueue.notify();
           return true;
           
           
       }
        
    }
   
      
    
    @Override
    public void run() {
   
    	while(true)
       {
         Socket input = null;
  
         		long start= System.currentTimeMillis();
         
         synchronized(socQueue){ 
        	 if(socQueue.isEmpty())
           {
            	      	 
        		 	try 
                	{
                    	socQueue.wait();
                    
                     } catch (InterruptedException ex) 
                     {
                    	 break;
                     }
              
           }
        
        		 CURRENT--;
        	 input=socQueue.removeFirst();
          }// SYNCHRONIZATION BLOCK

           
           try{
        new HandleReq().parseReq(input);
           }
           catch(Exception e)
           {
      //  	   e.printStackTrace();
           }
           finally
           {
   
           }
           
               
       }
    }
    
    
    
    
}

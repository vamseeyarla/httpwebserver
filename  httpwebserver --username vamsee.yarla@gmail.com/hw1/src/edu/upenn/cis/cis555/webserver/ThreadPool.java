/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upenn.cis.cis555.webserver;

import java.util.Vector;


/**
 *
 * @author VamseeKYarlagadda
 */
public class ThreadPool {
    
    public static final int threadCount=20;
    public static Thread [] threadClientReqHandle;
    public static Thread threadQueueHandle;
    public static boolean threadQueueHandleStatus;
    public static boolean []threadClientReqHandleStatus;
    public static Vector<String> status=new Vector<String>(threadCount);
    
    public static int CURRENT=0;
    
    public boolean genThreads()
    {
        try
        {
         
            threadClientReqHandle=new Thread[threadCount];
            threadClientReqHandleStatus=new boolean[threadCount];
            
            for(int z=0;z<threadCount;z++)
            {
                status.add(z,"Not Working");
            }
         
             for(int i=0;i<threadCount;i++)
            {              
                Thread temp=new Thread(new SocketVecQueue(),"Thread:"+String.valueOf(i));
                threadClientReqHandle[i]=temp;
                CURRENT++;
                threadClientReqHandleStatus[i]=false;
                threadClientReqHandle[i].start();
                
                
            }
            return true;
        }
        catch(Exception e)
        {
            //e.printStackTrace();
            return false;
        }
                
    }
    
    public void killThreads()
    {
        System.out.println("Killing Threads...Cleaning your System..//");
        threadQueueHandleStatus=true;
        
        for(int i=0;i<CURRENT;i++)
        {
            if(threadClientReqHandle[i].getName().equalsIgnoreCase(Thread.currentThread().getName())==false)
            {
            	threadClientReqHandle[i].interrupt();
            }
                    	
        }
        threadQueueHandle.interrupt();
        Thread.currentThread().interrupt();
        
        
    }

  
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.concurrency;

import java.util.Date;

/**
 * Call 
 * Date of create: Sep 25, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0925
 */
public class TimerThread extends Thread{
    
    private Long waitTime;
    private TimerClient client;
    private Long actualTime;
    private Long finishTime;

    public TimerThread(Long waitTime, TimerClient client) {
        this.waitTime = waitTime;
        this.client = client;
        setName("Wait thread. Wait " + waitTime + " ms");
    }
    
    @Override
    public synchronized void run() {
        Date date = new Date();
        actualTime = date.getTime();
        finishTime = actualTime + waitTime;
        while(actualTime < finishTime){
            try {
                this.wait(finishTime-actualTime);
                date = new Date();
                actualTime = date.getTime();
            } catch (InterruptedException ignore) {
                
            }
        }
        client.timeLeft();
    }
    
}

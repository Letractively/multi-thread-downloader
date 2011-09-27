/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.concurrency;

/**
 * Notify class implementing this interface that time set for this timer is over.
 * Date of create: Sep 25, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0925
 */
public interface TimerClient {
    
    /**
     * notify thread that time is over and it can perform some event
     */
    public void timeLeft();
    
}

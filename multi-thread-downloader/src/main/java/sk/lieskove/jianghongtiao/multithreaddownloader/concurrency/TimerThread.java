/*
 *  Copyright (C) 2011 JiangHongTiao <jjurco.sk_gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

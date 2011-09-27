/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.persistence;


import java.sql.Timestamp;
import java.util.Comparator;

/**
 * Date of create: Sep 27, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0927
 */
public class TimestampComparatorAZ implements Comparator<Timestamp> {

    public int compare(Timestamp o1, Timestamp o2) {
        if(o1.getTime() < o2.getTime()){
            return -1;
        } else {
            return 1;
        }
    }

}

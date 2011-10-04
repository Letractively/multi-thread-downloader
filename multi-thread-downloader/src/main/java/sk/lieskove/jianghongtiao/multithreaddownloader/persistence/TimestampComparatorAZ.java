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

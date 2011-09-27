/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.persistence;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;

/**
 * Date of create: Sep 23, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0923
 */
@Entity
public class UsageStatistics implements Serializable {

    private Integer numberOfResults;
    private Timestamp finishTime;
    @Id
    private Long id;

    public UsageStatistics() {
    }

    public UsageStatistics(Integer numberOfResults, Timestamp finishTime) {
        this.numberOfResults = numberOfResults;
        this.finishTime = finishTime;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(Integer numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    @Override
    public String toString() {
        return "UsageStatistics{" + "numberOfResults=" + numberOfResults
                + ", finishTime=" + finishTime + '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

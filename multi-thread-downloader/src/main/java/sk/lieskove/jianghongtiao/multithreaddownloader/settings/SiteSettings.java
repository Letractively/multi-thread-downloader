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
package sk.lieskove.jianghongtiao.multithreaddownloader.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import sk.lieskove.jianghongtiao.multithreaddownloader.network.AuthenticateProxy;

/**
 * Date of create: Sep 19, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0919
 */
public class SiteSettings {

    private String sitePattern = "";
    private List<AuthenticateProxy> proxies = new ArrayList<AuthenticateProxy>();
    private int maxConnections = 0;
    private Integer waitMin = 0;
    private Integer waitMax = 0;
    private int connections = 0;
    private int inTime = 0;
    private long inTimeInMs = 0;
    private WaitType waitType;
    private static Logger log = Logger.getLogger(SiteSettings.class.getName());
    private boolean noProxyEnabled = false;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    public String getSitePattern() {
        return sitePattern;
    }

    public void setSitePattern(String sitePattern) {
        this.sitePattern = sitePattern;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    private void convertInTime() {
        inTimeInMs = TimeUnit.MILLISECONDS.convert(inTime, timeUnit);
    }

    public void setTimeUnit(String timeUnit) {
        if (timeUnit.toLowerCase().startsWith("millisecond")) {
            this.timeUnit = TimeUnit.MILLISECONDS;
        } else {
            if (timeUnit.toLowerCase().startsWith("second")) {
                this.timeUnit = TimeUnit.SECONDS;
            } else {
                if (timeUnit.toLowerCase().startsWith("minute")) {
                    this.timeUnit = TimeUnit.MINUTES;
                } else {
                    if (timeUnit.toLowerCase().startsWith("hour")) {
                        this.timeUnit = TimeUnit.HOURS;
                    } else {
                        if (timeUnit.toLowerCase().startsWith("day")) {
                            this.timeUnit = TimeUnit.DAYS;
                        } else {
                            this.timeUnit = TimeUnit.SECONDS;
                        }
                    }
                }
            }
        }
        convertInTime();
    }

    public long getInTimeInMs() {
        return inTimeInMs;
    }

    public void setInTimeInMs(long inTimeInMs) {
        this.inTimeInMs = inTimeInMs;
    }

    public boolean isNoProxyEnabled() {
        return noProxyEnabled;
    }

    public void setNoProxyEnabled(boolean noProxyEnabled) {
        this.noProxyEnabled = noProxyEnabled;
    }

    public int getConnections() {
        return connections;
    }

    public void setConnections(int connections) {
        this.connections = connections;
    }

    public int getInTime() {
        return inTime;
    }

    public void setInTime(int inTime) {
        this.inTime = inTime;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public List<AuthenticateProxy> getProxies() {
        return proxies;
    }

    public void setProxies(List<AuthenticateProxy> proxies) {
        this.proxies = proxies;
    }

    public Integer getWaitMax() {
        return waitMax;
    }

    public Integer getWaitMin() {
        return waitMin;
    }

    public void setWait(Integer waitMin, Integer waitMax) {
        this.waitMin = waitMin;
        this.waitMax = waitMax;
        if ((waitMin == null) && (waitMax == null)) {
            waitType = WaitType.NONE;
        } else {

            if ((waitMin == null) || (waitMax == null)) {
                waitType = WaitType.STATIC;
                if (waitMin == null) {
                    this.waitMin = waitMax;
                } else {
                    this.waitMax = waitMin;
                }
            } else {
                waitType = WaitType.RANDOM;
            }
        }

    }

    public WaitType getWaitType() {
        return waitType;
    }

    public void setWaitTimeout(String waitTimeout) {
        if ("none".equalsIgnoreCase(waitTimeout)) {
            setWait(null, null);
        } else {
            if (Pattern.matches("\\d*", waitTimeout)) {
                setWait(Integer.parseInt(waitTimeout), null);
            } else {
                if (Pattern.matches("\\d*-\\d*", waitTimeout)) {
                    String[] w = waitTimeout.split("-");
                    if (w.length == 2) {
                        setWait(Integer.parseInt(w[0]), Integer.parseInt(w[1]));
                    } else {
                        throw new NumberFormatException("wait-timeout has bad "
                                + "format in settings file. Value: "
                                + waitTimeout);
                    }
                } else {
                    log.warn("wait-timeout has bad format. Value: "
                            + waitTimeout);
                }
            }
        }
    }

    public void setMaxConnectionsPerTime(String maxConnectionsPerTime,
            String timeUnit) {
        if (Pattern.matches("\\d*/\\d*", maxConnectionsPerTime)) {
            String[] w = maxConnectionsPerTime.split("/");
            if (w.length == 2) {
                setConnections(Integer.parseInt(w[0]));
                setInTime(Integer.parseInt(w[1]));
            } else {
                throw new NumberFormatException("max-connections-per-time"
                        + "has bad format in settings file. Value: "
                        + maxConnectionsPerTime);
            }
            setTimeUnit(timeUnit);
            convertInTime();
        } else {
            log.warn("max-connections-per-time has bad format. Value: "
                    + maxConnectionsPerTime);
        }
    }

    @Override
    public String toString() {
        String proxyString = "";
        if (proxies != null) {
            for (AuthenticateProxy proxy : proxies) {
                proxyString += "\n\t\t * " + proxy.toString();
            }
        }
        return "SiteSettings:" + "maxConnections=" + maxConnections
                + ", waitMin=" + waitMin + ", waitMax=" + waitMax
                + ", connections=" + connections + ", inTime=" + inTime
                + ", waitType=" + waitType
                + "proxies: " + proxies;
    }
}

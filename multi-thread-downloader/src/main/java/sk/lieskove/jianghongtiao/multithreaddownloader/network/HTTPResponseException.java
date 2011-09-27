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

package sk.lieskove.jianghongtiao.multithreaddownloader.network;

/**
 * Date of create: May 15, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0515
 */
public class HTTPResponseException extends Exception{

    private HTTPResponseStatusCode code;

    public HTTPResponseException(Throwable cause) {
    }

    public HTTPResponseException(String message, Throwable cause) {
    }

    public HTTPResponseException(String message) {
    }

    public HTTPResponseException() {
    }

    public HTTPResponseException(HTTPResponseStatusCode code) {
        super();
        this.code = code;
    }

    public HTTPResponseException(Throwable cause, HTTPResponseStatusCode code) {
        super(cause);
        this.code = code;
    }

    public HTTPResponseException(String message, Throwable cause, HTTPResponseStatusCode code) {
        super(message, cause);
        this.code = code;
    }

    public HTTPResponseException(String message, HTTPResponseStatusCode code) {
        super(message);
        this.code = code;
    }
    
    public HTTPResponseStatusCode getCode(){
        return code;
    }

    @Override
    public String toString() {
        return super.toString()+code.toString();
    }


}

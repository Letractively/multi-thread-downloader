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

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Date of create: May 15, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0515
 */
@Entity
@Table()
public class HTTPResponseStatusCode implements Serializable{
    @Id
    @GeneratedValue
    private Long id;
    public static enum HTTP_RESPONSE_STATUS_CLASSES {
        Informational, Success, Redirection, Client_Error, Server_Error
    };
    private static final Map<Integer, String> codes = new HashMap<Integer, String>();
    private static final Map<Integer, String> descr = new HashMap<Integer, String>();
    private int code;
    private boolean informational = false, success = false, redirection = false, clientError = false, serverError = false;

    static {
//1xx Informational
        codes.put(new Integer(100), "Continue");
        descr.put(new Integer(100), "This means that the server has received the request headers, and that the client should proceed to send the request body (in the case of a request for which a body needs to be sent; for example, a POST request). If the request body is large, sending it to a server when a request has already been rejected based upon inappropriate headers is inefficient. To have a server check if the request could be accepted based on the request's headers alone, a client must send Expect: 100-continue as a header in its initial request and check if a 100 Continue status code is received in response before continuing (or receive 417 Expectation Failed and not continue).");
        codes.put(new Integer(101), "Switching ProtFocols");
        descr.put(new Integer(101), "This means the requester has asked the server to switch protocols and the server is acknowledging that it will do so.");
        codes.put(new Integer(102), "Processing (WebDAV) (RFC 2518)");
        descr.put(new Integer(102), "As a WebDAV request may contain many sub-requests involving file operations, it may take a long time to complete the request. This code indicates that the server has received and is processing the request, but no response is available yet. This prevents the client from timing out and assuming the request was lost.");

//2xx Success
        codes.put(new Integer(200), "OK");
        descr.put(new Integer(200), "Standard response for successful HTTP requests. The actual response will depend on the request method used. In a GET request, the response will contain an entity corresponding to the requested resource. In a POST request the response will contain an entity describing or containing the result of the action.");
        codes.put(new Integer(201), "Created");
        descr.put(new Integer(201), "The request has been fulfilled and resulted in a new resource being created.");
        codes.put(new Integer(202), "Accepted");
        descr.put(new Integer(202), "The request has been accepted for processing, but the processing has not been completed. The request might or might not eventually be acted upon, as it might be disallowed when processing actually takes place.");
        codes.put(new Integer(203), "Non-Authoritative Information (since HTTP/1.1)");
        descr.put(new Integer(203), "The server successfully processed the request, but is returning information that may be from another source.");
        codes.put(new Integer(204), "No Content");
        descr.put(new Integer(204), "The server successfully processed the request, but is not returning any content.");
        codes.put(new Integer(205), "Reset Content");
        descr.put(new Integer(205), "The server successfully processed the request, but is not returning any content. Unlike a 204 response, this response requires that the requester reset the document view.");
        codes.put(new Integer(206), "Partial Content");
        descr.put(new Integer(206), "The server is delivering only part of the resource due to a range header sent by the client. The range header is used by tools like wget to enable resuming of interrupted downloads, or split a download into multiple simultaneous streams.");
        codes.put(new Integer(207), "Multi-Status (WebDAV) (RFC 4918)");
        descr.put(new Integer(207), "The message body that follows is an XML message and can contain a number of separate response codes, depending on how many sub-requests were made.");

//3xx Redirection
        codes.put(new Integer(300), "Multiple Choices");
        descr.put(new Integer(300), "Indicates multiple options for the resource that the client may follow. It, for instance, could be used to present different format options for video, list files with different extensions, or word sense disambiguation.");
        codes.put(new Integer(301), "Moved Permanently");
        descr.put(new Integer(301), "This and all future requests should be directed to the given URI.");
        codes.put(new Integer(302), "Found");
        descr.put(new Integer(302), "This is the most popular redirect code[citation needed], but also an example of industrial practice contradicting the standard. HTTP/1.0 specification (RFC 1945) required the client to perform a temporary redirect (the original describing phrase was \"Moved Temporarily\"), but popular browsers implemented 302 with the functionality of a 303 See Other. Therefore, HTTP/1.1 added status codes 303 and 307 to distinguish between the two behaviours. However, the majority of Web applications and frameworks still use the 302 status code as if it were the 303.");
        codes.put(new Integer(303), "See Other (since HTTP/1.1)");
        descr.put(new Integer(303), "The response to the request can be found under another URI using a GET method. When received in response to a PUT, it should be assumed that the server has received the data and the redirect should be issued with a separate GET message.");
        codes.put(new Integer(304), "Not Modified");
        descr.put(new Integer(304), "Indicates the resource has not been modified since last requested. Typically, the HTTP client provides a header like the If-Modified-Since header to provide a time against which to compare. Utilizing this saves bandwidth and reprocessing on both the server and client, as only the header data must be sent and received in comparison to the entirety of the page being re-processed by the server, then sent again using more bandwidth of the server and client.");
        codes.put(new Integer(305), "Use Proxy (since HTTP/1.1)");
        descr.put(new Integer(305), "Many HTTP clients (such as Mozilla and Internet Explorer) do not correctly handle responses with this status code, primarily for security reasons.");
        codes.put(new Integer(306), "Switch Proxy");
        descr.put(new Integer(306), "No longer used.");
        codes.put(new Integer(307), "Temporary Redirect (since HTTP/1.1)");
        descr.put(new Integer(307), "In this occasion, the request should be repeated with another URI, but future requests can still use the original URI. In contrast to 303, the request method should not be changed when reissuing the original request. For instance, a POST request must be repeated using another POST request.");

//4xx Client Error
        codes.put(new Integer(400), "Bad Request");
        descr.put(new Integer(400), "The request cannot be fulfilled due to bad syntax.");
        codes.put(new Integer(401), "Unauthorized");
        descr.put(new Integer(401), "Similar to 403 Forbidden, but specifically for use when authentication is possible but has failed or not yet been provided. The response must include a WWW-Authenticate header field containing a challenge applicable to the requested resource. See Basic access authentication and Digest access authentication.");
        codes.put(new Integer(402), "Payment Required");
        descr.put(new Integer(402), "Reserved for future use. The original intention was that this code might be used as part of some form of digital cash or micropayment scheme, but that has not happened, and this code is not usually used. As an example of its use, however, Apple's MobileMe service generates a 402 error (\"httpStatusCode:402\" in the Mac OS X Console log) if the MobileMe account is delinquent.");
        codes.put(new Integer(403), "Forbidden");
        descr.put(new Integer(403), "The request was a legal request, but the server is refusing to respond to it. Unlike a 401 Unauthorized response, authenticating will make no difference.");
        codes.put(new Integer(404), "Not Found");
        descr.put(new Integer(404), "The requested resource could not be found but may be available again in the future. Subsequent requests by the client are permissible.");
        codes.put(new Integer(405), "Method Not Allowed");
        descr.put(new Integer(405), "A request was made of a resource using a request method not supported by that resource; for example, using GET on a form which requires data to be presented via POST, or using PUT on a read-only resource.");
        codes.put(new Integer(406), "Not Acceptable");
        descr.put(new Integer(406), "The requested resource is only capable of generating content not acceptable according to the Accept headers sent in the request.");
        codes.put(new Integer(407), "Proxy Authentication Required");
        descr.put(new Integer(407), "no description");
        codes.put(new Integer(408), "Request Timeout");
        descr.put(new Integer(408), "The server timed out waiting for the request. According to W3 HTTP specifications: \"The client did not produce a request within the time that the server was prepared to wait. The client MAY repeat the request without modifications at any later time.\"");
        codes.put(new Integer(409), "Conflict");
        descr.put(new Integer(409), "Indicates that the request could not be processed because of conflict in the request, such as an edit conflict.");
        codes.put(new Integer(410), "Gone");
        descr.put(new Integer(410), "Indicates that the resource requested is no longer available and will not be available again. This should be used when a resource has been intentionally removed; however, it is not necessary to return this code and a 404 Not Found can be issued instead. However, despite the most common status code for such a page being 404 Not Found, 410 Gone is still used by some servers, including Geocities. Upon receiving a 410 status code, the client should not request the resource again in the future. Clients such as search engines should remove the resource from their indices.");
        codes.put(new Integer(411), "Length Required");
        descr.put(new Integer(411), "The request did not specify the length of its content, which is required by the requested resource.");
        codes.put(new Integer(412), "Precondition Failed");
        descr.put(new Integer(412), "The server does not meet one of the preconditions that the requester put on the request.");
        codes.put(new Integer(413), "Request Entity Too Large");
        descr.put(new Integer(413), "The request is larger than the server is willing or able to process.");
        codes.put(new Integer(414), "Request-URI Too Long");
        descr.put(new Integer(414), "The URI provided was too long for the server to process.");
        codes.put(new Integer(415), "Unsupported Media Type");
        descr.put(new Integer(415), "The request entity has a media type which the server or resource does not support. For example the client uploads an image as image/svg+xml, but the server requires that images use a different format.");
        codes.put(new Integer(416), "Requested Range Not Satisfiable");
        descr.put(new Integer(416), "The client has asked for a portion of the file, but the server cannot supply that portion. For example, if the client asked for a part of the file that lies beyond the end of the file.");
        codes.put(new Integer(417), "Expectation Failed");
        descr.put(new Integer(417), "The server cannot meet the requirements of the Expect request-header field.");
        codes.put(new Integer(418), "I'm a teapot");
        descr.put(new Integer(418), "This code was defined as one of the traditional IETF April Fools' jokes, in RFC 2324, Hyper Text Coffee Pot Control Protocol, and is not expected to be implemented by actual HTTP servers.");
        codes.put(new Integer(422), "Unprocessable Entity (WebDAV) (RFC 4918)");
        descr.put(new Integer(422), "The request was well-formed but was unable to be followed due to semantic errors.");
        codes.put(new Integer(423), "Locked (WebDAV) (RFC 4918)");
        descr.put(new Integer(423), "The resource that is being accessed is locked");
        codes.put(new Integer(424), "Failed Dependency (WebDAV) (RFC 4918)");
        descr.put(new Integer(424), "The request failed due to failure of a previous request (e.g. a PROPPATCH).");
        codes.put(new Integer(425), "Unordered Collection (RFC 3648)");
        descr.put(new Integer(425), "Defined in drafts of \"WebDAV Advanced Collections Protocol\", but not present in \"Web Distributed Authoring and Versioning (WebDAV) Ordered Collections Protocol\".");
        codes.put(new Integer(426), "Upgrade Required (RFC 2817)");
        descr.put(new Integer(426), "The client should switch to a different protocol such as TLS/1.0.");
        codes.put(new Integer(449), "Retry With");
        descr.put(new Integer(449), "A Microsoft extension. The request should be retried after performing the appropriate action.");
        codes.put(new Integer(450), "Blocked by Windows Parental Controls");
        descr.put(new Integer(450), "A Microsoft extension. This error is given when Windows Parental Controls are turned on and are blocking access to the given webpage.");

//5xx Server Error
        codes.put(new Integer(500), "Internal Server Error");
        descr.put(new Integer(500), "A generic error message, given when no more specific message is suitable.");
        codes.put(new Integer(501), "Not Implemented");
        descr.put(new Integer(501), "The server either does not recognise the request method, or it lacks the ability to fulfill the request.");
        codes.put(new Integer(502), "Bad Gateway");
        descr.put(new Integer(502), "The server was acting as a gateway or proxy and received an invalid response from the upstream server.");
        codes.put(new Integer(503), "Service Unavailable");
        descr.put(new Integer(503), "The server is currently unavailable (because it is overloaded or down for maintenance). Generally, this is a temporary state.");
        codes.put(new Integer(504), "Gateway Timeout");
        descr.put(new Integer(504), "The server was acting as a gateway or proxy and did not receive a timely response from the upstream server.");
        codes.put(new Integer(505), "HTTP Version Not Supported");
        descr.put(new Integer(505), "The server does not support the HTTP protocol version used in the request.");
        codes.put(new Integer(506), "Variant Also Negotiates (RFC 2295)");
        descr.put(new Integer(506), "Transparent content negotiation for the request results in a circular reference.");
        codes.put(new Integer(507), "Insufficient Storage (WebDAV) (RFC 4918)");
        descr.put(new Integer(507), "no description");
        codes.put(new Integer(509), "Bandwidth Limit Exceeded (Apache bw/limited extension)");
        descr.put(new Integer(509), "This status code, while used by many servers, is not specified in any RFCs.");
        codes.put(new Integer(510), "Not Extended (RFC 2774)");
        descr.put(new Integer(510), "Further extensions to the request are required for the server to fulfill it.");
    }

    public HTTPResponseStatusCode() {
    }

    public HTTPResponseStatusCode(int code) throws ClassNotFoundException {
        setCheckCode(code);
    }

    /**
     * get error name
     * @return error name by error code
     */
    public String getName() {
        return codes.get(new Integer(this.code));
    }

    /**
     * get error description
     * @return error description by error code
     */
    public String getDescription() {
        return descr.get(new Integer(this.code));
    }

    /**
     * get error name
     * @return error name by error code
     */
    public static String getName(int code) {
        return codes.get(new Integer(code));
    }

    /**
     * get error description
     * @return error description by error code
     * @throws ClassNotFoundException is thrown when no such class with error code is found
     */
    public static String getDescription(int code) {
        return descr.get(new Integer(code));
    }

    /**
     * get class for status code
     * @return class of status code
     * @throws ClassNotFoundException
     */
    public static HTTP_RESPONSE_STATUS_CLASSES getStatusClass(int errorCode) throws ClassNotFoundException {
        if ((errorCode < 100) && (errorCode >= 600) && !(codes.containsKey(new Integer(errorCode)))) {
            throw new ClassNotFoundException("No such status class with code: " + errorCode);
        }
        return HTTP_RESPONSE_STATUS_CLASSES.values()[(errorCode / 100) - 1];
    }

    /**
     * get class for status code
     * @return class of status code
     * @throws ClassNotFoundException
     */
    public HTTP_RESPONSE_STATUS_CLASSES getStatusClass() throws ClassNotFoundException {
        if ((code < 100) && (code >= 600) && !(codes.containsKey(new Integer(code)))) {
            throw new ClassNotFoundException("No such status class with code: " + code);
        }
        return HTTP_RESPONSE_STATUS_CLASSES.values()[(code / 100) - 1];
    }

    public int getCode() {
        return code;
    }
    
    private void setCheckCode(int code) throws ClassNotFoundException{
        if(!codes.containsKey(new Integer(code))){
            throw new ClassNotFoundException("No such status class with code: " + code);
        }
        this.code = code;
        informational = ((code>=100) && code <200);
        success       = ((code>=200) && code <300);
        redirection   = ((code>=300) && code <400);
        clientError   = ((code>=400) && code <500);
        serverError   = ((code>=500) && code <600);
    }

    public void setCode(int code) throws ClassNotFoundException {
        setCheckCode(code);
    }

    public boolean isClientError() {
        return clientError;
    }

    public boolean isInformational() {
        return informational;
    }

    public boolean isRedirection() {
        return redirection;
    }

    public boolean isServerError() {
        return serverError;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "Error code " + code + ": " + codes.get(code)
                + "\n\tDesc: " + descr.get(code) + "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static void main(String[] args) {

        Integer[] keysHTTP = codes.keySet().toArray(new Integer[codes.size()]);
        Arrays.sort(keysHTTP);

        for (int i = 0; i < keysHTTP.length; i++) {
            Integer codeNumber = keysHTTP[i];
            System.out.println("                 "
                    + codes.get(codeNumber).toUpperCase().replace(' ',
                    '_').replace('-', '_') + " = ('" +
                    codes.get(codeNumber) + "', "+codeNumber+"),");
        }

    }

}

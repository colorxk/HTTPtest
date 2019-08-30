package Server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class Response {
    
    public static final String CRLF="\r\n";
    public static final String BANK=" ";
    
    private StringBuilder headerinfo;
    private StringBuilder content;
    private BufferedWriter wr;
    private int len;
    
    public Response() {
        // TODO Auto-generated constructor stub
        headerinfo=new StringBuilder();
        content=new StringBuilder();
        if(content==null)
            System.out.println("error");
        len=0;
    }
    
    public Response(OutputStream os){
        this();
        wr=new BufferedWriter(new OutputStreamWriter(os));
    }
    
    public void createHeaderinfo(int code) {
        //System.out.println("code is "+code);
        headerinfo.append("HTTP/1.1").append(BANK);
        switch (code) {
        case 200:
            headerinfo.append(code).append(BANK).append("OK");
            break;
        case 404:
            headerinfo.append(code).append(BANK).append("404 not found");
            break;
        case 500:
            headerinfo.append(code).append(BANK).append("error");
            break;
        default:
            headerinfo.append(code).append(BANK).append("error");
            break;
        }
        headerinfo.append(CRLF);
        headerinfo.append("Server:dlkkill server/0.1").append(CRLF);
        headerinfo.append("Date:").append(new Date()).append(CRLF);
        headerinfo.append("Content-type:text/html;charset=GBK").append(CRLF);
        //���ĳ��ȣ��ֽڳ���
        headerinfo.append("Content-Length:").append(len).append(CRLF);
        //���зָ���
        headerinfo.append(CRLF);
        //System.out.println(headerinfo.toString());
    }
    
    public Response println(String msg) {
        //System.out.println(msg);
        if(content==null)
            System.out.println(msg);
        content.append(msg);
        len+=msg.getBytes().length;
        return this;
    }
    
    public void pushToClient(int code) throws IOException {
        if(wr==null) {
            code=500;
        }
        createHeaderinfo(code);
        wr.write(headerinfo.toString());
        wr.write(content.toString());
        wr.flush();
    }
}

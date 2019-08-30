package Server;

import java.io.IOException;
import java.net.Socket;

/*
 * 私有属性Socket client。利用该属性，该类可以创建一个Request和一个Response，
 * 然后该类再根据请求的url，利用Webapp类（该类用于生成处理不同请求的不同的类）获得对应的类，启动该类进行处理。
 */
public class Dispatcher implements Runnable {
    private Socket client;
    private Request req;
    private Response rep;
    private int code=200;
    
    public Dispatcher(Socket client) {
        this.client=client;
        try {
            req=new Request(client.getInputStream());//实例化客户端发来的请求包
            rep=new Response(client.getOutputStream());//实例化发给客户端的返回包
        } catch (IOException e) {
            code=500;
        }
    }
    
    @Override
    public void run() {
    	System.out.println("收到URL是:"+req.getUrl());
    	System.out.println("使用的请求方式是:"+req.getMethod());
    	System.out.println("参数变量是:"+req.getFirstFaramterName()+" 变量值是:"+req.getFirstParamterValue());
    	Servlet servlet=null;
    	String paramter = req.getFirstFaramterName();
    	System.out.println("收到命令是:"+paramter);
    	switch(paramter) {
    		case "shell":
    			shellServlet c = new shellServlet();
    			servlet=c;
    			break;
    		case "diskList":
    			DocumentQuery q = new DocumentQuery();
    			servlet = q;
    			break;
    		case "directList":
    			DocumentQuery d = new DocumentQuery();
    			servlet = d;
    			break;
    		case "fileDown":
    			break;
    		case "upFile":
    			break;
    		case "remoteControl":
    			break;
    	}

        if(servlet!=null)
            servlet.service(req, rep);
        else
            code=404;
        try {
            rep.pushToClient(code);
        } catch (IOException e) {
            code=500;
        }
        try {
            rep.pushToClient(code);
        } catch (IOException e) {
            
        }
        CloseUtil.closeAll(client);//关闭所有该用户的所有连接
    }
}

package Server;

import java.io.IOException;
import java.net.Socket;

/*
 * ˽������Socket client�����ø����ԣ�������Դ���һ��Request��һ��Response��
 * Ȼ������ٸ��������url������Webapp�ࣨ�����������ɴ���ͬ����Ĳ�ͬ���ࣩ��ö�Ӧ���࣬����������д���
 */
public class Dispatcher implements Runnable {
    private Socket client;
    private Request req;
    private Response rep;
    private int code=200;
    
    public Dispatcher(Socket client) {
        this.client=client;
        try {
            req=new Request(client.getInputStream());//ʵ�����ͻ��˷����������
            rep=new Response(client.getOutputStream());//ʵ���������ͻ��˵ķ��ذ�
        } catch (IOException e) {
            code=500;
        }
    }
    
    @Override
    public void run() {
    	System.out.println("�յ�URL��:"+req.getUrl());
    	System.out.println("ʹ�õ�����ʽ��:"+req.getMethod());
    	System.out.println("����������:"+req.getFirstFaramterName()+" ����ֵ��:"+req.getFirstParamterValue());
    	Servlet servlet=null;
    	String paramter = req.getFirstFaramterName();
    	System.out.println("�յ�������:"+paramter);
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
        CloseUtil.closeAll(client);//�ر����и��û�����������
    }
}

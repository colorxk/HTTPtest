package Server;
import java.net.Socket;

//该抽象类提供多个抽象方法doGet、doPost方法等分别处理不同的请求，
//传入参数为(Request,Response)这两个参数
public abstract class Servlet {
    
    public Servlet() { 
    }
    
    /*
     * 根据不同的请求方法进行处理
     * 因为请求不同，处理也就会不同，所以处理方法用抽象方法是最好的，让子类自己实现处理方法
     */
    public void service(Request req,Response rep) {
        if(req.getMethod().equalsIgnoreCase("get")) {
            this.doGet(req, rep);
        }else {
            this.doPost(req, rep);
        }
    }
    
    public abstract void doGet(Request req,Response rep);
    
    public abstract void doPost(Request req,Response rep);
}


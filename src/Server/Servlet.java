package Server;
import java.net.Socket;

//�ó������ṩ������󷽷�doGet��doPost�����ȷֱ���ͬ������
//�������Ϊ(Request,Response)����������
public abstract class Servlet {
    
    public Servlet() { 
    }
    
    /*
     * ���ݲ�ͬ�����󷽷����д���
     * ��Ϊ����ͬ������Ҳ�ͻ᲻ͬ�����Դ������ó��󷽷�����õģ��������Լ�ʵ�ִ�����
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


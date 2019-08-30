package Server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


//������ֻ���𲻶Ͻ������ӣ������ַ�����
public class Server {
    
    private boolean flag=false;
    private ServerSocket server;
    
    public static void main(String[] args) throws IOException {
        Server myserver=new Server(8888);
        System.out.println("���������ټ���8888�˿�...");
        myserver.start();
    }
    
    public Server(int port) {
        try {
            server=new ServerSocket(port);
        } catch (IOException e) {
            this.stop();
        }
    }
    
    public void start() throws IOException {
        this.flag=true;
        this.revice(server);
    }
    
    //�ַ���(Dispatcher���ͻ������Ӻ��������̹߳���
    public void revice(ServerSocket server) throws IOException {
            while(flag) {
                Socket client=server.accept();
                System.out.println("�пͻ������ӵ�������...");
                new Thread(new Dispatcher(client)).start();
            }
    }
    
    public void stop() {
        flag=false;
        
    }
}

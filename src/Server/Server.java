package Server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


//服务器只负责不断接收连接，建立分发器。
public class Server {
    
    private boolean flag=false;
    private ServerSocket server;
    
    public static void main(String[] args) throws IOException {
        Server myserver=new Server(8888);
        System.out.println("服务器正再监听8888端口...");
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
    
    //分发器(Dispatcher，客户端连接后对其进行线程管理
    public void revice(ServerSocket server) throws IOException {
            while(flag) {
                Socket client=server.accept();
                System.out.println("有客户端连接到服务器...");
                new Thread(new Dispatcher(client)).start();
            }
    }
    
    public void stop() {
        flag=false;
        
    }
}

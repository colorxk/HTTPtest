package Server;

import java.io.File;

public class DocumentQuery extends Servlet{

	@Override
	public void doGet(Request req, Response rep) {
		// TODO Auto-generated method stub
		System.out.println("�����ļ���ѯģ��");
		String result = "";
		if(req.getFirstFaramterName().equals("diskList")) {
			result = this.getDiskList();
			rep.println(result);
		}else {
			result = this.getDirectoryList(req.getFirstParamterValue());
			rep.println(result);
		}
		System.out.println(result);
	}

	@Override
	public void doPost(Request req, Response rep) {
		// TODO Auto-generated method stub
		String result = "";
		if(req.getFirstFaramterName().equals("diskList")) {
			result = this.getDiskList();
			rep.println(result);
		}else {
			result = this.getDirectoryList(req.getFirstParamterValue());
			rep.println(result);
		}
	}

	public static String getDiskList() {
		String result = "";
		File[] roots =  File.listRoots();
		for(int i=0;i<roots.length-1;i++) {
			result += roots[i].toString().substring(0,2)+",";
		}
		result += roots[roots.length-1].toString().substring(0,2);
		//System.out.println(result);
		return result;
	}
	
	public static String getDirectoryList(String pathh) {
		String path = pathh;
		System.out.println("�ͻ��˽��յ���path: "+path);
		if(pathh.equals("E:")) {
			path += File.separator;
		}
		File file=new File(path);
		String result = "";
		 if(file.exists()){
			 if(file.isDirectory()){
				 File f[]=file.listFiles();
				 if(f!=null){
					 result = f[0].getName();
	                    for(int i=1;i<f.length;i++){
	                    	result += ","+f[i].getName();
	                    }
	                    //System.out.println("�ͻ���ִ�н��: "+result);
	                }
			 }
			 else {

				 return result;
			 }
		 }
		 else {
			 //System.out.println("�����ڸ�Ŀ¼");
			 return result;
		 }
		return result;
	}
}

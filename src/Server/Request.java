package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

//Request����������ǽ���������Ϣ��������ʽ��������Դ·����������������������
public class Request {
    private String url;
    private String method;
    private String info;//�����������ݰ�
    private Map<String,List<String>> paramterMapValues;//������չ������
	private String firstFaramterName;
    private String firstParamterValue;
    private InputStream is;
    
    public static final String CRLF="\r\n";
    public static final String BANK=" ";
    
    private Request() {
    	System.out.println("��ʼ�Կͻ��˵�Request���н���");
        url=null;
        method=null;
        is=null;
        info=null;
        paramterMapValues=new HashMap<String,List<String>>();
    }
    
    public Request(InputStream is) {
        this();
        this.is=is;
        try {
            create();
        } catch (IOException e) {
            this.is=null;
            System.out.println("Request ��������");
            return;
        }
    }
    
    public String getMethod() {
        return this.method;
    }

    public String getUrl() {
        return url;
    }
    
    public String getFirstFaramterName() {
		return firstFaramterName;
	}

	public void setFirstFaramterName(String firstFaramterName) {
		this.firstFaramterName = firstFaramterName;
	}

	public String getFirstParamterValue() {
		return firstParamterValue;
	}

	public void setFirstParamterValue(String firstParamterValue) {
		this.firstParamterValue = firstParamterValue;
	}
    
    public Map<String,List<String>> getparamterMapValues(){
    	return this.paramterMapValues;
    }
    

    //ʵ����Request��Ķ����͸ö��󣬻�ȡ�ͻ��˷�������Ϣ
    //��ֵRequest�����URL��URL�����Ĳ���
    private void create() throws IOException{
        getInfo();
        getUrlAndParamter();
    }
    
    /**
     * ����ҳ��name��ȡ��Ӧ����ֵ
     * ��paramterValues�б��ڵ�ֵ��String[]����ʽ����
     * @return String[]
     */
    public String[] getparamterValues(String name) {
        List<String> paramterValues=null;
        if((paramterValues=paramterMapValues.get(name))==null) {
            return null;
        }else {
            return paramterValues.toArray(new String[0]);
        }
    }
    
    /**
     * ����ҳ��name��ȡ����ֵ
     * @return String[]
     */
    public String getparamterValue(String name) {
        String values[]=getparamterValues(name);
        if(values==null)
            return null;
        else
            return values[0];
    }
    /**
     * �õ�������Ϣ
     * @throws IOException
     */
    private void getInfo() throws IOException {
        byte bytes[]=new byte[20480];
        int len=is.read(bytes);
        info=new String(bytes,0,len);
    }
    /**
     * ����õ�url��Դ����·�����������ֵ
     * 
     * @return
     */
    private void getUrlAndParamter(){
        String firstline=info.substring(0,info.indexOf(CRLF));//��ȡ������:(���󷽷�ͷ /url /HTTP�汾)
        String paramter="";
        this.method=firstline.substring(0,firstline.indexOf("/")).trim();//��ȡ��һ��/ǰ������ݣ�ȥ�ո�
        String tempurl=firstline.substring(firstline.indexOf("/"),firstline.indexOf("HTTP/")).trim();//���󷽷�ͷ /url /HTTP�汾
        if(this.method.equalsIgnoreCase("post")) {	//equalsIgnoreCase()���ԱȽ����ݵĴ�Сд
            this.url=tempurl;
            paramter=info.substring(info.lastIndexOf(CRLF)).trim();//�����POST�������ȡ���һ���س����л�ò���
            
            System.out.println("info: "+info+"\r\n-------------------------");
            
            System.out.println("paramter: "+paramter);
            //��
            String[] tempstr = paramter.split("=");
            if(tempstr.length==2) {
            	this.setFirstFaramterName(tempstr[0]);
            	this.setFirstParamterValue(tempstr[1]);
            }
            
        }else {
            if(tempurl.contains("?")) {//contains()�ж��Ƿ����
                //split��������Ĳ���ʵ������Ҫ����������ʽ,��ͨ�ַ������ã�?���������ַ�
                String[] urlarry=tempurl.split("\\?");
                this.url=urlarry[0];
                paramter=urlarry[1];
                
                //��
                String[] tempstr = paramter.split("=");
                if(tempstr.length==2) {
                	this.setFirstFaramterName(tempstr[0]);
                	this.setFirstParamterValue(tempstr[1]);
                }
                
            }else {
                this.url=tempurl;
            }
        }
        //��������
        parseParmter(paramter);
        return;
    }
    
    /**
     * �����������,ת���ɼ�ֵ����ʽ
     * @param str
     */
    private void parseParmter(String str) {
        if(str==null||str.equals("")||str.trim().equals(""))
            return;
        StringTokenizer st=new StringTokenizer(str,"&");// StringTokenizer(String str, String delim) ������һ���������� str �� StringTokenizer ���󣬲��ṩһ��ָ���ķָ�����
        while(st.hasMoreTokens()) {//���st�к���&
            String temp=st.nextToken();//��ȡ��һ��key��values����ָ��������
            String[] KeyAndValues=temp.split("=");
            if(KeyAndValues.length==1) {
                KeyAndValues=Arrays.copyOf(KeyAndValues,2);//����ĸ��ƣ����ظ��ƺ������,2Ϊ���Ƹ������ǰ2Ԫ��
                KeyAndValues[1]=null;
            }
            String key=KeyAndValues[0].trim();
            String value=KeyAndValues[1]==null?null:KeyAndValues[1].trim();
            //����url������key�Ͷ�Ӧ��value��ӵ�paramterMapValues������
            if(!paramterMapValues.containsKey(KeyAndValues[0])){	//*************************
                paramterMapValues.put(key,new ArrayList<String>());
            }
            paramterMapValues.get(key).add(decode(value, "gbk"));
        }
    }
    /**
     * ������ı�������
     * @param value
     * @param code
     * @return
     * ����url�в���ֵ�ı���
     */
    private String decode(String value,String code) {
        try {
            return java.net.URLDecoder.decode(value, code);
        } catch (UnsupportedEncodingException e) {
            
        }
        return null;
    }
}


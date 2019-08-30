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

//Request对象的作用是解析请求信息，将请求方式，请求资源路径，请求参数解析分离出来
public class Request {
    private String url;
    private String method;
    private String info;//整个请求数据包
    private Map<String,List<String>> paramterMapValues;//保留扩展功能用
	private String firstFaramterName;
    private String firstParamterValue;
    private InputStream is;
    
    public static final String CRLF="\r\n";
    public static final String BANK=" ";
    
    private Request() {
    	System.out.println("开始对客户端的Request进行解析");
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
            System.out.println("Request 创建错误");
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
    

    //实例化Request类的对象后就该对象，获取客户端发来的信息
    //赋值Request对象的URL和URL所带的参数
    private void create() throws IOException{
        getInfo();
        getUrlAndParamter();
    }
    
    /**
     * 根据页面name获取对应所有值
     * 将paramterValues列表内的值以String[]的形式返回
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
     * 根据页面name获取单个值
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
     * 得到请求信息
     * @throws IOException
     */
    private void getInfo() throws IOException {
        byte bytes[]=new byte[20480];
        int len=is.read(bytes);
        info=new String(bytes,0,len);
    }
    /**
     * 处理得到url资源请求路径和请求参数值
     * 
     * @return
     */
    private void getUrlAndParamter(){
        String firstline=info.substring(0,info.indexOf(CRLF));//读取请求行:(请求方法头 /url /HTTP版本)
        String paramter="";
        this.method=firstline.substring(0,firstline.indexOf("/")).trim();//提取第一个/前面的内容，去空格
        String tempurl=firstline.substring(firstline.indexOf("/"),firstline.indexOf("HTTP/")).trim();//请求方法头 /url /HTTP版本
        if(this.method.equalsIgnoreCase("post")) {	//equalsIgnoreCase()忽略比较内容的大小写
            this.url=tempurl;
            paramter=info.substring(info.lastIndexOf(CRLF)).trim();//如果是POST请求则获取最后一个回车那行获得参数
            
            System.out.println("info: "+info+"\r\n-------------------------");
            
            System.out.println("paramter: "+paramter);
            //改
            String[] tempstr = paramter.split("=");
            if(tempstr.length==2) {
            	this.setFirstFaramterName(tempstr[0]);
            	this.setFirstParamterValue(tempstr[1]);
            }
            
        }else {
            if(tempurl.contains("?")) {//contains()判断是否包含
                //split函数里面的参数实际上需要的是正则表达式,普通字符串还好，?号是特殊字符
                String[] urlarry=tempurl.split("\\?");
                this.url=urlarry[0];
                paramter=urlarry[1];
                
                //改
                String[] tempstr = paramter.split("=");
                if(tempstr.length==2) {
                	this.setFirstFaramterName(tempstr[0]);
                	this.setFirstParamterValue(tempstr[1]);
                }
                
            }else {
                this.url=tempurl;
            }
        }
        //解析参数
        parseParmter(paramter);
        return;
    }
    
    /**
     * 解析请求参数,转换成键值对形式
     * @param str
     */
    private void parseParmter(String str) {
        if(str==null||str.equals("")||str.trim().equals(""))
            return;
        StringTokenizer st=new StringTokenizer(str,"&");// StringTokenizer(String str, String delim) ：构造一个用来解析 str 的 StringTokenizer 对象，并提供一个指定的分隔符。
        while(st.hasMoreTokens()) {//如果st中含有&
            String temp=st.nextToken();//提取第一个key和values并将指针往后移
            String[] KeyAndValues=temp.split("=");
            if(KeyAndValues.length==1) {
                KeyAndValues=Arrays.copyOf(KeyAndValues,2);//数组的复制，返回复制后的数组,2为复制该数组的前2元素
                KeyAndValues[1]=null;
            }
            String key=KeyAndValues[0].trim();
            String value=KeyAndValues[1]==null?null:KeyAndValues[1].trim();
            //将该url的所有key和对应的value添加到paramterMapValues集合里
            if(!paramterMapValues.containsKey(KeyAndValues[0])){	//*************************
                paramterMapValues.put(key,new ArrayList<String>());
            }
            paramterMapValues.get(key).add(decode(value, "gbk"));
        }
    }
    /**
     * 解决中文编码问题
     * @param value
     * @param code
     * @return
     * 解码url中参数值的编码
     */
    private String decode(String value,String code) {
        try {
            return java.net.URLDecoder.decode(value, code);
        } catch (UnsupportedEncodingException e) {
            
        }
        return null;
    }
}


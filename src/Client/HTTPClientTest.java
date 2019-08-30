package Client;

public class HTTPClientTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*String url = "http://127.0.0.1:8888/index.html?shell=ipconfig";
		System.out.println(HttpClient.doGet(url));*/
		String url2 = "http://127.0.0.1:8888/index2.html";
		String parem = "shell=ipconfig";
		System.out.println(HttpClient.doPost(url2,parem));
		
	}

}

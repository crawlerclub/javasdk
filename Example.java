import java.util.HashMap;
import java.util.Map;
import club.crawler.Client;

public class Example {

	public static void main(String[] args) {
		Client api = new Client("YOUR_ACCESS_KEY", "YOUR_SECRET_KEY");
		Map<String, String> data = new HashMap<>();
		data.put("number", "13702032331");
		String url = "https://api.crawler.club/phone";
		String response = api.Request(url, data);
		System.out.println(response);
	}

}

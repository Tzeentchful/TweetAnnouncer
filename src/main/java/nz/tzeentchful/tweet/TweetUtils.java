package nz.tzeentchful.tweet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import nz.tzeentchful.tweet.objects.Status;

public class TweetUtils {


	public static Status jTweetToStatus(JSONObject json){
		return new Status(json.getJSONObject("user").getString("name"), json.getString("text"), json.getInt("id"));
	}

	public static List<Status> getUserTweets(String user, int ammount){
		List<Status> out = new ArrayList<Status>();
		try {
			JSONArray array = readJsonFromUrl("https://api.twitter.com/1/statuses/user_timeline.json?screen_name=" + user + "&count=" + ammount);
			for(int i = 0; i < array.size(); i ++){
				out.add(jTweetToStatus(array.getJSONObject(i)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return out;
	}

	public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray json = new JSONArray();
			json = (JSONArray) JSONSerializer.toJSON(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
}

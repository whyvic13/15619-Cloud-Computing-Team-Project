import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;


public class Q4Mapper {
	
	public class userObject {
		@SerializedName("id")
		private long id;
		
		public long getUserId() {
			return id;
		}

	}
	
	static class Entities{
		private ArrayList<tagWrapper> hashtags;

		public ArrayList<tagWrapper> getHashtags() {
			return hashtags;
		}

	}
	
	static class tagWrapper{
		private String text;
		public String getText() {
			return text;
		}		
	}
	
	static class RawStructure {
		private long id;
		private String created_at;
		private userObject user;
		private String text;
		private Entities entities;
		
		public RawStructure() {
			this.id = -1;
			this.created_at = null;
			this.user = null;
			this.text = null;
			this.entities = null;
		}
				
		public long getId() {
			return id;
		}

		public String getCreated_at() {
			return created_at;
		}

		public userObject getUser() {
			return user;
		}

		public String getText() {
			return text;
		}
		
		public Entities getHashTags() {
			return entities;
		}

	}
	
	
	static class OutputStructure {
		private long id;
		private long timestamp;
		private String date;
		private String text;
		private long userId;
		private ArrayList<String> hashtags;
		
		public OutputStructure(long id, String created_at, String text, long user_id, ArrayList<tagWrapper> hashtags) {
			this.id = id;		
			this.text = text;
			this.userId = user_id;
			this.hashtags = new ArrayList<String>();
			for (tagWrapper tag: hashtags) {
				this.hashtags.add(tag.getText());
			}
			try {
				SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy",Locale.UK);
				SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
				this.timestamp = df1.parse(created_at).getTime() / 1000;
				this.date = df2.format(df1.parse(created_at));
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public long getId() {
			return id;
		}
		
		public String getText() {
			return text;
		}
	
		public long getUserId() {
			return userId;
		}

		public ArrayList<String> getHashtags() {
			return hashtags;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public String getDate() {
			return date;
		}

		
	}
	
	private static final Gson GSON = new Gson();
	
	public static void main(String[] args) {

		BufferedReader reader = null;
		PrintStream writer = null;
//		BufferedWriter writer = null;
		try {
//			reader = new BufferedReader(new FileReader("testOrigin"));
			reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
//			writer = new BufferedWriter(new FileWriter("aftermapper"));
			writer = new PrintStream(System.out, true, "UTF-8");
			
			String inputLine = null;
			while ((inputLine = reader.readLine()) != null) {
				// Uses Gson to read from json object
				// load data into raw structure
				RawStructure raw = GSON.fromJson(inputLine, RawStructure.class);
				if (raw != null) {
					// load data from raw structure to output structure 
					OutputStructure output = new OutputStructure(raw.id, raw.created_at, raw.text, raw.user.getUserId(), raw.entities.getHashtags());
					for (String hashTag: output.hashtags) {
						writer.println(hashTag + " " + GSON.toJson(output));
//						writer.write(hashTag + "\t" + GSON.toJson(realTweet) + "\n");	
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

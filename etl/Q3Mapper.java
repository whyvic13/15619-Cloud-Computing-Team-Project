import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Mapper {
	
	public static class MapperStructure {
		
		public class userObject {
			@SerializedName("id")
			private long id;
			private long followers_count;
			
			public long getUserId() {
				return id;
			}
			
			public long getFollowersCount() {
				return followers_count;
			} 
		}
		@SerializedName("created_at")
		private String created_at; 
		@SerializedName("id")
	    private long tweet_id;  
		@SerializedName("text")
	    private String text;
		@SerializedName("user")
	    private userObject user;
	    //getter and setter methods  
	  
	    public String getCreatedAt() {  
	        return created_at;  
	    }  
	  
	   
	    public long getTweetId() {  
	        return tweet_id;  
	    }  
	  
	    public String getText() {
	    	return text;
	    }
	    
	    public long getUserId() {
	    	return user.getUserId();
	    }
	    
	    public long getFollowersCount() {
	    	return user.getFollowersCount();
	    }

	}
	
	public static class OutputStructure {
		
		private long tweet_id;
		private String created_at;   
	    private String text;
	    private long user_id;
	    private long followers_count;
	    
	    public OutputStructure (long t_id, String c, String t, long id, long fcount) {
	    	this.tweet_id = t_id;
	    	this.created_at = c;
	    	this.text = t;
	    	this.user_id = id;
	    	this.followers_count = fcount;
	    }

	    //getter and setter methods 
	    public void setTweetId(long t_id) {
	    	tweet_id = t_id;
	    }
	    
	    public long getTweetId() {
	    	return tweet_id;
	    }
	    
	    public void setCreatedAt(String c) {
	    	created_at = c;
	    }
	    
	    public void setText(String t) {
	    	text = t;
	    }
	    
	    public void setUserId(long id) {
	    	user_id = id;
	    }
	    
	    public String getCreatedAt() {  
	        return created_at;  
	    }  
	     
	    public long getUserId() {  
	        return user_id;  
	    }  
	  
	    public String getText() {
	    	return text;
	    }
	    
	    public long getFollowersCount() {
			return followers_count;
		}
	    

	}


	public static void main(String[] args){
		BufferedReader reader = null;
		PrintStream writer = null;
		try {

			reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
			writer = new PrintStream(System.out, true, "UTF-8");
			String line = "";
			while ((line = reader.readLine()) != null) {
				Gson gson = new Gson(); 
				MapperStructure mapJson = gson.fromJson(line, MapperStructure.class);
				OutputStructure output = new OutputStructure(mapJson.getTweetId(), mapJson.getCreatedAt(), mapJson.getText(), mapJson.getUserId(), mapJson.getFollowersCount());				
				String outputJson = gson.toJson(output);
				writer.println(mapJson.getTweetId()+" "+outputJson);					
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

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


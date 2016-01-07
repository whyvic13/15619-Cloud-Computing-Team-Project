

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
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
			
			public long getUserId() {
				return id;
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

	}
	
	public static class OutputStructure {
		
		private long tweet_id;
		private String created_at;   
	    private String text;
	    private long user_id;
	    private int score;
	    
	    public OutputStructure (long t_id, String c, String t, long id) {
	    	this.tweet_id = t_id;
	    	this.created_at = c;
	    	this.text = t;
	    	this.user_id = id;
	    	this.score = 0;
	    }

	    public OutputStructure (long t_id, String c, String t, long id, int s) {
	        this.tweet_id = t_id;
	        this.created_at = c;
	        this.text = t;
	        this.user_id = id;
	        this.score = s;
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
				SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss Z yyyy", Locale.UK);
				Date parseDate = null;
				Date limitDate = null;
				try {
					parseDate = dateFormat.parse(mapJson.getCreatedAt());
					limitDate = dateFormat.parse("Sun Apr 20 00:00:00 +0000 2014");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(parseDate.getTime() >= limitDate.getTime()){
					OutputStructure output = new OutputStructure(mapJson.getTweetId(), mapJson.getCreatedAt(), mapJson.getText(), mapJson.getUserId());				
					String outputJson = gson.toJson(output);
					writer.println(mapJson.getTweetId()+" "+outputJson);
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		

		
	}
	
}


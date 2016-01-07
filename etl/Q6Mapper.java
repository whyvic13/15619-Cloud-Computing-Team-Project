

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Mapper {
	
	public static class MapperStructure {
		
		 
		@SerializedName("id")
	    private long tweet_id;  
		@SerializedName("text")
	    private String text;

	    //getter and setter methods  

	   
	    public long getTweetId() {  
	        return tweet_id;  
	    }  
	  
	    public String getText() {
	    	return text;
	    }

	}
	
	public static class OutputStructure {
		
		private long tweet_id;
  
	    private String text;

	    
	    public OutputStructure (long t_id, String t) {
	    	this.tweet_id = t_id;

	    	this.text = t;

	    }


	    //getter and setter methods 
	    public void setTweetId(long t_id) {
	    	tweet_id = t_id;
	    }
	    
	    public long getTweetId() {
	    	return tweet_id;
	    }

	    public void setText(String t) {
	    	text = t;
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
				
				OutputStructure output = new OutputStructure(mapJson.getTweetId(), mapJson.getText());				
				String outputJson = gson.toJson(output);
				writer.println(mapJson.getTweetId()+" "+outputJson);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}
	
}


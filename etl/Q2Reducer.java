

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import com.google.gson.Gson;

public class Reducer {
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
	public class Result{
		public String text;
		public int score;
	}
	
	/*
	 * Three main data structure in the reducer
	 *  1. scoreMap: ScoreMap stores all the information extracted from afinn.txt
	 *               the key is the text and the value is the score it has;
	 *  2. banMap:   BanMap stores all the information extracted from banned.txt
	 *               the key is the banned string and the value is the string of
	 *               '*' used to combine a new word when it comes to the banned word
	 *  3. rotMap:   Used to generate the banMap
	 */
	private HashMap<String, Integer> scoreMap = new HashMap<>();
	private static HashMap<String, String> banMap = new HashMap<>();
	private HashMap<Character, Character> rotMap = new HashMap<>();
	
	private final static String SECRET_CODE = "\u50ea\u87f6\u908d";
	public void importAfinnScore() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("afinn.txt"));
		try {
		    String line = br.readLine();
		    
		    while (line != null){
		    	String[] result = line.split("\\s+");
		    	int resultLength = result.length;
		    	String key = result[0];
		    	for (int i = 1; i < resultLength - 1; i ++){
		    		key = key + " " + result[i];
		    	}
		    	scoreMap.put(key, Integer.parseInt(result[resultLength-1]));
		    	line = br.readLine();
		    }

		} finally {
		    br.close();
		}
	}
	
	public void importBan() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("banned.txt"));
		try {
		    String line = br.readLine();
		    
		    while (line != null){
		    	String result = "";
		    	for (int i = 0; i < line.length(); i++) {
					char tmp = line.charAt(i);
					if(rotMap.containsKey(tmp))
						tmp = rotMap.get(line.charAt(i));
					result += tmp;
				}
//		    	System.out.println(result);
		    	String censorValue = "";
		    	for(int i = 0; i < result.length() - 2; i++){
		    		censorValue += "*";
		    	}
		    	banMap.put(result, censorValue);
		    	line = br.readLine();
		    }

		} finally {
		    br.close();
		}		
	}
	
	
	
	
	public void initRot13(){
		int []start = {65, 97};
		for( int j = 0; j < 2; j ++){
			int base = start[j];
			for( int i = 0; i < 26; i++){
				char key = (char)(base + i);
				char value = '-';
				if(i < 13){
					value = (char)(base + i + 13);
				} else {
					value = (char)(base + i - 13);
				}
				rotMap.put(key, value);	
			}
		}			
	}
	
	public Result reduce(String text){
		String []words = text.split("[^a-zA-Z0-9]+");
		int sentenceScore = 0;
		for (int i = 0; i < words.length; i++) {
			String currentWord = words[i];
			if(scoreMap.containsKey(currentWord.toLowerCase())){
				sentenceScore += scoreMap.get(currentWord.toLowerCase());
			}
//			System.out.println(banMap.containsKey(currentWord.toLowerCase()));
//			System.out.println(currentWord.toLowerCase());
			if(banMap.containsKey(currentWord.toLowerCase())){
//				System.out.println(banMap.get(currentWord.toLowerCase()));
				String newWord = currentWord.charAt(0) 
						+ banMap.get(currentWord.toLowerCase()) 
						+ currentWord.charAt(currentWord.length() - 1);
				text = text.replaceAll("\\b" + currentWord + "\\b", newWord);
			}
		}
		
		Result reduceResult = new Result();
		reduceResult.score = sentenceScore;
		reduceResult.text = text;
		return reduceResult;		
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Reducer r = new Reducer();
		r.importAfinnScore();
		r.initRot13();
		r.importBan();

//		System.out.println(banMap);
		BufferedReader reader = null;
		PrintStream writer = null;
		try {
			reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
			writer = new PrintStream(System.out, true, "UTF-8");
			// reader = new BufferedReader(new FileReader("test"));
			String line = "";
			long currentTweetId = 0;
			long previousTweetId = 0;
			while ((line = reader.readLine()) != null) {
				Gson gson = new Gson(); 
				// System.out.println(line);
				line = line.substring(line.indexOf(" ")+1);
				OutputStructure mapJson = gson.fromJson(line, OutputStructure.class);
				currentTweetId = mapJson.getTweetId();
				if(currentTweetId == previousTweetId)
					continue;
				String text = mapJson.getText();
				text = text.replace("\n", SECRET_CODE);
				Result reduceResult = r.reduce(text);
				int sentimentScore = reduceResult.score;
				String reduceText = reduceResult.text.replace(SECRET_CODE, "\n");
				// System.out.println(mapJson.getTweetId()+":"+reduceResult.score);
				// System.out.println("the sentence is: " + reduceResult.text);
				
				OutputStructure output = new OutputStructure(mapJson.getTweetId(), mapJson.getCreatedAt(), reduceText, mapJson.getUserId(), sentimentScore);				
//				String outputJson = gson.toJson(output);
				writer.println(gson.toJson(output));
				previousTweetId = currentTweetId;
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}

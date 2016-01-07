import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gson.Gson;

public class Q4Reducer {
		
	public static class DateResult implements Comparable<DateResult>{
		private String date;
		private long count;
		private String text;
		
		public DateResult(String data, long count, String text) {
			this.date = data;
			this.count = count;
			this.text = text;
		}
		public String getDate() {
			return date;
		}

		public long getCount() {
			return count;
		}

		public String getText() {
			return text;
		}

		
		// sort the result
		// Primary key - count: Each day should be sorted by count in a descending order
		// Secondary key - date: If counts are the same, the lines should be sorted by date in an ascending order.
		@Override
		public int compareTo(DateResult o) {
			// TODO Auto-generated method stub
			if (this.count > o.count) {
				return -1;
			} else if (this.count < o.count) {
				return 1;
			} else {
				return this.date.compareTo(o.date);
			}
		}
	}
	static class OutputStructure {
		private long id;
		private long timestamp;
		private String date;
		private String text;
		private long userId;
		private ArrayList<String> hashtags;

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

	public static class Result {
		private String tag;
		private ArrayList<DateResult> results = new ArrayList<>();
		
		public Result(String tag) {
			// TODO Auto-generated constructor stub
			this.tag = tag;
		}
		
		public String getTag() {
			return tag;
		}

		public ArrayList<DateResult> getResults() {
			return results;
		}

	}
	
	
	

	
	static final Gson GSON = new Gson();

	
	public static void main(String[] args) {
		// record each date's tag counts and newest text
		HashMap<String, DateResult> hashMap = new HashMap<>();
		
		// record each date's newest text time, use this to maintain the newest text in hashMap
		HashMap<String, Long> newestTime = new HashMap<>();
		
		// record all of the user ids who use the target hashtag
		// because of the uniqueness of user id, we use hashset to store all the user ids
		HashMap<String, HashSet<Long>> userHashMap = new HashMap<>();
		
		BufferedReader reader = null;
		PrintStream writer = null;
		
		
		
		try {
			reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
//			reader = new BufferedReader(new FileReader("test"));
			writer = new PrintStream(System.out, true, "UTF-8");
			String inputLine = null;
			// Mapper data is sorted by the hashtags
			String prev_tag = "";
			String current_tag = "";
			
			// read each line
			while ((inputLine = reader.readLine()) != null) {
				String inputLine_tmp = inputLine;
				// find split position between hashtag and json object
				int split = inputLine.indexOf('\t');
				
				// get the hashtag in each line
				current_tag = inputLine.substring(0, split);
				
				// if it's the same hashtag between lines
				// then add it to the maps to record the needed counts and user_id datas
				if(current_tag.equals(prev_tag)) {
					OutputStructure tweet = GSON.fromJson(inputLine_tmp.substring(split + 1), OutputStructure.class);	
					String date = tweet.date;
					if (hashMap.containsKey(date)) {
						DateResult dr = hashMap.get(date);
						// add counts
						dr.count++;
						// add the user_id to userlist
						userHashMap.get(date).add(tweet.userId);
						// get the newest timestamp, if there is a tie, use the one with the higher alphabetical order
						if (tweet.timestamp < newestTime.get(date) || (tweet.timestamp == newestTime.get(date) && tweet.text.compareTo(dr.text) < 0)) {
							// set the newest text
							dr.text = tweet.text;
							newestTime.put(date, tweet.timestamp);
						}
					} else {
						hashMap.put(date, new DateResult(date, 1, tweet.text));
						newestTime.put(date, tweet.timestamp);
						userHashMap.put(date, new HashSet<Long>());
						userHashMap.get(date).add(tweet.userId);
					}
				}
				
				// If it's new hashtag, first check if it's existence.
				// If exists then output the result
				// Else just re-assign values of hashtag and all the maps
				else {
					
					if(!prev_tag.equals("")) {
						Result tagResult = new Result(prev_tag);
						// get each dates' result
						for (DateResult dateResult: hashMap.values()) {
							ArrayList<Long> userList = new ArrayList<>(userHashMap.get(dateResult.date));
							// sort the userlist in an ascending order
							Collections.sort(userList);
							String resultString = "";
							// output the required data format
							resultString += dateResult.date
										 + ':'
										 + dateResult.count
										 + ':';
							for (Long userId: userList) {
								resultString += userId + ',';
							}
							// delete the last ","
							resultString.substring(0, resultString.length() - 2);
							resultString += ':' + dateResult.text;
							
							// store the output format data into text field
							dateResult.text = resultString.toString();
							tagResult.results.add(dateResult);
						}
						
						// sort the result
						// Primary key - count: Each day should be sorted by count in a descending order
						// Secondary key - date: If counts are the same, the lines should be sorted by date in an ascending order.
						Collections.sort(tagResult.results);
						for (DateResult dateResult: tagResult.results) {
							writer.println(tagResult.tag + " " + GSON.toJson(dateResult, DateResult.class));
						}
					}
					
					
					prev_tag = current_tag;
					hashMap.clear();
					newestTime.clear();
					userHashMap.clear();
					
					// re initial all the maps
					OutputStructure tweet = GSON.fromJson(inputLine_tmp.substring(split + 1), OutputStructure.class);	
					String date = tweet.date;
					hashMap.put(date, new DateResult(date, 1, tweet.text));
					newestTime.put(date, tweet.timestamp);
					userHashMap.put(date, new HashSet<Long>());
					userHashMap.get(date).add(tweet.userId);
				}
				
				
				
			}
			
			// Don't forget the last line
			if (prev_tag.equals(current_tag)) {
				Result tagResult = new Result(prev_tag);
				// get each date' result
				for (DateResult dateResult: hashMap.values()) {
					ArrayList<Long> userList = new ArrayList<>(userHashMap.get(dateResult.date));
					// sort the user list in an ascending order
					Collections.sort(userList);
					String resultString = "";
					// output the required data format
					resultString += dateResult.date
								 + ':'
								 + dateResult.count
								 + ':';
					for (Long userId: userList) {
						resultString += userId += ',';
					}
					// delete the last ","
					resultString.substring(0, resultString.length() - 2);
					resultString += ':' + dateResult.text;
					
					// store the output format data into text field
					dateResult.text = resultString.toString();
					tagResult.results.add(dateResult);
				}
				
				// sort the result
				// Primary key - count: Each day should be sorted by count in a descending order
				// Secondary key - date: If counts are the same, the lines should be sorted by date in an ascending order.
				Collections.sort(tagResult.results);
				for (DateResult dateResult: tagResult.results) {
					writer.println(tagResult.tag + " " + GSON.toJson(dateResult, DateResult.class));
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
			}
		}
	}

}

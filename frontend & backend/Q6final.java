import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Iterator;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

import org.vertx.java.core.Handler;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Q6final extends Verticle {

		public class Element {
			Object lock;
			AtomicInteger flag;
			ArrayList<String> tweetid;

			public Element() {
				lock = new Object();
				flag = new AtomicInteger(1);
				tweetid = new ArrayList<>();
			}

			public void setObject(Object l) {
				lock = l;
			}
			public Object getObject() {
				return lock;
			}
			public void setAtomic(AtomicInteger ai) {
				flag = ai;
			}
			public AtomicInteger getAtomic() {
				return flag;
			}
			public void setTweetid(String t) {
				tweetid.add(t);
			}
			public ArrayList<String> getTweetid() {
				return tweetid;
			}
		}
		// We use concurrentHashmap to record the signal, tweetid and lock
		private static ConcurrentHashMap<Integer, Element> lockMap = null;
		// We use another hashmap to record each tweetid's text
		private static ConcurrentHashMap<String, String> mapText = null;

		/// JDBC driver name and database URL
		static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		static final String DB_URL_1 = "jdbc:mysql://172.31.52.42:3306/q6?useUnicode=yes&characterEncoding=UTF-8";
		static final String DB_URL_2 = "jdbc:mysql://172.31.50.120:3306/q6?useUnicode=yes&characterEncoding=UTF-8";
		// static final String SECRET_CODE = "u908du87f6u50ea";
		//  Database credentials
		static final String USER = "root";
		static final String PASS = "1234";
		static Connection conn_1 = null;
		static Connection conn_2 = null;
		//static Statement stmt = null;
		static {
			try {
				//STEP 2: Register JDBC driver
				Class.forName("com.mysql.jdbc.Driver");

				//STEP 3: Open a connection
				System.out.println("Connecting to database...");
				conn_1 = DriverManager.getConnection(DB_URL_1,USER,PASS);
				conn_2 = DriverManager.getConnection(DB_URL_2,USER,PASS);
			}catch (Exception e) {
				e.printStackTrace();
			}
			mapText = new ConcurrentHashMap<>();
			lockMap = new ConcurrentHashMap<>();
		}

		@Override
		public void start() {
		
			final RouteMatcher routeMatcher = new RouteMatcher();
			final HttpServer server = vertx.createHttpServer();

	
			routeMatcher.get("/", new Handler<HttpServerRequest>() {
				@Override
				public void handle(final HttpServerRequest req) {
					
					req.response().end("hello world"); //Do not remove this
				}
			});
	
			routeMatcher.get("/q6", new Handler<HttpServerRequest>() {
				@Override
				public void handle(final HttpServerRequest req) {
					MultiMap map = req.params();
					String tid = map.get("tid");
					final String seq = map.get("seq");
					String opt = map.get("opt");
					final String tweet_id = map.get("tweetid");
					final String tag = map.get("tag");
					final int key_id = Integer.parseInt(tid);


					//System.out.println(req.absoluteURI());

					if (opt.charAt(0) == 'a') {
						
						// if the opt is 'a', we can response with the tag immediately	
						String result = "LuckyStar,3398-9470-0419\n";
						result += tag + "\n";
						req.response().putHeader("content-type", "application/json; charset=utf-8");						
						req.response().end(result);

						// Then we start a new thread to handle the request
						Thread t = new Thread(new Runnable() {
							public void run() {
								// first use the tid to get the relevant Element, which contains the lock shared by this transaction id
								// all the tweetids in this transaction and the signal which control the flow
								lockMap.putIfAbsent(key_id, new Element());
								Element element = lockMap.get(key_id);

								// get the lock
								Object lock = element.getObject();

								synchronized (lock) {
									// get the signal number
									AtomicInteger ai = element.getAtomic();

									int seq_num = Integer.parseInt(seq);

									// use while to check whether this seq_num can be processed now
									while( seq_num > ai.intValue()) {
										try {
											lock.wait();
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}	
									}

									// if there is no key in the text hashmap, we need to query it from the database
									if (!mapText.containsKey(tweet_id)) {

										element.setTweetid(tweet_id);
										String map_text = "";
										Statement stmt = null;
										
										//retrieve message from database
										try{					
											int lastnum = tweet_id.charAt(tweet_id.length()-1)-'0';
											//STEP 4: Execute a query
											if (lastnum < 5) {
												stmt = conn_1.createStatement();
											}
											else {
												stmt = conn_2.createStatement();
											}
											
											String sql = "SELECT text FROM tweet WHERE tweet_id = " + tweet_id + ";";
											ResultSet rs = stmt.executeQuery(sql);
											//STEP 5: Extract data from result set
											while(rs.next()){
												//Retrieve by column name
												String text = rs.getString("text");
												// put the text into hashmap
												mapText.put(tweet_id, map_text+text+tag);
											}
											rs.close();						
										}catch(SQLException se){
											//Handle errors for JDBC
											se.printStackTrace();
										}catch(Exception e){
											//Handle errors for Class.forName
											e.printStackTrace();
										} finally {
											try {
												stmt.close();
											} catch (Exception e) {
												e.printStackTrace();
											}										
										}
										
									}
									
									// if current seq_num equals to the lock signal number, then it should be add by 1
									// And then notify the locked thread to check whether it fulfill the requirements

									if (seq_num == ai.intValue()) {
										ai.incrementAndGet();	
										lock.notifyAll();									
									}
									
									// if it reach the final step, we clear the referenced hashmap to clear the memory
									if (seq_num == 5) {
										ArrayList<String> tids = element.getTweetid();
										for(String tweet_id : tids) {
											//System.out.println(tweet_id);
											mapText.remove(tweet_id);
										}
										
										lockMap.remove(key_id);
									}
 
								}
															
								
							}
						});

						t.start();
						
						
					}

					else if (opt.charAt(0) == 'r') {
						

						Thread t = new Thread(new Runnable() {
							public void run() {
								// first use the tid to get the relevant Element, which contains the lock shared by this transaction id
								// all the tweetids in this transaction and the signal which control the flow
								lockMap.putIfAbsent(key_id, new Element());
								Element element = lockMap.get(key_id);
								Object lock = element.getObject();

								synchronized (lock) {

									AtomicInteger ai = element.getAtomic();

									int seq_num = Integer.parseInt(seq);

									while( seq_num > ai.intValue()) {
										try {
											lock.wait();
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}	
									}

									String map_text = "";
									// if there is tweet_id in the hashmap then we don't have to read from the database
									// and maybe it has been added a tag.
									if (mapText.containsKey(tweet_id)) {
										map_text = mapText.get(tweet_id);
									}
									else {
										Statement stmt = null;
										element.setTweetid(tweet_id);
										//retrieve message from database
										try{					
											int lastnum = tweet_id.charAt(tweet_id.length()-1)-'0';
											//STEP 4: Execute a query
											if (lastnum < 5) {
												stmt = conn_1.createStatement();
											}
											else {
												stmt = conn_2.createStatement();
											}
											String sql = "SELECT text FROM tweet WHERE tweet_id = " + tweet_id + ";";
											ResultSet rs = stmt.executeQuery(sql);
											//STEP 5: Extract data from result set
											while(rs.next()){
												//Retrieve by column name
												map_text = rs.getString("text");
												mapText.put(tweet_id, map_text);
												//map_text += text + tag;
											}
											rs.close();						
										}catch(SQLException se){
											//Handle errors for JDBC
											se.printStackTrace();
										}catch(Exception e){
											//Handle errors for Class.forName
											e.printStackTrace();
										} finally {
											try {
												stmt.close();
											} catch (Exception e) {
												e.printStackTrace();
											}										
										}
										
										
									}
									
									String result = "LuckyStar,3398-9470-0419\n";
									result += map_text + "\n";
									req.response().putHeader("content-type", "application/json; charset=utf-8");						
									req.response().end(result);

									// if current seq_num equals to the lock signal number, then it should be add by 1
									// And then notify the locked thread to check whether it fulfill the requirements
									if (seq_num == ai.intValue()) {
										ai.incrementAndGet();	
										lock.notifyAll();								
									}
									
									// if it reach the final step, we clear the referenced hashmap to clear the memory
									if (seq_num == 5) {
										ArrayList<String> tids = element.getTweetid();
										for(String tweet_id : tids) {
											//System.out.println(tweet_id);
											mapText.remove(tweet_id);
										}
										lockMap.remove(key_id);
									}							
								}
							}
						});

						t.start();
					}
					
					else {
						String result = "LuckyStar,3398-9470-0419\n0\n";
						req.response().putHeader("content-type", "application/json; charset=utf-8");						
						req.response().end(result);
					}
					

				}
			});
	
	
			server.requestHandler(routeMatcher);
			server.listen(80);
		}
}

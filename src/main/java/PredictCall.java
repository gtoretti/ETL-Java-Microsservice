package main.java;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class PredictCall {

  private static final Logger logger = LogManager.getRootLogger();
	
  public static String executePost(String targetURL, String urlParameters) {
    HttpURLConnection connection = null;

    try {
      //Create connection
      URL url = new URL(targetURL);
      byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );            
      int    postDataLength = postData.length;
      connection = (HttpURLConnection) url.openConnection();

      connection.setDoOutput(true);
      connection.setInstanceFollowRedirects( false );
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      connection.setRequestProperty( "charset", "utf-8");
      connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
      connection.setUseCaches(false);

      try ( //Send request
          DataOutputStream wr = new DataOutputStream (connection.getOutputStream())) {
          wr.write(postData);
      }

      //Get Response  
      InputStream is = connection.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
      StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
      String line;
      while ((line = rd.readLine()) != null) {
          response.append(line);
          response.append('\r');
      }
      rd.close();
      return response.toString();
    } catch (Exception e) {
    	logger.error(e);
      return null;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }    

  public static String getJSON(String AlertKey, String InstanceID, String AlertGroup, String Node) throws Exception{
    String parameters = "ALERT_KEY=" + AlertKey + "&ALERT_GROUP=" + AlertGroup + "&INSTANCE_ID=" + InstanceID +"&NODE=" + Node;
    logger.info( "GetWatsonData - getWatsonJSON: " + parameters);
	Settings settings = new Settings();
    String JSONreturn = executePost(settings.getProperty("URL_WATSON_SEARCH"), parameters);
    logger.info(JSONreturn);
    
    return JSONreturn;
  }
  
}

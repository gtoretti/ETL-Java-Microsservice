package main.java;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

public class Settings {

	Properties settings = new Properties();
	Map<String, String> env = System.getenv();
	
	public Settings() throws Exception{
		try {
			settings.load(new FileInputStream("settings.properties"));
		}catch(Exception e) {
			try{
			settings.load(Settings.class.getClassLoader().getResourceAsStream("settings.properties"));
			}catch (Exception f) {
				throw f;
			}
		}
	}

	public String getProperty(String key) {
		if (env!=null && env.get(key)!=null) {
			return env.get(key);
		}else {
			return settings.getProperty(key);
		}
	}
}

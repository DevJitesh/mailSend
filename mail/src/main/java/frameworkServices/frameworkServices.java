package frameworkServices;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class frameworkServices 
{
	public static WebDriver driver;
	public static Properties prop;
	private static InputStream input;


	public static void loadProperties() throws IOException
	{
		prop=new Properties();

		try {
			input=new FileInputStream("config.properties");
			prop.load(input);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	



	public static WebDriver launchDriver()
	{
		if(prop.getProperty("browsername").toUpperCase().contains("CHROME"))
		{
			System.setProperty("webdriver.chrome.driver", "C:\\Users\\kedar\\eclipse-workspace\\mail\\chromedriver.exe");
			ChromeDriver driver = new ChromeDriver();
			driver.get(frameworkServices.prop.getProperty("url"));
		}
		else if(prop.getProperty("browsername").toUpperCase().contains("FIREFOX"))
		{
			
		}
		
		return driver;
	}
	
	
	
}
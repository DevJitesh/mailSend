package mail.page;

import java.beans.Visibility;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFAnchor;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import frameworkServices.frameworkServices;

/**
 * Hello world!
 *
 */
public class App
{
//testProgram

	static WebDriver driver;
	 
	static InputStream input;
	static Properties prop;
	public static ExtentReports extent;
	public static ExtentTest logger;
	static File file;
	static File file1;
	static String screenshotPath;
	public static void main( String[] args ) throws IOException
	{	
		WebDriverWait wdwait = null;
		
		prop=new Properties();
		By signInBtn=By.xpath("//div[@role=\"toolbar\"]/div[1]/div[1]/a[1]");
		By userName=By.id("login-username");
		By signInButton=By.id("login-signin");
		By passwordText=By.id("login-passwd");
		By remindMELater=By.xpath("//a[contains(text(),'Remind me later')]");
		By mailButton=By.xpath("//div[@id='ybar-navigation']/div/ul/li[1]/a");
		By composeButton= By.xpath("//a[contains(text(),'Compose')]");
		By senderMail=By.xpath("//div[@class='select']/following::input[@role='combobox']");
		By subjectMail=By.xpath("//input[@data-test-id='compose-subject']");
		By fileUploader=By.xpath("//input[@type='file']");
		By sendMailButton=By.xpath("//button[@title='Send this email']");
		By mailBody=By.xpath("//div[@id='editor-container']/div[1]");
		By useAnotherAccount=By.xpath("//a[contains(text(),'Use another account')]");
		
		
		try {
			input=new FileInputStream("config.properties");
			prop.load(input);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		setupReport();
		
		logger=extent.createTest("<B><Font Color='Black'><u>Email Sent</u></Font></B>");
		System.setProperty("webdriver.chrome.driver", prop.getProperty("chromepath"));
		WebDriver driver = new ChromeDriver();
		driver.get(prop.getProperty("url"));
		driver.manage().window().maximize();


		FileInputStream fis=new FileInputStream(new File(prop.getProperty("DataFile")));

		XSSFWorkbook wb=new XSSFWorkbook(fis);
		XSSFSheet sheet=wb.getSheetAt(0);
		XSSFRow row;
		XSSFCell cell;
		for(int i=3;i<=sheet.getLastRowNum();i++)
		{
			String email=sheet.getRow(i).getCell(0).getStringCellValue();
			String password=sheet.getRow(i).getCell(1).getStringCellValue();
			

			try
			{
				
				
			    
				 takeSnapShot(driver,screenshotPath);
				driver.findElement(signInBtn).click();
				logger.info("Click on sign In");
				
				
				
				try
				{
					Thread.sleep(1000);
					driver.findElement(useAnotherAccount).click();
				}catch(Exception e) {}
		            
				driver.findElement(userName).clear();

				driver.findElement(userName).sendKeys(email);
				logger.log(Status.PASS,"UserName Entered as :"+email);

				driver.findElement(signInButton).click();

				
				
				Thread.sleep(5000);
				
				driver.findElement(passwordText).sendKeys(password);
				
				logger.log(Status.PASS,"Password Entered as :"+password);

				driver.findElement(signInButton).click();

				try
				{
					if(driver.findElement(remindMELater).isDisplayed())
					{
						driver.findElement(remindMELater).click();
					}
				}
				catch(Exception e) {}


				try
				{
					if(driver.findElement(mailButton).isDisplayed())
					{
						driver.findElement(mailButton).click();
					}
				}
				catch(Exception e) {}

				

				for(int j=1;j<=sheet.getLastRowNum();j++)
				{
					String sender=sheet.getRow(j).getCell(2).getStringCellValue();
					String filepath=prop.getProperty("attachmentFilePath");
					
					PrintWriter writer2 = new PrintWriter(filepath);
					writer2.print("");
					// other operations
					writer2.close();
					
					 FileWriter writer = new FileWriter(filepath, true);
			            writer.write(i+" Jitesh "+j);
			            writer.write("\r\n");   // write new line
			            
			            writer.close();

			            Thread.sleep(5000);
			            takeSnapShot(driver,screenshotPath);
			            
					driver.findElement(composeButton).click();
					logger.log(Status.PASS,"Click on Compose Mail");

					 Thread.sleep(5000);
					
					 
					driver.findElement(senderMail).sendKeys(sender);
					driver.findElement(senderMail).sendKeys(Keys.TAB);
					
					logger.log(Status.PASS,"Sender :"+sender);
					Thread.sleep(5000);
					driver.findElement(subjectMail).sendKeys(i+" Jitesh "+j);
					
					String mailBodyTXT;
					BufferedReader br = new BufferedReader(new FileReader(prop.getProperty("mailBody")));
					 StringBuilder sb = new StringBuilder();
				    try {
				       
				        String line = br.readLine();

				        while (line != null) {
				            sb.append(line);
				            sb.append("\n");
				            line = br.readLine();
				        }
				        mailBodyTXT= sb.toString();
				    } finally {
				        br.close();
				    }
				    
				    Thread.sleep(5000);
					driver.findElement(mailBody).sendKeys(mailBodyTXT);
					
					Thread.sleep(3000);
					driver.findElement(fileUploader).sendKeys(filepath);
					System.out.println("File Attached Successfully!!!");
					logger.log(Status.PASS,"File Attached Successfully!!!");
					Thread.sleep(3000);
					takeSnapShot(driver,screenshotPath);
					Thread.sleep(1000);
					
					driver.findElement(sendMailButton).click();
					System.out.println("Mail Sent successfully for :"+sender);
					logger.log(Status.PASS,"Mail Sent successfully for :"+sender);
					Thread.sleep(3000);
					takeSnapShot(driver,screenshotPath);
					Thread.sleep(1000);
					
				}
				
				Thread.sleep(2000);
				driver.findElement(By.xpath("//span[@role='presentation']")).click();
				
				Thread.sleep(2000);
				
				
				WebElement mainMenu = driver.findElement(By.xpath("//span[contains(text(),'Sign out')]"));

				//Instantiating Actions class
				Actions actions = new Actions(driver);

				//Hovering on main menu
				actions.moveToElement(mainMenu);
				
				actions.click().build().perform();
				// Locating the element fr
				
				System.out.println("User Logout Successfully!!!!");
				logger.log(Status.PASS,"User Logout Successfully!!!!");
				
				
				Thread.sleep(2000);
				




			}catch(Exception e)
			{
				System.out.println(e.getMessage());
				driver.quit();
			}
		}
		
		extent.flush();
		driver.quit();

	}
	public static void setupReport()
	{
		file=new File(prop.getProperty("extentReportPath")+"\\ExecutionResult\\ExtentReport_"+getCurrentDateTime());
		file.mkdir();
		ExtentHtmlReporter extentHtmlReporter=new ExtentHtmlReporter(file+"/extentReport.html");
		extent=new ExtentReports();
		extent.attachReporter(extentHtmlReporter);
		
		screenshotPath=file.getAbsolutePath();
	}
	
	public static String getCurrentDateTime()
	{
		SimpleDateFormat sfdate=new SimpleDateFormat("dd-MM-yy_HH-mm-ss");
		Date now=new Date();
		return sfdate.format(now);
	}
	
	public void webdriverWait(By locator) throws InterruptedException
	{
		Thread.sleep(1000);
	}
	
	 public static void takeSnapShot(WebDriver webdriver,String fileWithPath) throws Exception
	 {

	        //Convert web driver object to TakeScreenshot

	        TakesScreenshot scrShot =((TakesScreenshot)webdriver);

	        //Call getScreenshotAs method to create image file

	                File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);

	            //Move image file to new destination
	                fileWithPath=fileWithPath+"\\"+"SS"+getCurrentDateTime()+".png";
	                File DestFile=new File(fileWithPath);

	                //Copy file at destination

	                FileUtils.copyFile(SrcFile, DestFile);
	                logger.log(Status.PASS, "<br><a target = \"_blank\" href=\""+DestFile+"\">"+
	                "<img src=\""
	                +DestFile
	                +"\" alt=\"Screenshot not available\" height=\"775\" width=\"400\"></a>");

	    }
}

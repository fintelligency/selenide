package integration;

import com.codeborne.selenide.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.isPhantomjs;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

public class FileDownloadTest extends IntegrationTest {
  File folder = new File(Configuration.reportsFolder);
  private ChromeDriver webDriver;

  @Before
  public void setUp() {
    webDriver = new ChromeDriver();
    setWebDriver(webDriver);
    openFile("page_with_uploads.html");
  }

  @After
  public void tearDown() {
    webDriver.quit();
  }

  @Test
  public void downloadsFiles() throws IOException {
    assumeFalse(isPhantomjs()); // Why it's not working? It's magic for me...
    
    File downloadedFile = $(byText("Download me")).download();

    assertEquals("hello_world.txt", downloadedFile.getName());
    assertEquals("Hello, WinRar!", readFileToString(downloadedFile, "UTF-8"));
    assertTrue(downloadedFile.getAbsolutePath().startsWith(folder.getAbsolutePath()));
  }

  @Test
  public void downloadExternalFile() throws FileNotFoundException {
    open("http://the-internet.herokuapp.com/download");
    File video = $(By.linkText("some-file.txt")).download();
    assertEquals("some-file.txt", video.getName());
  }

  @Test(expected = FileNotFoundException.class)
  public void downloadMissingFile() throws IOException {
    $(byText("Download missing file")).download();
  }
}

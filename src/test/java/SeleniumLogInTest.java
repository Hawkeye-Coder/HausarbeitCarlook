import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;


public class SeleniumLogInTest {

    private static WebDriver driver = null;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("webdriver.gecko.driver", "/Users/Moritz/Documents/SE2/Tools/geckodriver.exe");
        driver = new FirefoxDriver();
    }

    @Test
    public void testLogin() {
        try {
            //Carlook ltd. aufrufen
            driver.get("http://localhost:8080/Carlook/#!login");

            //Seite Maximieren
            driver.manage().window().maximize();

            //Email Feld auswählen und moritz@t.de eingeben
            driver.findElement(By.xpath("//*[@id=\"gwt-uid-3\"]")).sendKeys("moritz@t.de");

            //Passwort Feld auswählen und das Passwort test eingeben
            driver.findElement(By.xpath("//*[@id=\"gwt-uid-5\"]")).sendKeys("test");

            //Einloggen klicken
            driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[5]/div/div[2]/div/div[5]/div/div[1]/div")).click();

            //Testen ob der Login erfolgreich war

           // driver.wait(1000);
            TimeUnit.SECONDS.sleep(5);
            assertEquals("http://localhost:8080/Carlook/#!profil", driver.getCurrentUrl());

        } catch (IllegalMonitorStateException | InterruptedException e) {
            System.out.println("Fehler im Test");
        }

    }

    @AfterAll
    public static void tearDownClass() {
        driver.quit();
    }
}
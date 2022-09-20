import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String url = "https://www.a101.com.tr/";
        WebDriver driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
        driver.get(url);

        // Cookie Accept
        WebElement firstResult = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@class='CybotCookiebotDialogBodyButton'])[2]")));
        firstResult.click();

        WebElement giyim_btn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@title,'GİYİM & AKSESUAR')]")));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click()", giyim_btn);

        WebElement kdn_ıc_gym_btn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),'Kadın İç Giyim')]")));
        js.executeScript("arguments[0].click()", kdn_ıc_gym_btn);

        WebElement corap_btn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),'Dizaltı Çorap')]")));
        js.executeScript("arguments[0].click()", corap_btn);

        List<WebElement> siyah_corap = driver.findElements(By.xpath("//h3[contains(text(),'Siyah')]/ancestor::a"));
        for (WebElement link : siyah_corap) {
            System.out.println(link);
            driver.get(link.getAttribute("href"));

            // check if product is black
            String product_title = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(@class,'product-name')]"))).getText();
            if (!product_title.contains("Siyah")) {
                System.out.println("Bu ürün siyah değil");
                continue;
            }

            // Find add to basket button
            WebElement add_to_basket_button = driver.findElement(By.xpath("(//button[contains(@class,'add') and contains(@class,'basket')])[1]"));
            add_to_basket_button.click();
            // Go to basket
            String basket_link = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("go-to-shop"))).getAttribute("href");

            while (true) {
                Thread.sleep(1000);
                driver.get(basket_link);
                try {
                    new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,\"basket-empty\") and @style=\"display: block;\"]")));
                } catch (Exception e) {
                    break;
                }
            }
            break;
        }

        // Click 'Sepeti Onayla'
        WebElement checkout_button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='Sepeti Onayla' and contains(@class,'checkout')]")));
        js.executeScript("arguments[0].click()", checkout_button);

        // Click 'Üye Olmadan Devam Et' button
        WebElement uye_olmadan_btn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@title,'OLMADAN')]")));
        js.executeScript("arguments[0].click()", uye_olmadan_btn);

        // detect mail input and send mail
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        WebElement mail_input = new WebDriverWait(driver, Duration.ofSeconds(3), Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='user_email']")));
        mail_input.sendKeys("asd@asda.com");

        // Detect continue button and click it
        WebElement continue_btn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@type='submit' and contains(text(),'DEVAM')]")));
        js.executeScript( "arguments[0].click()", continue_btn);

        // Add new address
        WebElement add_address_btn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@class,'address') and @title='Yeni adres oluştur']")));
        js.executeScript("arguments[0].click()", add_address_btn);

        // Address Name
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='title']"))).sendKeys("Deneme Adres İsmi");

        // Name input
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='first_name']"))).sendKeys("Deneme İsim");

        // Surname input
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='last_name']"))).sendKeys("Deneme Soyad");

        // Phone input
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='phone_number']"))).sendKeys("5393988576");

        // Select Random Province
        Select province = new Select(driver.findElement(By.xpath("//select[@name='city']")));
        Random rand = new Random();
        province.selectByIndex(rand.nextInt(province.getOptions().size()));

        // Select Random Town
        Select town = new Select(driver.findElement(By.xpath("//select[@name='township']")));
        town.selectByIndex(rand.nextInt(town.getOptions().size()));

        // Select District
        WebElement districtElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("district")));
        districtElement.click();

        Select District = new Select(driver.findElement(By.name("district")));
        int s = District.getOptions().size();
        int n = rand.nextInt(s);
        WebElement districtOption = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//select[@name='district']/option)[" + n + "]")));
        try{
            Thread.sleep(1000);
            districtOption.click();
            Thread.sleep(1000);
        }
        catch (org.openqa.selenium.StaleElementReferenceException ex){
            System.out.println(ex);
            District.selectByIndex(n);
        }

        //Address İnput
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//textarea[@name='line']"))).sendKeys("Deneme Adres");

        try{
            //PostCode Input
            new WebDriverWait(driver,Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='postcode']"))).sendKeys("48100");
        }
        catch (Exception ex){
            System.out.println(ex);
        }
        // Submit Button
        WebElement submit_btn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@class,'js-set-country')]")));
        js.executeScript("arguments[0].click()", submit_btn);

        // Select Random Cargo
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class=\"cargo\"]/div[@class=\"cargo-list\"]/ul/li")));
        List<WebElement> cargo_options =  driver.findElements(By.xpath("//div[@class=\"cargo\"]/div[@class=\"cargo-list\"]/ul/li"));

        WebElement select_cargo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class=\"cargo\"]/div[@class=\"cargo-list\"]/ul/li[" + rand.nextInt(cargo_options.size())+1 + "]/label/div[@class='check']")));
        js.executeScript("arguments[0].click()", select_cargo);

        // Save and continue
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='cargo']/button[@type='submit']"))).click();

        // Check if checkout page
        String strURL = driver.getCurrentUrl();
        if (strURL != "https://www.a101.com.tr/orders/checkout/"){
            driver.get("https://www.a101.com.tr/orders/checkout/");
        }

        // Name Surname
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@type='text' and @name='name'])[2]"))).sendKeys("Deneme İsim");

        // Card Number
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='tel' and @class='js-masterpassbin-payment-card']"))).sendKeys("4564564654646546");

        // Last Date
        Select lDate = new Select(driver.findElement(By.xpath("(//select[@name='card_month'])[2]")));
        lDate.selectByValue("5");

        // Last Year
        Select lYear = new Select(driver.findElement(By.xpath("(//select[@name='card_year'])[2]")));
        lYear.selectByValue("2023");

        // CVC
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@type='tel' and @name='card_cvv'])[2]"))).sendKeys("123");

        // CheckBox
        WebElement CheckBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='checkbox' and @id ='agrement2']")));
        js.executeScript("arguments[0].click()", CheckBox);

        // Order Complete
        WebElement order_complete_button = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='order-complete']/parent::button")));
        js.executeScript("arguments[0].click()", order_complete_button);

        driver.quit();
    }

}

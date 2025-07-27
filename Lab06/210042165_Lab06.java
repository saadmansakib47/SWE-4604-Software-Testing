import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class ToolShopTest 
{

    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "https://practicesoftwaretesting.com/";

    @BeforeEach
    public void setup() 
    {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get(baseUrl);
    }

    @AfterEach
    public void teardown() 
    {
        driver.quit();
    }

    // Login
    private void login() 
    {
        driver.findElement(By.linkText("Sign in")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys("customer@practicesoftwaretesting.com");
        driver.findElement(By.id("password")).sendKeys("welcome01");
        driver.findElement(By.id("submit-login")).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".nav-link.user"), "Jane"));
    }

    @Test
    public void testHomePageFilters() 
    {
        // Search
        WebElement searchBar = wait.until(d -> d.findElement(By.name("s")));
        searchBar.sendKeys("drill");
        searchBar.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product-item")));

        // Sort
        Select sortDropdown = new Select(driver.findElement(By.name("orderby")));
        sortDropdown.selectByVisibleText("Sort by price: low to high");

        // Filter by Category
        WebElement catCheckbox = wait.until(d -> d.findElement(By.cssSelector("input[value='Power Tools']")));
        if (!catCheckbox.isSelected())
            catCheckbox.click();

        // Filter by Brand
        WebElement brandCheckbox = driver.findElement(By.cssSelector("input[value='BrandX']"));
        if (!brandCheckbox.isSelected())
            brandCheckbox.click();

        // Price Range
        try 
        {
            WebElement maxSlider = wait
                    .until(d -> d.findElement(By.cssSelector(".price_slider .ui-slider-handle:nth-child(3)")));
            Actions actions = new Actions(driver);
            actions.dragAndDropBy(maxSlider, -50, 0).perform();

            Thread.sleep(2000);

            for (WebElement priceElement : driver.findElements(By.cssSelector(".price"))) 
            {
                String priceText = priceElement.getText().replaceAll("[^0-9.]", "");
                if (!priceText.isEmpty())
                {
                    double price = Double.parseDouble(priceText);
                    Assertions.assertTrue(price <= 50, "Product price exceeds expected maximum after slider filter.");
                }
            }
        } 
        catch (Exception e) 
        {
            Assertions.fail("Price range slider test failed: " + e.getMessage());
        }
    }


    @Test
    public void testContactPage() 
    {
        driver.findElement(By.linkText("Contact")).click();
        WebElement contactForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("form")));
        contactForm.findElement(By.name("name")).sendKeys("Jane Doe");
        contactForm.findElement(By.name("email")).sendKeys("jane@example.com");
        contactForm.findElement(By.name("message")).sendKeys("Testing contact page.");
        contactForm.submit();

        WebElement confirmation = wait.until(d -> d.findElement(By.className("alert-success")));
        Assertions.assertTrue(confirmation.getText().contains("Thanks") || confirmation.isDisplayed());
    }

    @Test
    public void testLoginPage() 
    {
        login();
    }

    @Test
    public void testProductDetailsActions() 
    {
        login();
        // Click first product
        driver.findElement(By.cssSelector(".product-item a")).click();

        // Increase Quantity
        WebElement qtyInput = wait.until(d -> d.findElement(By.name("quantity")));
        qtyInput.clear();
        qtyInput.sendKeys("2");

        // Add to Cart
        driver.findElement(By.cssSelector("button[name='add-to-cart']")).click();

        WebElement cartNotice = wait.until(d -> d.findElement(By.className("woocommerce-message")));
        Assertions.assertTrue(cartNotice.getText().contains("added to your cart"));

        // Add to Favorite
        WebElement favButton = driver.findElement(By.cssSelector(".add-to-wishlist-button"));
        favButton.click();
        WebElement favSuccess = wait.until(d -> d.findElement(By.className("yith-wcwl-wishlistaddedbrowse")));
        Assertions.assertTrue(favSuccess.isDisplayed());
    }

    @Test
    public void testFavoriteAndProfilePage() 
    {
        login();

        // Favorites
        driver.findElement(By.linkText("Wishlist")).click();
        WebElement wishlist = wait.until(d -> d.findElement(By.className("wishlist_table")));
        Assertions.assertFalse(wishlist.findElements(By.cssSelector(".product-name")).isEmpty());

        // Profile
        driver.findElement(By.linkText("My Account")).click();
        WebElement emailField = wait.until(d -> d.findElement(By.id("account_email")));
        Assertions.assertEquals("customer@practicesoftwaretesting.com", emailField.getAttribute("value"));
    }

    @Test
    public void testCheckoutWithBuyNowPayLater() 
    {
        login();

        // Add Product
        driver.findElement(By.cssSelector(".product-item a")).click();
        driver.findElement(By.cssSelector("button[name='add-to-cart']")).click();

        // Go to Checkout
        driver.findElement(By.linkText("Cart")).click();
        wait.until(d -> d.findElement(By.linkText("Proceed to checkout"))).click();

        // Select Buy Now Pay Later
        WebElement bnpl = wait.until(d -> d.findElement(By.id("payment_method_bnpl")));
        bnpl.click();

        driver.findElement(By.id("place_order")).click();

        WebElement thankYou = wait.until(d -> d.findElement(By.className("woocommerce-thankyou-order-received")));
        Assertions.assertTrue(thankYou.getText().contains("Thank you"));
    }

    @Test
    public void testCheckoutWithCreditCard() 
    {
        login();

        // Add Product
        driver.findElement(By.cssSelector(".product-item a")).click();
        driver.findElement(By.cssSelector("button[name='add-to-cart']")).click();

        // Go to Checkout
        driver.findElement(By.linkText("Cart")).click();
        wait.until(d -> d.findElement(By.linkText("Proceed to checkout"))).click();

        // Select Credit Card
        WebElement credit = wait.until(d -> d.findElement(By.id("payment_method_creditcard")));
        credit.click();

        driver.findElement(By.id("cc_number")).sendKeys("4111111111111111");
        driver.findElement(By.id("cc_expiry")).sendKeys("12/25");
        driver.findElement(By.id("cc_cvc")).sendKeys("123");
        driver.findElement(By.id("place_order")).click();

        WebElement thankYou = wait.until(d -> d.findElement(By.className("woocommerce-thankyou-order-received")));
        Assertions.assertTrue(thankYou.getText().contains("Thank you"));
    }
}

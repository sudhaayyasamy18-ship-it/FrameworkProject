package pageObjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import AbstractComponents.AbstractComponent;

public class ProductCatalogue extends AbstractComponent {

    WebDriver driver;

    public ProductCatalogue(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // All products displayed on the page
    @FindBy(css = ".mb-3")
    private List<WebElement> products;

    // Loading spinner
    @FindBy(css = ".ng-animating")
    private WebElement spinner;

    // Locators
    private By productsBy = By.cssSelector(".mb-3");
    private By addToCartButtonBy = By.cssSelector(".card-body button:last-of-type");
    private By toastMessageBy = By.cssSelector("#toast-container");

    // Return all products after ensuring they are visible
    public List<WebElement> getProductList() {
        waitForElementToAppear(productsBy);
        return products;
    }

    // Find product by name
    public WebElement getProductByName(String productName) {
        return getProductList().stream()
                .filter(product -> product.findElement(By.cssSelector("b")).getText().equalsIgnoreCase(productName))
                .findFirst()
                .orElse(null);
    }

    // Add a specific product to cart
    public void addProductToCart(String productName) throws InterruptedException {
        WebElement product = getProductByName(productName);
        if (product == null) {
            throw new RuntimeException("Product not found: " + productName);
        }

        WebElement addToCartBtn = product.findElement(addToCartButtonBy);

        // Scroll into view
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartBtn);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Wait until button is clickable
        wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn));

        // Retry click up to 3 times in case of interception
        int attempts = 0;
        while (attempts < 3) {
            try {
                addToCartBtn.click();
                break; // success
            } catch (ElementClickInterceptedException e) {
                Thread.sleep(300);
                attempts++;
            }
        }

        // Wait for toast message to appear and disappear
        wait.until(ExpectedConditions.visibilityOfElementLocated(toastMessageBy));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(toastMessageBy));

        // Wait for spinner to disappear
        waitForElementToDisappear(spinner);
        
    }
}

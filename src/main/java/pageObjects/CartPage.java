package pageObjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import AbstractComponents.AbstractComponent;

public class CartPage extends AbstractComponent {

    WebDriver driver;

    @FindBy(css = ".totalRow button")
    WebElement checkoutEle;

    @FindBy(css = ".cartSection h3")
    private List<WebElement> cartProducts;

    public CartPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Verify if a product is displayed in the cart
    public boolean verifyProductDisplay(String productName) {
        return cartProducts.stream()
                .anyMatch(product -> product.getText().equalsIgnoreCase(productName));
    }

    // Navigate to checkout safely
    public CheckOutPage goToCheckout() {
        clickElement(checkoutEle); // uses AbstractComponent safe click
        return new CheckOutPage(driver);
    }
}

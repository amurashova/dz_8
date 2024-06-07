package tests;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTests extends TestBase {

    @Tag("Auth")
    @CsvFileSource(resources = "/userdata.csv", delimiter = ',')
    @ParameterizedTest()
    @DisplayName("Открытие страницы со списком товаров для разных юзеров")

    void userAuth(String testData, String expectedURL) {
        open("https://www.saucedemo.com");
        $("[id=user-name]").setValue(testData);
        $("[id=password]").setValue("secret_sauce");
        $("[id=login-button]").click();

        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertEquals(expectedURL, currentUrl);

    }
    @Test
    @Tag("AddToCart")
    @DisplayName("Добавление товара в корзину")
    void addToCart() {
        open("https://www.saucedemo.com");
        $("[id=user-name]").setValue("standard_user");
        $("[id=password]").setValue("secret_sauce");
        $("[id=login-button]").click();
        $("[id=add-to-cart-sauce-labs-backpack]").click();

        $("[id=remove-sauce-labs-backpack]").shouldHave(text("Remove"));



    }
}

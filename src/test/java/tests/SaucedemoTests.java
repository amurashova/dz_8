package tests;

import com.codeborne.selenide.WebDriverRunner;
import com.google.common.base.Optional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SaucedemoTests extends TestBase {

    @Tag("Auth")
    @CsvFileSource(resources = "/userdata.csv", delimiter = ',')
    @ParameterizedTest(name = "Для пользователя {0} после авторизации открывается страница {1}")
    void userAuthTest(String testData, String expectedURL) {
        open("https://www.saucedemo.com");
        $("[id=user-name]").setValue(testData);
        $("[id=password]").setValue("secret_sauce");
        $("[id=login-button]").click();

        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        assertEquals(expectedURL, currentUrl);

    }

    @Tag("AddToCart")
    @ValueSource(strings = {"Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt"})
    @ParameterizedTest(name = "Название в списке товаров соответствует названию в карточке товара")
    void addToCartTest(String cardText) {
        open("https://www.saucedemo.com");
        $("[id=user-name]").setValue("standard_user");
        $("[id=password]").setValue("secret_sauce");
        $("[id=login-button]").click();

        $("#inventory_container").$(withText(cardText)).click();
        $(".inventory_details_name").shouldHave(text(cardText));

    }

    static Stream<Arguments> checkSortingTest() {

        return Stream.of(
                Arguments.of("Name (A to Z)", "Sauce Labs Backpack"),
                Arguments.of("Name (Z to A)", "Test.allTheThings() T-Shirt (Red)"),
                Arguments.of("Price (low to high)", "Sauce Labs Onesie"),
                Arguments.of("Price (high to low)", "Sauce Labs Fleece Jacket")
        );
    }

    @MethodSource("checkSortingTest")
    @ParameterizedTest
    void checkSortingTest(String sortType, String result) {
        open("https://www.saucedemo.com");
        $("[id=user-name]").setValue("standard_user");
        $("[id=password]").setValue("secret_sauce");
        $("[id=login-button]").click();

        $(".product_sort_container").selectOption(sortType);
        $(".inventory_item_name").shouldHave(text(result));
    }
}

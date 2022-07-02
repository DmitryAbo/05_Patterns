import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.netology.generatotrs.UserDataGenerator;
import ru.netology.users.UserInfo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class CallBackTest {

    @BeforeEach
    public void startBrowser() {
        open("http://localhost:9999/");
    }

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    public void shouldSuccess() {
        UserInfo userInfo = UserDataGenerator.Registration.registrationInfo("ru");
        $x("//span/input[@placeholder = 'Город']").setValue(userInfo.getCity());
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").setValue(UserDataGenerator.Date.generateDate(3));
        $x("//span[@data-test-id= 'name']//child::input[@name= 'name']").setValue(userInfo.getName());
        $x("//span[@data-test-id= 'phone']//child::input[@name= 'phone']").setValue(userInfo.getPhone());
        $x("//label[@data-test-id=\"agreement\"]").click();
        $x("//span[text()=\"Запланировать\"]/ancestor::button").click();
        $x("//div[text() = 'Встреча успешно запланирована на ']/ancestor::div[@data-test-id=\"success-notification\"]").should(visible, Duration.ofSeconds(15));
        $x("//div[text() = \"" + UserDataGenerator.Date.generateDate(3) + "\"]/ancestor::div[@data-test-id=\"success-notification\"]").should(visible, Duration.ofSeconds(15));
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").setValue(UserDataGenerator.Date.generateDate(4));
        $x("//span[text()=\"Запланировать\"]/ancestor::button").click();
        $x("//div[@data-test-id=\"replan-notification\"]").should(visible, Duration.ofSeconds(15));
        $x("//span[text() = \"Перепланировать\"]/ancestor::button").click();
        $x("//div[text() = 'Встреча успешно запланирована на ']/ancestor::div[@data-test-id=\"success-notification\"]").should(visible, Duration.ofSeconds(15));
        $x("//div[text() = \"" + UserDataGenerator.Date.generateDate(4) + "\"]/ancestor::div[@data-test-id=\"success-notification\"]").should(visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldFailInvalidDate() {
        UserInfo userInfo = UserDataGenerator.Registration.registrationInfo("ru");
        $x("//span/input[@placeholder = 'Город']").setValue(userInfo.getCity());
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").setValue(UserDataGenerator.Date.generateDate(2));
        $x("//span[@data-test-id= 'name']//child::input[@name= 'name']").setValue(userInfo.getName());
        $x("//span[@data-test-id= 'phone']//child::input[@name= 'phone']").setValue(userInfo.getPhone());
        $x("//label[@data-test-id=\"agreement\"]").click();
        $x("//span[text()=\"Запланировать\"]/ancestor::button").click();
        $x("//span[@data-test-id=\"date\"]//span[@class = \"input__sub\"]").shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldFailInvalidFormatDate() {
        UserInfo userInfo = UserDataGenerator.Registration.registrationInfo("ru");
        $x("//span/input[@placeholder = 'Город']").setValue(userInfo.getCity());
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").setValue(UserDataGenerator.Date.generateDate(2).substring(9));
        $x("//span[@data-test-id= 'name']//child::input[@name= 'name']").setValue(userInfo.getName());
        $x("//span[@data-test-id= 'phone']//child::input[@name= 'phone']").setValue(userInfo.getPhone());
        $x("//label[@data-test-id=\"agreement\"]").click();
        $x("//span[text()=\"Запланировать\"]/ancestor::button").click();
        $x("//span[@data-test-id=\"date\"]//span[@class = \"input__sub\"]").shouldHave(text("Неверно введена дата"));
    }

    @Test
    public void shouldFailInvalidCity() {
        UserInfo userInfo = UserDataGenerator.Registration.registrationInfo("en");
        UserInfo userInfoEng = UserDataGenerator.Registration.registrationInfo("en");
        $x("//span/input[@placeholder = 'Город']").setValue(userInfoEng.getCity());
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").setValue(UserDataGenerator.Date.generateDate(3));
        $x("//span[@data-test-id= 'name']//child::input[@name= 'name']").setValue(userInfo.getName());
        $x("//span[@data-test-id= 'phone']//child::input[@name= 'phone']").setValue(userInfo.getPhone());
        $x("//label[@data-test-id=\"agreement\"]").click();
        $x("//span[text()=\"Запланировать\"]/ancestor::button").click();
        $x("//span[@data-test-id=\"city\"]//span[@class = \"input__sub\"]").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldFailInvalidNameAlphabet() {
        UserInfo userInfo = UserDataGenerator.Registration.registrationInfo("ru");
        UserInfo userInfoEng = UserDataGenerator.Registration.registrationInfo("en");
        $x("//span/input[@placeholder = 'Город']").setValue(userInfo.getCity());
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").setValue(UserDataGenerator.Date.generateDate(3));
        $x("//span[@data-test-id= 'name']//child::input[@name= 'name']").setValue(userInfoEng.getName());
        $x("//span[@data-test-id= 'phone']//child::input[@name= 'phone']").setValue(userInfo.getPhone());
        $x("//label[@data-test-id=\"agreement\"]").click();
        $x("//span[text()=\"Запланировать\"]/ancestor::button").click();
        $x("//span[@data-test-id=\"name\"]//span[@class = \"input__sub\"]").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void shouldFailInvalidPhoneLessNumbers() {
        UserInfo userInfo = UserDataGenerator.Registration.registrationInfo("ru");
        $x("//span/input[@placeholder = 'Город']").setValue(userInfo.getCity());
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").setValue(UserDataGenerator.Date.generateDate(3));
        $x("//span[@data-test-id= 'name']//child::input[@name= 'name']").setValue(userInfo.getName());
        $x("//span[@data-test-id= 'phone']//child::input[@name= 'phone']").setValue(userInfo.getPhone().substring(3));
        $x("//label[@data-test-id=\"agreement\"]").click();
        $x("//span[text()=\"Запланировать\"]/ancestor::button").click();
        $x("//span[@data-test-id=\"phone\"]//span[@class = \"input__sub\"]").shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldFailNoValidNoAgreement() {
        UserInfo userInfo = UserDataGenerator.Registration.registrationInfo("ru");
        $x("//span/input[@placeholder = 'Город']").setValue(userInfo.getCity());
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id= 'date']//child::input[@placeholder= 'Дата встречи']").setValue(UserDataGenerator.Date.generateDate(3));
        $x("//span[@data-test-id= 'name']//child::input[@name= 'name']").setValue(userInfo.getName());
        $x("//span[@data-test-id= 'phone']//child::input[@name= 'phone']").setValue(userInfo.getPhone());
        $x("//span[text()=\"Запланировать\"]/ancestor::button").click();
        Assertions.assertTrue($(By.cssSelector("[data-test-id=\"agreement\"]")).getCssValue("color").equals("rgba(255, 92, 92, 1)"));
    }
}

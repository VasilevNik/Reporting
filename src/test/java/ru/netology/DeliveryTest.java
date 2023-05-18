package ru.netology;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataGenerator;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import entities.UserInfo;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {

    private Faker faker;

    public String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        faker = new Faker(new Locale("ru"));
        open("http://localhost:9999/");
    }

    @Test
    void testData() {
        UserInfo info = DataGenerator.Registration.generateInfo("ru");
        String city = info.getCity();
        String date = generateDate(3, "dd.MM.yyyy");
        String name = info.getName();
        String phone = info.getPhone();

        Configuration.holdBrowserOpen = true;
        $("[data-test-id=city] input").setValue(city);
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $("div.notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + date), Duration.ofSeconds(15))
                .shouldBe(visible);

        $("[data-test-id=city] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=city] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=city] input").setValue(city);
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=name] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=phone] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=phone] input").setValue(phone);
        $("button.button").click();
        $("div.notification_status_error")
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);
        $x("//*[text()='Перепланировать']").click();
        $("div.notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + date), Duration.ofSeconds(15))
                .shouldBe(visible);
    }
}

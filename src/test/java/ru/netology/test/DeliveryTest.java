package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfullyPlanAndReplanMeeting() {
        var user = ru.netology.data.DataGenerator.generateUser();
        var firstDate = ru.netology.data.DataGenerator.generateDate(3);
        var secondDate = ru.netology.data.DataGenerator.generateDate(7);

        // Заполнение формы
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), firstDate);
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.text("Запланировать")).click();

        // Проверка первого уведомления
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!"));

        // Перепланирование
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), secondDate);
        $$("button").findBy(Condition.text("Запланировать")).click();

        // Подтверждение перепланирования
        $("[data-test-id='replan-notification'] button")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .click();

        // Проверка второго уведомления
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!"));
    }
}
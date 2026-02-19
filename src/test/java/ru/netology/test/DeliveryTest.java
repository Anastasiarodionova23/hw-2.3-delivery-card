package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {

    @BeforeEach
    void setUp() {
        // Открываем браузер на странице приложения
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfullyPlanAndReplanMeeting() {
        // Генерируем данные
        DataGenerator.UserInfo user = DataGenerator.generateUser();
        String firstDate = DataGenerator.generateDate(3); // первая дата через 3 дня
        String secondDate = DataGenerator.generateDate(5); // вторая дата через 5 дней

        // Заполняем форму для первой даты
        $("[data-test-id='city'] input").setValue(user.getCity());

        // Очищаем поле даты и вводим новую
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstDate);

        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id='agreement']").click();

        // Нажимаем кнопку "Запланировать"
        $$("button").findBy(Condition.text("Запланировать")).click();

        // Проверяем первое уведомление: заголовок и полный текст с датой
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!"));

        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstDate));

        // Планируем встречу на другую дату (перепланирование)
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondDate);

        $$("button").findBy(Condition.text("Запланировать")).click();

        // Подтверждаем перепланирование в появившемся диалоге
        $(withText("Перепланировать")).click();

        // Проверяем второе уведомление: заголовок и полный текст с новой датой
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!"));

        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondDate));
    }
}
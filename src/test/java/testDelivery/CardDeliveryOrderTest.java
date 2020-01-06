package testDelivery;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

import data.DefaultData;

class CardDeliveryOrderTest {

    @Test
    @DisplayName(value = "Check verification form with correct input")
    void checkVerificationFormWithCorrectInput() {
        open("http://localhost:9999");
        DefaultData.defaultCity();
        DefaultData.datePicker();
        DefaultData.defaultName();
        DefaultData.defaultPhone();
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $("[class='notification__content']").waitUntil(Condition.visible, 25000)
                .shouldHave(text("Встреча успешно забронирована на "));
    }

    @Test
    @DisplayName(value = "Wrong date selection test")
    void wrongDateSelectionTest() {
        open("http://localhost:9999");
        DefaultData.defaultCity();
        $("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[placeholder='Дата встречи']").setValue(DefaultData.localDate.format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        DefaultData.defaultName();
        DefaultData.defaultPhone();
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $(".input_invalid .input__sub").shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    @DisplayName(value = "Wrong city selection test")
    void wrongCitySelectionTest() {
        open("http://localhost:9999");
        $("[placeholder='Город']").setValue("Норильск");
        DefaultData.datePicker();
        DefaultData.defaultName();
        DefaultData.defaultPhone();
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $(".input_invalid .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    @DisplayName(value = "Wrong name selection test")
    void wrongNameSelectionTest() {
        open("http://localhost:9999");
        DefaultData.defaultCity();
        DefaultData.datePicker();
        $("[name='name']").setValue("Razuvaev Ivan");
        DefaultData.defaultPhone();
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $(".input_invalid .input__sub").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только " +
                "русские буквы, пробелы и дефисы."));
    }

    @Test
    @DisplayName(value = "Wrong phone selection test")
    void wrongPhoneSelectionTest() {
        open("http://localhost:9999");
        DefaultData.defaultCity();
        DefaultData.datePicker();
        DefaultData.defaultName();
        $("[name='phone']").setValue("+790123456");
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $(".input_invalid .input__sub").shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, " +
                "например, +79012345678."));
    }

    @Test
    @DisplayName(value = "Check for empty field")
    void checkForEmptyField() {
        open("http://localhost:9999");
        DefaultData.datePicker();
        DefaultData.defaultName();
        DefaultData.defaultPhone();
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $(".input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName(value = "Verification not agreement")
    void verificationNotAgreement() {
        open("http://localhost:9999");
        DefaultData.defaultCity();
        DefaultData.datePicker();
        DefaultData.defaultName();
        DefaultData.defaultPhone();
        $("[class='button__content']").click();
        $("[class='checkbox__text']").shouldHave(text("Я соглашаюсь с условиями обработки и " +
                "использования моих персональных данных"));
    }

    @Test
    @DisplayName(value = "Check city selection by two letters")
    void checkCitySelectionByTwoLetters() {
        open("http://localhost:9999");
        $("[placeholder='Город']").setValue("НН");
        $$("[class=menu-item__control]").find(exactText("Нижний Новгород")).click();
        $("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[placeholder='Дата встречи']")
                .setValue(DefaultData.localDate.plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        DefaultData.defaultName();
        DefaultData.defaultPhone();
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $("[class='notification__content']").waitUntil(Condition.visible, 25000)
                .shouldHave(text("Встреча успешно забронирована на "));
    }

    @Test
    @DisplayName(value = "On 7 days order")
    void on7DaysOrder() {
        LocalDate dateToBe = DefaultData.localDate.plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateToUse = dateToBe.format(formatter);
        open("http://localhost:9999");
        DefaultData.defaultCity();
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id=date] input").click();
        $("[data-test-id=date] input").setValue(dateToUse);
        DefaultData.defaultName();
        DefaultData.defaultPhone();
        $("[data-test-id=agreement]").click();
        $("[class='button__content']").click();
        $("[class='notification__content']").waitUntil(Condition.visible, 25000)
                .shouldHave(text("Встреча успешно забронирована на "));
    }
}
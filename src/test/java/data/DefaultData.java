package data;

import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

public class DefaultData {
    public static LocalDate localDate = LocalDate.now();

    public static Date datePicker() {
        $("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[placeholder='Дата встречи']")
                .setValue(localDate.plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
        return null;
    }

    public static String defaultName() {
        $("[name='name']").setValue("Разуваев Иван");
        return null;
    }

    public static String defaultPhone() {
        $("[name='phone']").setValue("+79991360001");
        return null;
    }

}
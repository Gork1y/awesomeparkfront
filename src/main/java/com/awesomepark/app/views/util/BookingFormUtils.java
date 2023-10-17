package com.awesomepark.app.views.util;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Component
public class BookingFormUtils {

    public static void setupValidation(Binder<?> binder, TextField phone, TextField nameField, TextField surnameField) {
        binder.forField(phone)
                .asRequired("Телефон является обязательным полем")
                .withValidator(new PhoneValidator("Неверный формат номера телефона"))
                .bind("phone");

        binder.forField(nameField)
                .asRequired("Имя является обязательным полем")
                .withValidator(new StringLengthValidator(
                        "Имя должно содержать минимум 2 символа",
                        2, 20))
                .withValidator(name -> !name.matches("\\d.*"),
                        "Имя не должно начинаться с цифры")
                .withValidator(name -> name.replaceAll("\\D", "").length() <= 1,
                        "Имя не должно содержать более одной цифры")
                .bind("name");

        binder.forField(surnameField)
                .asRequired("Фамилия является обязательным полем")
                .withValidator(new StringLengthValidator(
                        "Фамилия должна содержать минимум 2 символа",
                        2, 20))
                .withValidator(surname -> !surname.matches("\\d.*"),
                        "Фамилия не должна начинаться с цифры")
                .withValidator(surname -> surname.replaceAll("\\D", "").length() <= 1,
                        "Фамилия не должна содержать более одной цифры")
                .bind("surname");
    }


    public static void setupDateTimePicker(DateTimePicker timePicker) {
        timePicker.setValue(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));
        timePicker.setMin(LocalDate.now().atTime(0, 0, 0));
        timePicker.setStep(Duration.ofMinutes(30));
        DatePicker.DatePickerI18n russianI18n = new DatePicker.DatePickerI18n();
        russianI18n.setMonthNames(List.of("Январь", "Февраль", "Март", "Апрель",
                "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь",
                "Ноябрь", "Декабрь"));
        russianI18n.setWeekdays(List.of("Воскресенье", "Понедельник", "Вторник",
                "Среда", "Четверг", "Пятница", "Суббота"));
        russianI18n.setWeekdaysShort(
                List.of("Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"));
        russianI18n.setToday("Сегодня");
        russianI18n.setCancel("Отмена");
        timePicker.setDatePickerI18n(russianI18n);
    }

    // Вспомогательный класс валидатора для номера телефона
    private static class PhoneValidator extends com.vaadin.flow.data.validator.RegexpValidator {
        public PhoneValidator(String errorMessage) {
            super(errorMessage, "^[+]?[0-9]{10,13}$");
        }
    }
}


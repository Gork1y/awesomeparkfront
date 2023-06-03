package com.awesomepark.app.views.bookingform;

import com.awesomepark.app.dto.BookingRequestDto;
import com.awesomepark.app.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@UIScope
@Component
@PageTitle("Записаться")
@Route(value = "data-grid/:sampleAddressID?/:action?(edit)", layout = MainLayout.class)
public class BookingFormView extends VerticalLayout {

    private final RestTemplate restTemplate;
    private final TextField phone;
    private final TextField name;
    private final DateTimePicker timePicker;
    private static final String BOOKING_API_URL = "http://localhost:80/api/public/booking";
    private final Binder<BookingRequestDto> binder;

    @Autowired
    public BookingFormView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        Div container = new Div();
        container.setClassName("form-container");

        phone = new TextField("Телефон");
        name = new TextField("Имя");
        timePicker = new DateTimePicker("Время записи");

        Button saveButton = new Button("Записаться на каталку");
        saveButton.addClickListener(this::saveButtonClicked);

        FormLayout formLayout = new FormLayout();
        formLayout.add(phone, name, timePicker, saveButton);

        container.add(formLayout);

        setAlignItems(FlexComponent.Alignment.CENTER);
        add(container);

        binder = new Binder<>(BookingRequestDto.class);
        binder.bindInstanceFields(this);
        setupValidation();
        setupDateTimePicker();
    }

    @PostConstruct
    private void setupValidation() {
        binder.forField(phone)
                .asRequired("Телефон является обязательным полем")
                .withValidator(new PhoneValidator("Неверный формат номера телефона"))
                .bind(BookingRequestDto::getPhone, BookingRequestDto::setPhone);

        binder.forField(name)
                .asRequired("Имя является обязательным полем")
                .bind(BookingRequestDto::getName, BookingRequestDto::setName);

        binder.forField(timePicker)
                .withValidator(dateTime -> dateTime.isAfter(LocalDateTime.now()), "Выберите будущую дату и время")
                .withValidator(dateTime -> dateTime.getHour() >= 10 && dateTime.getHour() < 21, "Доступное время для записи с 10:00 до 21:00")
                .bind(BookingRequestDto::getTime, BookingRequestDto::setTime);
    }

    private void setupDateTimePicker() {
        timePicker.setLocale(new Locale("ru","RU"));
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
    private void clearFields() {
        binder.removeBean();
    }

    // Вспомогательный класс валидатора для номера телефона
    private static class PhoneValidator extends com.vaadin.flow.data.validator.RegexpValidator {
        public PhoneValidator(String errorMessage) {
            super(errorMessage, "^[+]?[0-9]{10,13}$");
        }
    }

    private void saveButtonClicked(ClickEvent<Button> event) {
        String phone = this.phone.getValue();
        String name = this.name.getValue();
        LocalDateTime time = timePicker.getValue();

        BookingRequestDto bookingDto = new BookingRequestDto(phone, name, time);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<BookingRequestDto> requestEntity = new HttpEntity<>(bookingDto, headers);

        try {
            binder.writeBean(bookingDto);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    BOOKING_API_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Notification.show(responseEntity.getBody());
                clearFields();
            } else {
                Notification.show("Ой, ошибка: " + responseEntity.getBody());
            }
        } catch (ValidationException e) {
            Notification.show("Проверьте правильность заполнения формы");
        } catch (Exception e) {
            Notification.show("Ой, ошибка: " + e.getMessage());
        }
    }
}

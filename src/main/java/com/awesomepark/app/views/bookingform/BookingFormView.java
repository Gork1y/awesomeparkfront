package com.awesomepark.app.views.bookingform;

import com.awesomepark.app.data.service.webclients.BookingFeignClient;
import com.awesomepark.app.dto.BookingRequestDto;
import com.awesomepark.app.views.MainLayout;
import com.awesomepark.app.views.util.BookingFormUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
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
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.time.ZoneOffset;

import static com.awesomepark.app.views.util.BookingFormUtils.setupDateTimePicker;


@UIScope
@Component
@Getter
@PageTitle("Записаться")
@Route(value = "bookings", layout = MainLayout.class)
public class BookingFormView extends VerticalLayout {

    BookingFeignClient bookingClient;

    TextField phone;
    TextField name;
    DateTimePicker timePicker;
    Binder<BookingRequestDto> binder;
    private SimpMessagingTemplate messagingTemplate;


    @Autowired
    public BookingFormView(BookingFeignClient bookingClient) {
        this.bookingClient = bookingClient;


        Div container = new Div();
        container.setClassName("form-container");

        phone = new TextField("Телефон");
        name = new TextField("Имя");
        timePicker = new DateTimePicker("Время записи");

        Button saveButton = new Button("Записаться на каталку");
        saveButton.addClickListener(this::saveButtonClicked);
        saveButton.addClassName("save-button");

        FormLayout formLayout = new FormLayout();
        formLayout.add(phone, name, timePicker, saveButton);

        container.add(formLayout);

        setAlignItems(FlexComponent.Alignment.CENTER);
        add(container);

        binder = new Binder<>(BookingRequestDto.class);
        binder.bindInstanceFields(this);
        BookingFormUtils.setupValidation(binder, phone, name);
        setupDateTimePicker(timePicker);
    }


    private void clearFields() {
        binder.removeBean();
    }

    private void saveButtonClicked(ClickEvent<Button> event) {
        String phone = this.phone.getValue();
        String name = this.name.getValue();
        Instant time = timePicker.getValue().toInstant(ZoneOffset.UTC);

        BookingRequestDto bookingDto = new BookingRequestDto(phone, name, time);

        try {
            binder.writeBean(bookingDto);
            ResponseEntity<String> responseEntity = bookingClient.createOrUpdateBooking(bookingDto);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Notification.show(responseEntity.getBody());
                clearFields();
            } else {
                Notification.show("Ой, ошибка: " + responseEntity.getBody());
            }
        } catch (HttpClientErrorException.BadRequest ex) {
            String responseBody = ex.getResponseBodyAsString();
            Notification.show("Ошибка при отправке запроса: " + responseBody);
        } catch (ValidationException ex) {
            Notification.show("Некоректно введены данные!");
        } catch (Exception ex) {
            Notification.show("Произошла ошибка: " + ex.getMessage());
        }
    }

}


package com.awesomepark.app.views.bookingadminpanel;

import com.awesomepark.app.data.entity.Booking;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import static com.awesomepark.app.views.util.BookingFormUtils.*;

public class BookingForm extends FormLayout {
    TextField phone = new TextField("Телефон");
    TextField name = new TextField("Имя");

    DateTimePicker timePicker = new DateTimePicker("Время записи");
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");

    Binder<Booking> binder = new BeanValidationBinder<>(Booking.class);

    public BookingForm() {
        addClassName("contact-form");
        binder.bindInstanceFields(this);

        add(name,
            phone,
            timePicker,
            createButtonsLayout());
        setupValidation(binder, phone, name);
        setupDateTimePicker(timePicker);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave()); // <1>
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean()))); // <2>
        close.addClickListener(event -> fireEvent(new CloseEvent(this))); // <3>

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid())); // <4>
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean())); // <6>
        }
    }


    public void setBooking(Booking booking) {
        binder.setBean(booking); // <1>
    }

    // Events
    public static abstract class BookingFormEvent extends ComponentEvent<BookingForm> {
        private final Booking booking;

        protected BookingFormEvent(BookingForm source, Booking booking) {
            super(source, false);
            this.booking = booking;
        }

        public Booking getBooking() {
            return booking;
        }
    }

    public static class SaveEvent extends BookingFormEvent {
        SaveEvent(BookingForm source, Booking booking) {
            super(source, booking);
        }
    }

    public static class DeleteEvent extends BookingFormEvent {
        DeleteEvent(BookingForm source, Booking booking) {
            super(source, booking);
        }

    }

    public static class CloseEvent extends BookingFormEvent {
        CloseEvent(BookingForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

}



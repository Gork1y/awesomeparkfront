package com.awesomepark.app.views.bookingadminpanel;

import com.awesomepark.app.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Админка")
@Route(value = "Booking-Admin-Panel", layout = MainLayout.class)

public class BookingAdminPanelView extends Div implements BeforeEnterObserver {

    private final TextField phone = new TextField("Phone");
    private final TextField name = new TextField("Name");
    private final DatePicker timePicker = new DatePicker("Time");

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    public BookingAdminPanelView() {
        addClassNames("booking-admin-panel-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        createEditorLayout(splitLayout);
        add(splitLayout);

        cancel.addClickListener(e -> clearFields());

        save.addClickListener(e -> saveButtonClicked());

        clearFields();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Empty method body, as no specific logic is required before entering the view
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        formLayout.add(phone, name, timePicker);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void saveButtonClicked() {
        String phoneValue = phone.getValue();
        String nameValue = name.getValue();
//        LocalDateTime timeValue = timePicker.getValue();

//        BookingRequestDto bookingDto = new BookingRequestDto(phoneValue, nameValue, timeValue);

        // Call your API endpoint to save the bookingDto
        // ...

        clearFields();
        Notification.show("Data saved");
    }

    private void clearFields() {
        phone.clear();
        name.clear();
        timePicker.clear();
    }
}

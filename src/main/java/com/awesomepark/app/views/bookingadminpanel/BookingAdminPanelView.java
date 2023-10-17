package com.awesomepark.app.views.bookingadminpanel;

import com.awesomepark.app.data.entity.Booking;
import com.awesomepark.app.service.CrmService;
import com.awesomepark.app.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@PageTitle("Админка")
@SpringComponent
@Scope("prototype")
@Route(value = "Booking-Admin-Panel", layout = MainLayout.class)
public class BookingAdminPanelView extends VerticalLayout {
    Grid<Booking> grid = new Grid<>(Booking.class);
    TextField filterText = new TextField();
    BookingForm form;
    CrmService service;

    public BookingAdminPanelView(CrmService crmService) {
        this.service = crmService;
        addClassName("admin-panel");
        setSizeFull();

        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new BookingForm();
        form.setWidth("40em");
        form.addSaveListener(this::saveBooking); // <1>
        form.addDeleteListener(this::deleteBooking); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setHeightFull();

        grid.setColumns("name", "surname", "phone");
        grid.getColumnByKey("name").setHeader("Имя");
        grid.getColumnByKey("surname").setHeader("Фамилия");
        grid.getColumnByKey("phone").setHeader("Телефон");
        grid.addColumn(booking -> {
                    LocalDateTime time = booking.getTime();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM в HH:mm");
                    return time.format(formatter);
                })
                .setHeader("Время записи")
                .setKey("time")
                .setSortable(true)
                .setSortProperty("time");

        grid.addColumn(booking -> {
                    if (booking.getActivityId() == 1) {
                        return "Вейкборд";
                    } else if (booking.getActivityId() == 2) {
                        return "Сап-борд";
                    } else if (booking.getActivityId() == 3) {
                        return "Другое";
                    }
                    return "Неизвестный тип услуги";
                }).setHeader("Тип услуги")
                .setKey("activityType")
                .setSortable(true)
                .setSortProperty("activityType");

        grid.addColumn(booking -> booking.getActivityCount().toString())
                .setHeader("Количество бронирований")
                .setKey("bookingCount")
                .setSortable(true)
                .setSortProperty("bookingCount");

        grid.asSingleSelect().addValueChangeListener(event ->
                editBooking(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Фильтр(пока не работает)");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Добавить нового катуна");
        addContactButton.addClickListener(click -> addBooking());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editBooking(Booking booking) {
        if (booking == null) {
            closeEditor();
        } else {
            form.setBooking(booking);
            if (booking.getActivityId() != null || booking.getActivityCount() != null) {
                form.setActivityType(booking);
                form.setBookingCount(booking);
                form.setBookingCount(booking);
            }
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void saveBooking(BookingForm.SaveEvent event) {
        event.getBooking().setPhone(form.phone.getValue());
        event.getBooking().setName(form.name.getValue());
        event.getBooking().setSurname(form.surname.getValue());
        event.getBooking().setTime(form.timePicker.getValue());
        event.getBooking().setActivityCount(form.bookingCount.getValue()); // втыкнули значение полей для списочка с бронированиями
        event.getBooking().setActivityId(form.activityType.getValue()); // и для радиобатона который меняет сост
        service.saveBooking(event.getBooking());
        updateList();

    }

    private void deleteBooking(BookingForm.DeleteEvent event) {
        service.deleteBooking(event.getBooking());
        updateList();

    }

    private void closeEditor() {
        form.setBooking(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAllBookings(filterText.getValue()));
    }

    private void addBooking() {
        grid.asSingleSelect().clear();
        editBooking(new Booking());
    }
}

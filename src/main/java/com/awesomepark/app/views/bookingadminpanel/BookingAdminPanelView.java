package com.awesomepark.app.views.bookingadminpanel;

import com.awesomepark.app.data.entity.Booking;
import com.awesomepark.app.data.service.CrmService;
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

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
        form.setWidth("25em");
        form.addSaveListener(this::saveBooking); // <1>
        form.addDeleteListener(this::deleteBooking); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setHeightFull();
//        grid.setColumns("phone", "name", "email");
//        grid.addColumn(booking -> booking.getStatus().getName()).setHeader("Status");
//        grid.addColumn(booking -> booking.getCompany().getName()).setHeader("Company");
//        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setColumns("name", "phone");
        grid.getColumnByKey("name").setHeader("Имя");
        grid.getColumnByKey("phone").setHeader("Телефон");
        grid.addColumn(booking -> {
                    Instant time = booking.getTime();
                    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(time, ZoneId.systemDefault());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM в HH:mm");
                    return zonedDateTime.format(formatter);
                })
                .setHeader("Время записи")
                .setKey("time")
                .setSortable(true)
                .setSortProperty("time");


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
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void saveBooking(BookingForm.SaveEvent event) {
        service.saveBooking(event.getBooking());
        updateList();
        closeEditor();
    }

    private void deleteBooking(BookingForm.DeleteEvent event) {
        service.deleteBooking(event.getBooking());
        updateList();
        closeEditor();
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

/* private final Grid<BookingResponseDto> bookingGrid;
    private TextField nameField;
    private TextField phoneField;
    private DatePicker dateField;
    private Button saveButton;
    private Button deleteButton;
    private Button cancelButton;
    private BookingFormView bookingFormView;

    private Binder<BookingResponseDto> binder;
    private BookingResponseDto currentBooking;


    private final BookingFeignClient bookingFeignClient;

    public BookingAdminPanelView(BookingFormView bookingFormView, BookingFeignClient bookingFeignClient) {
        this.bookingFormView = bookingFormView;
        this.bookingFeignClient = bookingFeignClient;

        bookingGrid = new Grid<>(BookingResponseDto.class);
        bookingGrid.setColumns("name", "phone", "time");
        bookingGrid.setItems(getAllBookings());
        bookingGrid.asSingleSelect().addValueChangeListener(event -> showBooking(event.getValue()));

        nameField = new TextField("Имя");
        phoneField = new TextField("Телефон");
        dateField = new DatePicker("Дата");

        saveButton = new Button("Сохранить", event -> saveBooking());
        deleteButton = new Button("Удалить", event -> deleteBooking());
        cancelButton = new Button("Отмена", event -> cancel());

     *//*   binder = new BeanValidationBinder<>(BookingResponseDto.class);
        binder.bind(nameField, "name");
        binder.bind(phoneField, "phone");
        binder.bind(dateField, "time");*//*

        add(getContent());
        setSpacing(true);
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(bookingGrid, bookingFormView);
        content.setFlexGrow(2, bookingGrid);
        content.setFlexGrow(1, bookingFormView);
        content.addClassNames("admin");
        content.setSizeFull();
        return content;
    }

    private List<BookingResponseDto> getAllBookings() {
        ResponseEntity<List<BookingResponseDto>> response = bookingFeignClient.getAllBookings();
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return Collections.emptyList();
    }

    private void showBooking(BookingResponseDto booking) {
        if (booking != null) {
            currentBooking = booking;
            binder.setBean(currentBooking);
        } else {
            clearFields();
        }
    }

    private void saveBooking() {
        try {
            BookingRequestDto bookingDto = new BookingRequestDto();
            binder.writeBean(currentBooking);
            bookingDto.setName(currentBooking.getName());
            bookingDto.setPhone(currentBooking.getPhone());
            bookingDto.setTime(currentBooking.getTime());
            ResponseEntity<String> response = bookingFeignClient.createOrUpdateBooking(bookingDto);
            if (response.getStatusCode().is2xxSuccessful()) {
                refreshGrid();
                clearFields();
            }
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private void deleteBooking() {
        if (currentBooking != null) {
            ResponseEntity<String> response = bookingFeignClient.deleteBooking(currentBooking.getId());
            if (response.getStatusCode().is2xxSuccessful()) {
                refreshGrid();
                clearFields();
            }
        }
    }

    private void cancel() {
        clearFields();
    }

    private void refreshGrid() {
        bookingGrid.setItems(getAllBookings());
    }

    private void clearFields() {
        currentBooking = null;
        binder.readBean(null);
    }*/





/*
import com.awesomepark.app.data.service.CrmService;
import com.awesomepark.app.views.MainLayout;
import com.awesomepark.app.views.bookingform.BookingFormView;
import com.awesomepark.app.views.datagrid.DataGridView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Админка")
@SpringComponent
@Route(value = "Booking-Admin-Panel", layout = MainLayout.class)
public class BookingAdminPanelView extends VerticalLayout {
    BookingFormView bookingFormView;
    DataGridView dataGridView;
    CrmService service;
    String phoneValue = bookingFormView.getPhone().getValue();
    String nameValue = bookingFormView.getName().getValue();
    String timeValue = bookingFormView.getTimePicker().getValue().toString();
    public BookingAdminPanelView(){

    }
    @Autowired
    public BookingAdminPanelView(DataGridView dataGridView) {


        addClassNames("booking-admin-panel-view");


        SplitLayout splitLayout = new SplitLayout();
        createEditorLayout(splitLayout);
        add(splitLayout);


        splitLayout.addToPrimary(dataGridView);

        Button cancel = new Button("Cancel");
//        cancel.addClickListener(cancel);

        Button save = new Button("Save");
        save.addClickListener(e -> saveButtonClicked());

        Button delete = new Button("Delete");
        delete.addClickListener(e -> deleteButtonClicked());


    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        formLayout.add(bookingFormView.getPhone(), bookingFormView.getName(), bookingFormView.getTimePicker());

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);

        splitLayout.addToPrimary(dataGridView); // Добавляем таблицу в левую часть SplitLayout
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        // ...
        HorizontalLayout buttonLayout = new HorizontalLayout();
        // ...

        Button addButton = new Button("Добавить");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(event -> {


            // Добавьте код для обработки добавления записи в таблицу DataGridView

            // Очистите поля ввода после добавления записи
            bookingFormView.getPhone().clear();
            bookingFormView.getName().clear();
            bookingFormView.getTimePicker().clear();
        });

        // ...

        buttonLayout.add(addButton);
        editorLayoutDiv.add(buttonLayout);
    }


    private void saveButtonClicked() {


        if (phoneValue.isEmpty() || nameValue.isEmpty() || timeValue.isEmpty()) {
            Notification.show("Please enter all fields", 3000, Notification.Position.MIDDLE);
            return;
        }

        // Save booking logic

        Notification.show("Booking saved successfully!", 3000, Notification.Position.MIDDLE);

    }

    private void deleteButtonClicked() {
        if (phoneValue.isEmpty()) {
            Notification.show("Please enter the phone number to delete", 3000, Notification.Position.MIDDLE);
            return;
        }

        // Delete booking logic

        Notification.show("Booking deleted successfully!", 3000, Notification.Position.MIDDLE);

    }



*/
/*Grid<Booking> grid = new Grid<>(Booking.class);
    TextField filterText = new TextField();
    BookingFormView form;
    CrmService service;

    public BookingAdminPanelView(BookingFormView bookingFormView, CrmService service) {
        this.bookingFormView = bookingFormView;
        this.service = service;
        addClassName("list-view");
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
        form = new BookingFormView(service.findAllBookings(), service.findAllStatuses());
        form.setWidth("25em");
        form.addSaveListener(this::saveContact); // <1>
        form.addDeleteListener(this::deleteContact); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveContact(SaveEvent event) {
        service.saveBooking(event.getBooking());
        updateList();
        closeEditor();
    }

    private void deleteContact(DeleteEvent event) {
        service.deleteBooking(event.getBooking());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email");
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editContact(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact");
        addContactButton.addClickListener(click -> addContact());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editContact(Contact contact) {
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }


    private void updateList() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }*//*

}



*/

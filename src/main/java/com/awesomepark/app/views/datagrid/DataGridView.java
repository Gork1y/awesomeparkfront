package com.awesomepark.app.views.datagrid;

import com.awesomepark.app.dto.BookingResponseDto;
import com.awesomepark.app.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@PageTitle("Список бронирований")
@Route(value = "bookings", layout = MainLayout.class)
@AnonymousAllowed
@Component
@UIScope
@RequiredArgsConstructor
public class DataGridView extends VerticalLayout {

    public static final String API_PUBLIC_BOOKING = "http://localhost:80/api/public/booking";
    private Grid<BookingResponseDto> grid;

    @PostConstruct
    public void init() {
        setSizeFull();
        addClassNames("booking-list-view");

        createGrid();
        fetchBookings();
    }

    private void createGrid() {
        grid = new Grid<>(BookingResponseDto.class);
        grid.setColumns("name", "phone");
        grid.getColumnByKey("name").setHeader("Имя");
        grid.getColumnByKey("phone").setHeader("Телефон");
        grid.addColumn(dto -> dto.getTime().format(DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm")))
                .setHeader("Время записи")
                .setKey("time")
                .setSortable(true)
                .setSortProperty("time");

        grid.setHeightFull();

        add(grid);
    }

    private void fetchBookings() {
        try {
            grid.setItems(getAllComments());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<BookingResponseDto> getAllComments() {

        System.out.println("Fetching all Comment objects through REST..");

        // Fetch from 3rd party API; configure fetch
        WebClient.RequestHeadersSpec<?> spec = WebClient.create().
                get().uri(API_PUBLIC_BOOKING);

        // do fetch and map result
        List<BookingResponseDto> bookingResponseDto = Objects.requireNonNull(spec.retrieve().
                toEntityList(BookingResponseDto.class).block()).getBody();

        assert bookingResponseDto != null;
        System.out.printf("...received %d items.%n", bookingResponseDto.size());

        return bookingResponseDto;
    }

}
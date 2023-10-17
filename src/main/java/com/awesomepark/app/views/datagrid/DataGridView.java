package com.awesomepark.app.views.datagrid;

import com.awesomepark.app.dto.BookingResponseDto;
import com.awesomepark.app.service.BookingFeignClient;
import com.awesomepark.app.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@PageTitle("Список бронирований")
@Route(value = "data-grid", layout = MainLayout.class)
@AnonymousAllowed
@Component
@UIScope
@RequiredArgsConstructor
@Scope(value = "vaadin-ui", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DataGridView extends VerticalLayout {
    private Grid<BookingResponseDto> grid;
    private final BookingFeignClient bookingClient;


    @PostConstruct
    public void init() {
        setSizeFull();
        addClassNames("booking-list-view");

        createGrid();
        fetchBookings();
    }

    private void createGrid() {
        grid = new Grid<>(BookingResponseDto.class);
        grid.setColumns("name");
        grid.getColumnByKey("name").setHeader("Имя");
//        grid.getColumnByKey("phone").setHeader("Телефон");
        grid.addColumn(booking -> {
                    LocalDateTime time = booking.getTime();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM в HH:mm");
                    return time.format(formatter);
                })
                .setHeader("Время записи")
                .setKey("time")
                .setSortable(true)
                .setSortProperty("time");

        grid.setHeightFull();
        grid.addColumn(booking -> {
                    if (booking.getActivityId() == 1) {
                        return "Вейкборд";
                    } else if (booking.getActivityId() == 2) {
                        return "Сап-борд";
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
        ResponseEntity<List<BookingResponseDto>> responseEntity = bookingClient.getAllBookings();
        List<BookingResponseDto> bookingResponseDtoList = responseEntity.getBody();

        if (bookingResponseDtoList != null) {
            System.out.printf("...received %d items.%n", bookingResponseDtoList.size());
        } else {
            System.out.println("...received no items.");
        }
        return bookingResponseDtoList;
    }
}
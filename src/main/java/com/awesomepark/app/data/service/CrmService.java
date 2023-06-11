package com.awesomepark.app.data.service;

import com.awesomepark.app.data.entity.Booking;
import com.awesomepark.app.data.service.webclients.BookingFeignClient;
import com.awesomepark.app.dto.BookingRequestDto;
import com.awesomepark.app.dto.BookingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CrmService {
    private final BookingFeignClient bookingFeignClient;


    public void saveBooking(Booking booking) {
        if (booking == null) {
            System.err.println("Нихрена нету, проверь чтоб было не так");
            return;
        }
        BookingRequestDto dto = bookingToBookingRequestDto(booking);
        bookingFeignClient.createOrUpdateBooking(dto);
    }



    public List<Booking> findAllBookings(String stringFilter) {
//        if (stringFilter == null || stringFilter.isEmpty()) {
        List<BookingResponseDto> dtoList = bookingFeignClient.getAllBookings().getBody();
        List<Booking> bookings = new ArrayList<>();
        assert dtoList != null;
        for (BookingResponseDto dto : dtoList) {
            Booking booking = bookingResponseDtoToBooking(dto);
            bookings.add(booking);
        }
            return bookings;
//        } else {
//            return contactRepository.search(stringFilter); todo : доделать поиск
//        }
    }

    public void deleteBooking(Booking booking) {
        bookingFeignClient.deleteBooking(UUID.fromString(booking.getId()));
    }

    private Booking bookingResponseDtoToBooking(BookingResponseDto dto) { //todo переделать на мапер
        Booking booking = new Booking();
        booking.setPhone(dto.getPhone());
        booking.setName(dto.getName());
        booking.setTime(dto.getTime());
        booking.setId(String.valueOf(dto.getId()));

        return booking;
    }
    private BookingRequestDto bookingToBookingRequestDto(Booking booking) { //todo переделать на мапер
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setPhone(booking.getPhone());
        requestDto.setName(booking.getName());
        requestDto.setTime(booking.getTime());
        requestDto.setId(String.valueOf(booking.getId()));

        return requestDto;
    }

}

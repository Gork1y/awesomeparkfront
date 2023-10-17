package com.awesomepark.app.service;

import com.awesomepark.app.data.entity.Booking;
import com.awesomepark.app.dto.BookingRequestDto;
import com.awesomepark.app.dto.BookingResponseDto;
import com.awesomepark.app.mappers.BookingResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrmService {
    private final BookingFeignClient bookingFeignClient;
    private final BookingResponseMapper mapper;


    public void saveBooking(Booking booking) {
        if (booking == null) {
            System.err.println("Нихрена нету, проверь чтоб было не так");
            throw new NullPointerException("ничего нет");
        }
        BookingRequestDto dto = bookingToBookingRequestDto(booking);
        if (dto.getTime() != null ) {
            bookingFeignClient.createOrUpdateBooking(dto);
        } else {
            throw new NullPointerException("Пустое поле");
        }
    }


    public List<Booking> findAllBookings(String stringFilter) {
//        if (stringFilter == null || stringFilter.isEmpty()) {
        List<BookingResponseDto> dtoList = bookingFeignClient.getAllBookings().getBody();
        List<Booking> bookings = new ArrayList<>();
        assert dtoList != null;
        for (BookingResponseDto dto : dtoList) {
            Booking booking = mapper.mapToEntity(dto);
            bookings.add(booking);
        }
        return bookings;
//        } else {
//            return contactRepository.search(stringFilter); todo : доделать поиск
//        }
    }

    public void deleteBooking(Booking booking) {
        bookingFeignClient.deleteBooking(Long.valueOf(booking.getId()));
    }


    private BookingRequestDto bookingToBookingRequestDto(Booking booking) { //todo переделать на мапер
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setId(booking.getId());
        requestDto.setPhone(booking.getPhone());
        requestDto.setName(booking.getName());
        requestDto.setSurname(booking.getSurname());
        requestDto.setTime(booking.getTime());
        requestDto.setActivityId(booking.getActivityId());
        requestDto.setActivityCount(booking.getActivityCount());
        return requestDto;
    }

}

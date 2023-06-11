package com.awesomepark.app.data.service.webclients;


import com.awesomepark.app.dto.BookingRequestDto;
import com.awesomepark.app.dto.BookingResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "booking-service", url = "${booking.api.base-url}")
@Service
public interface BookingFeignClient {

    @PostMapping()
    ResponseEntity<String> createOrUpdateBooking(@RequestBody BookingRequestDto bookingDto);

    @GetMapping("/{id}")
    ResponseEntity<BookingResponseDto> getBookingById(@PathVariable("id") UUID id);

    @GetMapping()
    ResponseEntity<List<BookingResponseDto>> getAllBookings();

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteBooking(@PathVariable("id") UUID id);
}

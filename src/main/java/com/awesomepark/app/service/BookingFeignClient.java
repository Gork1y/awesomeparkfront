package com.awesomepark.app.service;

import com.awesomepark.app.dto.BookingRequestDto;
import com.awesomepark.app.dto.BookingResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "booking-service", url = "${booking.api.base-url}")
@Service
public interface BookingFeignClient {

    @PostMapping("/")
    ResponseEntity<String> createOrUpdateBooking(@RequestBody BookingRequestDto bookingDto);

    @GetMapping("/{id}")
    ResponseEntity<BookingResponseDto> getBookingById(@PathVariable("id") UUID id);

    @GetMapping("/")
    ResponseEntity<List<BookingResponseDto>> getAllBookings();

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteBooking(@PathVariable("id") Long id);
}

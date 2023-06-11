/*
package com.awesomepark.app.data.service.webclients;
import com.awesomepark.app.dto.BookingRequestDto;
import com.awesomepark.app.dto.BookingResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class WebClientBookingService {

    private final WebClient webClient;
    @Value("${booking.api.base-url}")
    private String bookingApiBaseUrl;

    @Autowired
    public WebClientBookingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(bookingApiBaseUrl).build();
    }

    public Mono<ResponseEntity<String>> createOrUpdateBooking(String phone, String name, LocalDateTime time) {
        BookingRequestDto bookingDto = new BookingRequestDto(phone, name, time);
        return webClient.post()
                .uri("/")
                .body(BodyInserters.fromValue(bookingDto))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);
    }

    public Mono<ResponseEntity<BookingResponseDto>> getBookingById(UUID id) {
        return webClient.get()
                .uri("/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(BookingResponseDto.class);
    }

    public Mono<ResponseEntity<List<BookingResponseDto>>> getAllBookings() {
        return webClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(BookingResponseDto.class);
    }

    public Mono<ResponseEntity<String>> deleteBooking(UUID id) {
        return webClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .toEntity(String.class);
    }
}

*/

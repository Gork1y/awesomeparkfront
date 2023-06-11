package com.awesomepark.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor


public class BookingRequestDto {

    private String id;

    private String phone;

    private String name;

    private Instant time;

    public BookingRequestDto(String phone, String name, Instant time) {
        this.phone = phone;
        this.name = name;
        this.time = time;
    }
}

package com.awesomepark.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private UUID id;
    private String phone;

    private String name;

    private Instant time;
}

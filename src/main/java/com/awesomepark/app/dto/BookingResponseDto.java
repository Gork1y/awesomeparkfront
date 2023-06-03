package com.awesomepark.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private String phone;

    private String name;

    private LocalDateTime time;
}

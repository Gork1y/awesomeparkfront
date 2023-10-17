package com.awesomepark.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private Long id;
    private String phone;
    private String name;
    private String surname;
    private LocalDateTime time;
    private Long activityId;
    private Integer activityCount;
}

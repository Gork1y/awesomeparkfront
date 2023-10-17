package com.awesomepark.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data


@RequiredArgsConstructor
public class BookingRequestDto {
    @JsonProperty("id")
    private String Id;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("time")
    private LocalDateTime time;

    @JsonProperty("activityId")
    private Long activityId;

    @JsonProperty("activityCount")
    private Integer activityCount;


    public BookingRequestDto(String phone, String name, String surname, LocalDateTime time, Long activityId, Integer activityCount) {
        this.phone = phone;
        this.name = name;
        this.surname = surname;
        this.time = time;
        this.activityId = activityId;
        this.activityCount = activityCount;
    }
}

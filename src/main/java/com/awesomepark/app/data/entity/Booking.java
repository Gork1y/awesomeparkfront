package com.awesomepark.app.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
public class Booking {
    @NotEmpty
    @Id
    private String id;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String name;
    @NotEmpty
    private String surname;
    @NotEmpty
    private LocalDateTime time;
    @NotEmpty
    private Long activityId;
    @NotEmpty
    private Integer activityCount;

}

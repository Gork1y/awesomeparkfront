package com.awesomepark.app.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.Instant;


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
    private Instant time;

}

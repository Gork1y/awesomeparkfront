package com.awesomepark.app.data.entity;

import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Booking extends AbstractEntity {

    private String phone;
    private String name;
    private LocalDateTime time;

}

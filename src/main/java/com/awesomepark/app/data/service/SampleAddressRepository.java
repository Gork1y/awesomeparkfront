package com.awesomepark.app.data.service;

import com.awesomepark.app.data.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SampleAddressRepository
        extends
            JpaRepository<Booking, Long>,
            JpaSpecificationExecutor<Booking> {

}

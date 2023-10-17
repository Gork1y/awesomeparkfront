package com.awesomepark.app.mappers;


import com.awesomepark.app.data.entity.Booking;
import com.awesomepark.app.dto.BookingRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingRequestMapper extends BaseMapper<BookingRequestDto, Booking> {
}


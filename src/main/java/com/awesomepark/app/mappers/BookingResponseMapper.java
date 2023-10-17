package com.awesomepark.app.mappers;


import com.awesomepark.app.data.entity.Booking;
import com.awesomepark.app.dto.BookingResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingResponseMapper extends BaseMapper<BookingResponseDto, Booking> {
}


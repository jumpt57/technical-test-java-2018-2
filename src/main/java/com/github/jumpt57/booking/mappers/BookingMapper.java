package com.github.jumpt57.booking.mappers;

import com.github.jumpt57.booking.domain.Booking;
import com.github.jumpt57.booking.dto.BookingWithPassword;
import com.github.jumpt57.booking.dto.SimpleBooking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    SimpleBooking bookingToSimpleBooking(Booking booking);

    @Mapping(target = "encryptedPassword", ignore = true)
    Booking bookingWithPasswordToBooking(BookingWithPassword bookingWithPassword);

}

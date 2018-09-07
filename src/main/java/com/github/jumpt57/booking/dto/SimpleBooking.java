package com.github.jumpt57.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleBooking {

    String roomNumber;
    String userName;
    Integer hourStart;

}

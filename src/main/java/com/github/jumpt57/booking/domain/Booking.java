package com.github.jumpt57.booking.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    String roomNumber;
    Integer hourStart;
    String userName;
    String encryptedPassword;

}

package com.github.jumpt57.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingWithPassword {

    @Size(min = 3, max = 3)
    String roomNumber;

    @Min(8)
    @Max(19)
    Integer hourStart;

    @Size(min = 5, max = 25)
    String userName;

    @Size(min = 8, max = 50)
    String password;

}

package com.github.jumpt57.booking.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
class RoomService {

    static final String COCA = "C";
    static final String PEPSI = "P";
    static final int HOW_MANY_ROOMS = 10;

    Collection<String> rooms = new HashSet<>();

    @PostConstruct
    private void initRooms() {
        rooms.addAll(generateRoomForCorporation(COCA));
        rooms.addAll(generateRoomForCorporation(PEPSI));
    }

    private Set<String> generateRoomForCorporation(String corporate) {
        return IntStream.range(1, HOW_MANY_ROOMS + 1)
                .mapToObj(value -> format("%s%s%s", corporate, value < 10 ? "0" : "", value))
                .collect(Collectors.toSet());
    }

    Collection<String> getRooms() {
        return rooms;
    }

}

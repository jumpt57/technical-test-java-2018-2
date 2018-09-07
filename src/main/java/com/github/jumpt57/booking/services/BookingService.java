package com.github.jumpt57.booking.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jumpt57.booking.domain.Block;
import com.github.jumpt57.booking.domain.Booking;
import com.github.jumpt57.booking.dto.BookingWithPassword;
import com.github.jumpt57.booking.dto.SimpleBooking;
import com.github.jumpt57.booking.mappers.BookingMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.jumpt57.booking.common.StringUtil.applySha256;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class BookingService {

    static ObjectMapper objectMapper = new ObjectMapper();
    static final String EMPTY_STRING = "";
    static final int MIN_HOUR = 8;
    static final int MAX_HOUR = 19;

    final BlockChainService blockChainService;
    final RoomService roomService;
    final BookingMapper bookingMapper;

    static final Function<String, Booking> STRING_TO_BOOKING = data -> {
        try {
            return objectMapper.readValue(data, Booking.class);
        } catch (IOException e) {
            return null;
        }
    };

    static final Predicate<SimpleBooking> NOT_BOOKED = booking -> booking.getUserName().isEmpty();
    static final Predicate<SimpleBooking> BOOKED = booking -> !booking.getUserName().isEmpty();


    @PostConstruct
    private void initEmptyBookings() {
        roomService.getRooms()
                .forEach(roomNumber -> IntStream.range(MIN_HOUR, MAX_HOUR + 1)
                        .mapToObj(hourStart ->
                                buildEmptyBooking(roomNumber, hourStart)
                        )
                        .forEach(blockChainService::createBlock)
                );
    }

    public Collection<SimpleBooking> getBookings() {
        return getSimpleBookingFromBlockChain().stream()
                .filter(BOOKED)
                .sorted(Comparator.comparing(SimpleBooking::getRoomNumber)
                        .thenComparing(SimpleBooking::getHourStart))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Collection<SimpleBooking> getAvailableBookings() {
        return getSimpleBookingFromBlockChain().stream()
                .filter(NOT_BOOKED)
                .sorted(Comparator.comparing(SimpleBooking::getRoomNumber)
                        .thenComparing(SimpleBooking::getHourStart))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Collection<SimpleBooking> getBookingsForRoom(String roomNumber) {
        return getSimpleBookingFromBlockChain().stream()
                .filter(booking -> booking.getRoomNumber().equals(roomNumber))
                .filter(BOOKED)
                .sorted(Comparator.comparing(SimpleBooking::getHourStart))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public boolean createBooking(BookingWithPassword bookingWithPassword) {
        if (available(bookingWithPassword.getRoomNumber(), bookingWithPassword.getHourStart())) {
            Booking booking = bookingMapper.bookingWithPasswordToBooking(bookingWithPassword);
            booking.setEncryptedPassword(applySha256(bookingWithPassword.getPassword()));
            return blockChainService.createBlock(booking);
        }
        return false;
    }

    public boolean deleteBooking(BookingWithPassword deleteBookingDto) {
        String password = applySha256(deleteBookingDto.getPassword());
        return findLastBlockByRoomNumberAndHourStart(deleteBookingDto.getRoomNumber(), deleteBookingDto.getHourStart())
                .filter(booking -> password.equals(booking.getEncryptedPassword()))
                .map(booking -> buildEmptyBooking(deleteBookingDto.getRoomNumber(), deleteBookingDto.getHourStart()))
                .map(blockChainService::createBlock)
                .orElse(false);
    }

    private boolean available(String roomNumber, Integer hourStart) {
        return findLastBlockByRoomNumberAndHourStart(roomNumber, hourStart)
                .map(booking -> booking.getHourStart().equals(hourStart) && booking.getUserName().isEmpty())
                .orElse(false);
    }

    private Collection<SimpleBooking> getSimpleBookingFromBlockChain() {
        return roomService.getRooms().stream()
                .map(roomNumber -> IntStream.range(MIN_HOUR, MAX_HOUR + 1)
                        .mapToObj(hourStart -> findLastBlockByRoomNumberAndHourStart(roomNumber, hourStart))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
                )
                .flatMap(Collection::stream)
                .map(bookingMapper::bookingToSimpleBooking)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    private Optional<Booking> findLastBlockByRoomNumberAndHourStart(String roomNumber, Integer hourStart) {
        return blockChainService.getBlockChain().stream()
                .map(Block::getData)
                .map(STRING_TO_BOOKING)
                .filter(booking -> booking.getRoomNumber().equals(roomNumber))
                .filter(booking -> booking.getHourStart().equals(hourStart))
                .peek(booking -> log.debug(booking.toString()))
                .findFirst();
    }

    private Booking buildEmptyBooking(String roomNumber, int hourStart) {
        return Booking.builder()
                .hourStart(hourStart)
                .roomNumber(roomNumber)
                .userName(EMPTY_STRING)
                .encryptedPassword(EMPTY_STRING)
                .build();
    }

}

package gr.harborlab.service;

import gr.harborlab.entities.Booking;
import gr.harborlab.exception.ExceptionUtils;
import gr.harborlab.repositories.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBookings_Success() {
        Long roomId = 1L;
        LocalDate date = LocalDate.of(2024, 12, 10);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setRoomId(roomId);
        booking.setBookingDate(date);

        when(bookingRepository.findByRoomIdAndBookingDateAndActive(roomId, date,true))
                .thenReturn(Collections.singletonList(booking));

        List<Booking> bookings = bookingService.getBookings(roomId, date);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
        verify(bookingRepository, times(1)).findByRoomIdAndBookingDateAndActive(roomId, date,true);
    }

    @Test
    void testCreateBooking_Success() {
        Booking booking = new Booking();
        booking.setRoomId(1L);
        booking.setEmployeeEmail("john.doe@acme.com");
        booking.setBookingDate(LocalDate.of(2024, 12, 10));
        booking.setTimeFrom(LocalTime.of(9, 0));
        booking.setTimeTo(LocalTime.of(11, 0));

        when(bookingRepository.findByRoomIdAndBookingDate(booking.getRoomId(), booking.getBookingDate()))
                .thenReturn(Collections.emptyList());
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking savedBooking = bookingService.createBooking(booking);

        assertNotNull(savedBooking);
        assertEquals("john.doe@acme.com", savedBooking.getEmployeeEmail());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testCreateBooking_FailsDueToOverlap() {
        Booking existingBooking = new Booking();
        existingBooking.setTimeFrom(LocalTime.of(10, 0));
        existingBooking.setTimeTo(LocalTime.of(12, 0));

        Booking newBooking = new Booking();
        newBooking.setRoomId(1L);
        newBooking.setBookingDate(LocalDate.of(2024, 12, 10));
        newBooking.setTimeFrom(LocalTime.of(11, 0));
        newBooking.setTimeTo(LocalTime.of(13, 0));

        when(bookingRepository.findByRoomIdAndBookingDateWithTimeRange(newBooking.getRoomId(), newBooking.getBookingDate(), newBooking.getTimeFrom(), newBooking.getTimeTo()))
                .thenReturn(Collections.singletonList(existingBooking));

        ExceptionUtils exception = assertThrows(ExceptionUtils.class, () -> bookingService.createBooking(newBooking));

        assertEquals("Time slot already booked.", exception.getMessage());
        assertEquals(ExceptionUtils.ErrorType.BAD_REQUEST, exception.getErrorType());
    }

    @Test
    void testDeleteBooking_Success() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingDate(LocalDate.of(2024, 12, 10));

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.deleteBooking(1L);

        verify(bookingRepository, times(1)).delete(booking);
    }

    @Test
    void testCancelBooking_Success() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingDate(LocalDate.of(2024, 12, 30));

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(1L);

        assertEquals(false, booking.getActive());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testCancelBooking_FailsWhenNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        ExceptionUtils exception = assertThrows(ExceptionUtils.class,
                () -> bookingService.cancelBooking(1L));

        assertEquals("Booking not found.", exception.getMessage());
        assertEquals(ExceptionUtils.ErrorType.RESOURCE_NOT_FOUND, exception.getErrorType());
    }

    @Test
    void testCancelBooking_FailsForPastBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingDate(LocalDate.of(2024, 1, 1)); // Past date

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        ExceptionUtils exception = assertThrows(ExceptionUtils.class,
                () -> bookingService.cancelBooking(1L));

        assertEquals("Cannot cancel past bookings.", exception.getMessage());
        assertEquals(ExceptionUtils.ErrorType.BAD_REQUEST, exception.getErrorType());
    }
}
package gr.harborlab.service;

import gr.harborlab.entities.Booking;
import gr.harborlab.exception.ExceptionUtils;
import gr.harborlab.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;


    //get bookings but exclude inactive bookings by checking active status.
    //you could use the: bookingRepository.findByRoomIdAndBookingDate ,if active status is reduntant
    public List<Booking> getBookings(Long roomId, LocalDate date) {
        return bookingRepository.findByRoomIdAndBookingDateAndActive(roomId, date, true);
    }

    @Transactional
    public Booking createBooking(Booking booking) {

        List<Booking> existingBookings = bookingRepository.findByRoomIdAndBookingDateWithTimeRange(booking.getRoomId(), booking.getBookingDate(), booking.getTimeFrom(), booking.getTimeTo());
        if (!existingBookings.isEmpty())
            throw new ExceptionUtils(ExceptionUtils.ErrorType.BAD_REQUEST, "Time slot already booked.");

        return bookingRepository.save(booking);
    }


    //soft delete bookings
    @Transactional
    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ExceptionUtils(ExceptionUtils.ErrorType.RESOURCE_NOT_FOUND, "Booking not found."));
        if (booking.getBookingDate().isBefore(LocalDate.now())) {
            throw new ExceptionUtils(ExceptionUtils.ErrorType.BAD_REQUEST, "Cannot cancel past bookings.");
        }
        if (!booking.getActive()) {
            throw new ExceptionUtils(ExceptionUtils.ErrorType.BAD_REQUEST, "Booking is already cancelled");
        }

        booking.setActive(false);

        bookingRepository.save(booking);

    }


    //hard delete bookings if needed
    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ExceptionUtils(ExceptionUtils.ErrorType.RESOURCE_NOT_FOUND, "Booking not found."));
        if (booking.getBookingDate().isBefore(LocalDate.now())) {
            throw new ExceptionUtils(ExceptionUtils.ErrorType.BAD_REQUEST, "Cannot cancel past bookings.");
        }
        bookingRepository.delete(booking);
    }
}

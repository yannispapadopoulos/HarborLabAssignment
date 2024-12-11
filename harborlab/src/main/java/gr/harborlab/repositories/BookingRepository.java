package gr.harborlab.repositories;

import gr.harborlab.entities.Booking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRoomIdAndBookingDate(Long roomId, LocalDate date);

    List<Booking> findByRoomIdAndBookingDateAndActive(Long roomId, LocalDate date,Boolean active);

    List<Booking> findAll();

    //add lock to protect from raise conditions
    //for better performance we could create a unique constraint composite key in db
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Booking b WHERE b.roomId = :roomId AND b.bookingDate = :bookingDate " +
            "AND b.timeFrom < :timeTo AND b.timeTo > :timeFrom")
    List<Booking> findByRoomIdAndBookingDateWithTimeRange(
            @Param("roomId") Long roomId,
            @Param("bookingDate") LocalDate bookingDate,
            @Param("timeFrom") LocalTime timeFrom,
            @Param("timeTo") LocalTime timeTo);
}
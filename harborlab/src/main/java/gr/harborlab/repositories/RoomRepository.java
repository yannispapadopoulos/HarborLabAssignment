package gr.harborlab.repositories;

import gr.harborlab.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import gr.harborlab.entities.Room;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByName(String name);

    List<Room> findAll();
}



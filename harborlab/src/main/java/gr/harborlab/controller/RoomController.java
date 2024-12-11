package gr.harborlab.controller;

import gr.harborlab.entities.Room;
import gr.harborlab.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping("/{id}")
    public Optional<Room> getExampleById(@PathVariable Long id) {
        return roomRepository.findById(id);
    }


    @GetMapping
    public List<Room> getRooms() {
        return roomRepository.findAll();
    }
}

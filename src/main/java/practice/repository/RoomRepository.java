package practice.repository;

import org.springframework.data.repository.CrudRepository;

import practice.model.Room;

public interface RoomRepository extends CrudRepository<Room, Long>{

}

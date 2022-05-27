package practice.repository;

import org.springframework.data.repository.CrudRepository;

import practice.model.Bill;

public interface BillRepository extends CrudRepository<Bill, Long>{

}

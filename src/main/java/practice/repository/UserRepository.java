package practice.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import practice.model.Account;
import practice.model.User;

public interface UserRepository extends CrudRepository<User, Long>{

//	Optional<User> findByAccount(Account account);

//	Optional<Account> findByUsername(String username);

}

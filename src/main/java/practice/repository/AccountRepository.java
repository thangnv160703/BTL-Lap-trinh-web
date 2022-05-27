package practice.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import practice.model.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {

	Optional<Account> findByUsername(String username);

}

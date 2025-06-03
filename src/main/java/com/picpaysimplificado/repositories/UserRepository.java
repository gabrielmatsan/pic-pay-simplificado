package com.picpaysimplificado.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.picpaysimplificado.domain.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findUserByCpf(String cpf);

  Optional<User> findUserById(String id);

  Optional<User> findUserByEmail(String email);
}

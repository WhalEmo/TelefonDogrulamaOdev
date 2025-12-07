package com.emrullah.phoneNumberVerify;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    long countByPhoneIsNotNull();

    Page<User> findAll(Pageable pageable);
}

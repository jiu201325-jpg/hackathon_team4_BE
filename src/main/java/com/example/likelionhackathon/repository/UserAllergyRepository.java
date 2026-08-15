package com.example.likelionhackathon.repository;

import com.example.likelionhackathon.entity.User;
import com.example.likelionhackathon.entity.UserAllergy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAllergyRepository extends JpaRepository<UserAllergy, Long> {

    List<UserAllergy> findAllByUser(User user);

    void deleteAllByUser(User user);
}
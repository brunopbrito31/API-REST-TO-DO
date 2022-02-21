package com.brunopbrito31.apitodo.repositories;

import java.util.Optional;

import com.brunopbrito31.apitodo.models.entities.UserModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserModelRepository extends JpaRepository<UserModel, Long> {

    public Optional<UserModel> findByLogin(String login);
}

package com.example.carrental.repository;

import com.example.carrental.model.PriceUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceUpdateRepository extends JpaRepository<PriceUpdate, Long> {
}

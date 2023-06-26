package com.example.carrental.repository;

import com.example.carrental.model.PriceUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PriceUpdateRepository extends JpaRepository<PriceUpdate, Long> {

    Optional<PriceUpdate> findFirstByCar_IdAndUpdateDateBeforeOrderByUpdateDateDesc(long carId, LocalDateTime date);
}

package com.example.carrental.priceUpdate;

import com.example.carrental.priceUpdate.PriceUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PriceUpdateRepository extends JpaRepository<PriceUpdate, Long> {

    Optional<PriceUpdate> findFirstByCar_IdAndUpdateDateBeforeOrderByUpdateDateDesc(long carId, LocalDateTime date);
    Page<PriceUpdate> findAll(Pageable page);
}

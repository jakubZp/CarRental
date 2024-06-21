package com.example.carrental.priceUpdate;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PriceUpdateDTOMapper implements Function<PriceUpdate, PriceUpdateDTO> {
    @Override
    public PriceUpdateDTO apply(PriceUpdate priceUpdate) {
        return new PriceUpdateDTO(
                priceUpdate.getUpdateDate(),
                priceUpdate.getPrice(),
                priceUpdate.getCar().getId()
        );
    }
}
package com.example.carrental.controller.mapper;

import com.example.carrental.controller.dto.PriceUpdateDTO;
import com.example.carrental.model.PriceUpdate;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PriceUpdateDTOMapper implements Function<PriceUpdate, PriceUpdateDTO> {
    @Override
    public PriceUpdateDTO apply(PriceUpdate priceUpdate) {
        return new PriceUpdateDTO(
                priceUpdate.getId(),
                priceUpdate.getUpdateDate(),
                priceUpdate.getPrice(),
                priceUpdate.getCar().getId());
    }
}

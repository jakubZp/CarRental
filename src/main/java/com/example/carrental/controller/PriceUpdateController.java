package com.example.carrental.controller;

import com.example.carrental.controller.dto.PriceUpdateDTO;
import com.example.carrental.controller.mapper.PriceUpdateDTOMapper;
import com.example.carrental.model.PriceUpdate;
import com.example.carrental.service.PriceUpdateService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/priceupdates")
@AllArgsConstructor
public class PriceUpdateController {

    private final PriceUpdateService priceUpdateService;
    private final PriceUpdateDTOMapper priceUpdateDTOMapper;

    @GetMapping
    public List<PriceUpdateDTO> getPriceUpdates() {
        return priceUpdateService.getPriceUpdates()
                .stream()
                .map(priceUpdateDTOMapper)
                .collect(Collectors.toList());
    }

    @PostMapping
    public PriceUpdate addPriceUpdate(@RequestBody PriceUpdateDTO priceUpdate) {
        return priceUpdateService.addPriceUpdate(priceUpdate);
    }
}

package com.example.carrental.controller;

import com.example.carrental.controller.dto.PriceUpdateDTO;
import com.example.carrental.model.PriceUpdate;
import com.example.carrental.service.PriceUpdateService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/priceupdates")
@AllArgsConstructor
public class PriceUpdateController {
    private final PriceUpdateService priceUpdateService;

    @GetMapping
    public List<PriceUpdate> getPriceUpdates() {
        return priceUpdateService.getPriceUpdates();
    }

    @PostMapping
    public PriceUpdate addPriceUpdate(@RequestBody PriceUpdateDTO priceUpdate) {
        return priceUpdateService.addPriceUpdate(priceUpdate);
    }
}

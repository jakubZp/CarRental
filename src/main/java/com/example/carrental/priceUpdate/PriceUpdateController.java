package com.example.carrental.priceUpdate;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PostMapping
    public PriceUpdate addPriceUpdate(@RequestBody PriceUpdateDTO priceUpdate) {
        return priceUpdateService.addPriceUpdate(priceUpdate);
    }
}

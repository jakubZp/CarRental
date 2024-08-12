package com.example.carrental.priceUpdate;

import com.example.carrental.exceptions.CannotFindPriceException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/priceupdates")
@AllArgsConstructor
public class PriceUpdateController {

    private final PriceUpdateService priceUpdateService;
    private final PriceUpdateDTOMapper priceUpdateDTOMapper;

    @GetMapping
    public List<PriceUpdateDTO> getPriceUpdates(@RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) Integer pageSize) {
        Page<PriceUpdate> priceUpdatePage = priceUpdateService.getPriceUpdates(page, pageSize);
        return priceUpdatePage
                .stream()
                .map(priceUpdateDTOMapper)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PostMapping
    public PriceUpdate addPriceUpdate(@Valid @RequestBody PriceUpdateDTO priceUpdate) {
        return priceUpdateService.addPriceUpdate(priceUpdate);
    }

    @GetMapping("/{id}/{date}")
    public BigDecimal getPriceOnDate(@PathVariable Long id, @PathVariable LocalDateTime date) {
        return priceUpdateService.findPriceOnDate(id, date);
    }
}

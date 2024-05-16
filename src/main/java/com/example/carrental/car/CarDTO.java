package com.example.carrental.car;

import java.math.BigDecimal;

record CarDTO(Long carId,
              String brand,
              String model,
              Integer productionYear,
              BigDecimal actualDailyPrice) {
}

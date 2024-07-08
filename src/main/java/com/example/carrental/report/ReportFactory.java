package com.example.carrental.report;

import com.example.carrental.rental.Rental;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface ReportFactory {
    void generateReportForRentals(List<Rental> rentals, HttpServletResponse response);
}

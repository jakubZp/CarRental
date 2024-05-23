package com.example.carrental.rental.report;

import com.example.carrental.rental.Rental;
import com.example.carrental.priceUpdate.PriceUpdateService;
import com.example.carrental.rental.RentalService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportPDFService {

    private final RentalService rentalService;
    private final PriceUpdateService priceUpdateService;
    private BigDecimal earnedMoney = BigDecimal.ZERO;

    public void generateRentalsReport(List<Rental> rentals, HttpServletResponse response) {

        response.setContentType("application/pdf");
        String headerkey = "Content-Disposition";

        Collections.sort(rentals);
        LocalDateTime fromDate = rentals.get(0).getFromDate();
        LocalDateTime toDate = rentals.get(rentals.size()-1).getToDate();

        String headervalue = "attachment; filename=rentals_" + fromDate + "_to_" + toDate + ".pdf";
        response.setHeader(headerkey, headervalue);

        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("Error before generating PDF", e);
        }

        document.open();

        Paragraph title = new Paragraph("Rentals report for: " + fromDate + " to " + toDate);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        addRentalsTable(document, rentals);
        Paragraph footer = new Paragraph("Sum: " + earnedMoney);
        footer.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(footer);

        document.close();

    }

    private void addRentalsTable(Document document, List<Rental> rentals) {

        String [] columnNames = {"rental id", "from date", "to date", "car id", "customer id", "days", "daily price", "earning"};
        PdfPTable table = new PdfPTable(columnNames.length);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2.1F, 3.8F, 3.8F, 1.5F, 2.2F, 1.3F, 1.6F, 2.3F});
        table.setSpacingBefore(5);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.GRAY);
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        for(String name: columnNames) {
            cell.setPhrase(new Phrase(name));
            table.addCell(cell);
        }

        //Collections.sort(rentals);
        for(Rental r: rentals) {
            table.addCell(String.valueOf(r.getId()));
            table.addCell(String.valueOf(r.getFromDate()));
            table.addCell(String.valueOf(r.getToDate()));
            table.addCell(String.valueOf(r.getCar().getId()));
            table.addCell(String.valueOf(r.getCustomer().getId()));
            table.addCell(String.valueOf(Duration.between(r.getFromDate(), r.getToDate()).toDays()));
            BigDecimal dailyPrice = priceUpdateService.findPriceOnDate(r.getCar().getId(), r.getFromDate());
            table.addCell(String.valueOf(dailyPrice));
            BigDecimal currentEarning = rentalService.calculateEarning(r);
            table.addCell(String.valueOf(currentEarning));
            earnedMoney = earnedMoney.add(currentEarning);
        }

        document.add(table);
    }

//    public BigDecimal calculateEarning(Rental r) {
//        BigDecimal result;
//        long days = Duration.between(r.getFromDate(), r.getToDate()).toDays();
//        Optional<BigDecimal> priceOnStartingDay = priceUpdateService.findPriceOnDate(r.getCar().getId(), r.getFromDate());
//        BigDecimal price = priceOnStartingDay.orElse(BigDecimal.valueOf(0.0));
//
//        result = BigDecimal.valueOf(days).multiply(price);
//
//        earnedMoney = earnedMoney.add(result);
//
//        return result;
//    }
}

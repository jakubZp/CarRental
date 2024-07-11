package com.example.carrental.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportFactoryProvider {

    private final PDFReportFactoryService pdfReportFactoryService;
    private final ExcelReportFactoryService excelReportFactoryService;

    public ReportFactory getReportFactory(String format) {
        return switch (format.toLowerCase()) {
            case "pdf" -> pdfReportFactoryService;
            case "excel" -> excelReportFactoryService;
            default -> throw new IllegalArgumentException("Unknown report type: " + format);
        };
    }
}

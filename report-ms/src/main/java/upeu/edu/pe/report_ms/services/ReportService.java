package upeu.edu.pe.report_ms.services;

public interface ReportService {
    String generarReportePrestamos(Long clienteId, String estado);
    String generarReporteCuotas(Long clienteId, String estado);
}


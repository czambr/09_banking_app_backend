package com.czambra.mscuentas.controller;

import com.czambra.mscuentas.dto.reporte.ReportePaginadoDTO;
import com.czambra.mscuentas.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public ResponseEntity<?> obtenerReporte(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam Long clienteId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        if (fechaInicio == null || fechaFin == null || clienteId == null) {
            return ResponseEntity.badRequest().build();
        }

        if (fechaInicio.isAfter(fechaFin)) {
            return ResponseEntity.badRequest().build();
        }

        ReportePaginadoDTO reportes = reporteService.generarReporte(clienteId, fechaInicio, fechaFin, page, size);
        return ResponseEntity.ok(reportes);
    }
}

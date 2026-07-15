package com.czambra.mscuentas.controller;

import com.czambra.mscuentas.dto.cuenta.CuentaCreateDTO;
import com.czambra.mscuentas.dto.cuenta.CuentaResponseDTO;
import com.czambra.mscuentas.dto.cuenta.CuentaUpdateDTO;
import com.czambra.mscuentas.dto.movimiento.MovimientoCreateDTO;
import com.czambra.mscuentas.dto.movimiento.MovimientoResponseDTO;
import com.czambra.mscuentas.service.CuentaService;
import com.czambra.mscuentas.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@CrossOrigin(origins = "*")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping
    public ResponseEntity<List<CuentaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(cuentaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.obtenerPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CuentaResponseDTO>> obtenerPorClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(cuentaService.obtenerPorClienteId(clienteId));
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody CuentaCreateDTO dto) {
        try {
            CuentaResponseDTO cuenta = cuentaService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(cuenta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody CuentaUpdateDTO dto) {
        try {
            CuentaResponseDTO cuenta = cuentaService.actualizar(id, dto);
            return ResponseEntity.ok(cuenta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            cuentaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/movimientos")
    public ResponseEntity<?> registrarMovimiento(@PathVariable Long id, @Valid @RequestBody MovimientoCreateDTO dto) {
        try {
            dto.setCuentaId(id);
            MovimientoResponseDTO movimiento = movimientoService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}/movimientos")
    public ResponseEntity<?> obtenerMovimientos(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        if (page >= 1 && size > 0) {
            return ResponseEntity.ok(movimientoService.obtenerPorCuentaIdPaginado(id, page - 1, size));
        }
        return ResponseEntity.ok(movimientoService.obtenerPorCuentaId(id));
    }
}

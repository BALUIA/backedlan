package com.souldevec.security.controllers;

import com.souldevec.security.dtos.GastoDto;
import com.souldevec.security.dtos.GastoResponseDto;
import com.souldevec.security.entities.Caja;
import com.souldevec.security.entities.Gasto;
import com.souldevec.security.services.CajaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caja")
public class CajaController {

    @Autowired
    private CajaService cajaService;

    @GetMapping
    public ResponseEntity<Caja> getCaja() {
        return ResponseEntity.ok(cajaService.getCaja());
    }

    @PostMapping("/gastos")
    public ResponseEntity<Gasto> registrarGasto(@RequestBody GastoDto gastoDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Gasto gasto = cajaService.registrarGasto(gastoDto, userName);
        return ResponseEntity.ok(gasto);
    }

    @GetMapping("/gastos")
    public ResponseEntity<List<GastoResponseDto>> getGastos() {
        return ResponseEntity.ok(cajaService.getGastos());
    }
}

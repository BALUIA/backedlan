package com.souldevec.security.services;

import com.souldevec.security.dtos.GastoDto;
import com.souldevec.security.dtos.GastoResponseDto;
import com.souldevec.security.entities.Caja;
import com.souldevec.security.entities.Gasto;
import com.souldevec.security.entities.User;
import com.souldevec.security.repositories.CajaRepository;
import com.souldevec.security.repositories.GastoRepository;
import com.souldevec.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CajaService {

    @Autowired
    private CajaRepository cajaRepository;

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private UserRepository userRepository;

    public Caja getCaja() {
        return cajaRepository.findAll().stream().findFirst().orElseGet(() -> {
            Caja nuevaCaja = new Caja();
            nuevaCaja.setBalance(BigDecimal.ZERO);
            return cajaRepository.save(nuevaCaja);
        });
    }

    public Gasto registrarGasto(GastoDto gastoDto, String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Caja caja = getCaja();
        BigDecimal gastoAmount = gastoDto.getAmount();

        if (caja.getBalance().compareTo(gastoAmount) < 0) {
            throw new RuntimeException("No hay suficiente saldo en la caja para registrar el gasto.");
        }

        caja.setBalance(caja.getBalance().subtract(gastoAmount));
        cajaRepository.save(caja);

        Gasto gasto = new Gasto();
        gasto.setDescription(gastoDto.getDescription());
        gasto.setAmount(gastoAmount);
        gasto.setTimestamp(LocalDateTime.now());
        gasto.setUser(user);
        return gastoRepository.save(gasto);
    }

    public List<GastoResponseDto> getGastos() {
        return gastoRepository.findAllByOrderByTimestampDesc().stream()
                .map(this::mapToGastoResponseDto)
                .collect(Collectors.toList());
    }

    public void agregarEfectivo(BigDecimal monto) {
        Caja caja = getCaja();
        caja.setBalance(caja.getBalance().add(monto));
        cajaRepository.save(caja);
    }

    private GastoResponseDto mapToGastoResponseDto(Gasto gasto) {
        GastoResponseDto dto = new GastoResponseDto();
        dto.setId(gasto.getId());
        dto.setDescription(gasto.getDescription());
        dto.setAmount(gasto.getAmount());
        dto.setTimestamp(gasto.getTimestamp());
        dto.setUserName(gasto.getUser().getUserName());
        return dto;
    }
}

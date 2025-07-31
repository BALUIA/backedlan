package com.souldevec.security.services;

import com.souldevec.security.dtos.*;
import com.souldevec.security.entities.*;
import com.souldevec.security.repositories.*;
import com.souldevec.security.enums.RoleList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TurnoService {

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryMovementRepository inventoryMovementRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RetiroDetalleRepository retiroDetalleRepository;

    @Autowired
    private CajaService cajaService;

    

    public TurnoResponseDto save(CreateTurnoDto createTurnoDto, String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Turno turno = new Turno();
        TurnoDto turnoDto = createTurnoDto.getTurno();
        turno.setFecha(turnoDto.getFecha());
        turno.setHoraEntrada(turnoDto.getHoraEntrada());
        turno.setHoraSalida(turnoDto.getHoraSalida());
        turno.setEfectivo(turnoDto.getEfectivo());
        turno.setYape(turnoDto.getYape());
        turno.setSnacks(turnoDto.getSnacks());
        turno.setIngresoInventario(turnoDto.getIngresoInventario());
        turno.setConsumo(turnoDto.getConsumo());
        turno.setRetiros(turnoDto.getRetiros());
        turno.setDineroPancafe(turnoDto.getDineroPancafe());
        turno.setUsanzaPancafe(turnoDto.getUsanzaPancafe());
        turno.setKw(turnoDto.getKw());
        turno.setUsuarios(turnoDto.getUsuarios());
        turno.setUser(user);
        Turno savedTurno = turnoRepository.save(turno);

        for (InventoryMovementDto movementDto : createTurnoDto.getMovements()) {
            Product product = productRepository.findById(movementDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            InventoryMovement movement = new InventoryMovement();
            movement.setProduct(product);
            movement.setTurno(savedTurno);
            movement.setQuantity(movementDto.getQuantity());
            movement.setType(movementDto.getType());
            movement.setTimestamp(java.time.LocalDateTime.now());
            if (movementDto.getType() == com.souldevec.security.enums.MovementType.OUT) {
                if (product.getStock() < movementDto.getQuantity()) {
                    throw new RuntimeException("No hay stock suficiente para el producto: " + product.getName());
                }
                product.setStock(product.getStock() - movementDto.getQuantity());
            } else {
                product.setStock(product.getStock() + movementDto.getQuantity());
            }
            movement.setStockAfterMovement(product.getStock());
            productRepository.save(product);

            inventoryMovementRepository.save(movement);
        }

        for (RetiroDetalleDto retiroDto : createTurnoDto.getRetiros()) {
            RetiroDetalle retiroDetalle = new RetiroDetalle();
            retiroDetalle.setDescription(retiroDto.getDescription());
            retiroDetalle.setAmount(retiroDto.getAmount());
            retiroDetalle.setTurno(savedTurno);
            retiroDetalleRepository.save(retiroDetalle);
        }

        cajaService.agregarEfectivo(savedTurno.getEfectivo());

        return mapToTurnoResponseDto(savedTurno);
    }

    public List<TurnoResponseDto> findAll() {
        return turnoRepository.findAllWithUser().stream().map(this::mapToTurnoResponseDto).collect(Collectors.toList());
    }

    public void deleteById(Long id, String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        boolean isAdmin = user.getRole().getName().equals(RoleList.ROLE_ADMIN);

        if (!isAdmin) {
            throw new RuntimeException("No tienes permiso para eliminar este turno");
        }

        // Revertir el efectivo de la caja
        cajaService.agregarEfectivo(turno.getEfectivo().negate());

        // Eliminar dependencias
        List<InventoryMovement> movements = inventoryMovementRepository.findByTurno(turno);
        inventoryMovementRepository.deleteAll(movements);

        List<RetiroDetalle> retiros = retiroDetalleRepository.findByTurno(turno);
        retiroDetalleRepository.deleteAll(retiros);

        // Finalmente, eliminar el turno
        turnoRepository.deleteById(id);
    }

    public TurnoSummaryDto getTurnoSummary(Long turnoId) {
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        List<InventoryMovement> movements = inventoryMovementRepository.findByTurno(turno);
        List<RetiroDetalle> retiros = retiroDetalleRepository.findByTurno(turno);

        List<InventoryMovementResponseDto> movementDtos = movements.stream().map(movement -> {
            InventoryMovementResponseDto dto = new InventoryMovementResponseDto();
            dto.setProductName(movement.getProduct().getName());
            dto.setQuantity(movement.getQuantity());
            dto.setType(movement.getType());
            dto.setPrice(movement.getProduct().getSellingPrice());
            dto.setTotalPrice(movement.getProduct().getSellingPrice().multiply(new java.math.BigDecimal(movement.getQuantity())));
            dto.setStockAfterMovement(movement.getStockAfterMovement());
            return dto;
        }).collect(Collectors.toList());

        List<RetiroDetalleDto> retiroDtos = retiros.stream().map(retiro -> {
            RetiroDetalleDto dto = new RetiroDetalleDto();
            dto.setDescription(retiro.getDescription());
            dto.setAmount(retiro.getAmount());
            return dto;
        }).collect(Collectors.toList());

        java.math.BigDecimal totalSales = movementDtos.stream()
                .filter(dto -> dto.getType() == com.souldevec.security.enums.MovementType.OUT)
                .map(InventoryMovementResponseDto::getTotalPrice)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        TurnoSummaryDto summaryDto = new TurnoSummaryDto();
        summaryDto.setMovements(movementDtos);
        summaryDto.setRetiros(retiroDtos);
        summaryDto.setTotalSales(totalSales);
        summaryDto.setTurno(mapToTurnoResponseDto(turno));

        return summaryDto;
    }

    private TurnoResponseDto mapToTurnoResponseDto(Turno turno) {
        TurnoResponseDto dto = new TurnoResponseDto();
        dto.setId(turno.getId());
        dto.setFecha(turno.getFecha());
        dto.setHoraEntrada(turno.getHoraEntrada());
        dto.setHoraSalida(turno.getHoraSalida());
        dto.setEfectivo(turno.getEfectivo());
        dto.setYape(turno.getYape());
        dto.setSnacks(turno.getSnacks());
        dto.setIngresoInventario(turno.getIngresoInventario());
        dto.setConsumo(turno.getConsumo());
        dto.setRetiros(turno.getRetiros());
        dto.setDineroPancafe(turno.getDineroPancafe());
        dto.setUsanzaPancafe(turno.getUsanzaPancafe());
        dto.setKw(turno.getKw());
        dto.setUsuarios(turno.getUsuarios());
        dto.setUserName(turno.getUser().getUserName());
        return dto;
    }
}


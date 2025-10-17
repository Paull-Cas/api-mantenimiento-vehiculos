package com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.impl;

import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoCorrectivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.request.MantenimientoPreventivoRequestDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.dtos.response.MantenimientoResponseDTO;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.Mantenimiento;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.MantenimientoCorrectivo;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.entities.MantenimientoPreventivo;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.exceptions.MantenimientoNoEncontradoExcepcion;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.mappers.MantenimientoMapper;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.repositories.MantenimientoRepositorio;
import com.mantenimiento.vehiculos.api.mantenimiento.vehiculos.services.MantenimientoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class MantenimientoServiceImpl implements MantenimientoService {

    @Autowired
    private MantenimientoRepositorio mantenimientoRepositorio;
    @Autowired
    private MantenimientoMapper mantenimientoMapper;

    @Override
    @Transactional
    public MantenimientoResponseDTO saveMantenimientoPreventivo(MantenimientoPreventivoRequestDTO mantPrevenRequestDTO) {
        log.info("Guardar mantenimiento preventivo");
        MantenimientoPreventivo mantenimientoPreventivo = mantenimientoMapper.mapearDeMantPreventivoRequest(mantPrevenRequestDTO);
        MantenimientoPreventivo mantenimientoPreventivoBD = mantenimientoRepositorio.save(mantenimientoPreventivo);
        return mantenimientoMapper.mapearDeMantResponseDTO(mantenimientoPreventivoBD);
    }

    @Override
    @Transactional
    public MantenimientoResponseDTO saveMantenimientoCorrectivo(MantenimientoCorrectivoRequestDTO mantCorrecRequestDTO) {
        log.info("Guardar mantenimiento correctivo");
        MantenimientoCorrectivo mantenimientoCorrectivo = mantenimientoMapper.mapearDeMantCorrectivoRequest(mantCorrecRequestDTO);
        MantenimientoCorrectivo mantenimientoCorrectivoBD = mantenimientoRepositorio.save(mantenimientoCorrectivo);
        return mantenimientoMapper.mapearDeMantResponseDTO(mantenimientoCorrectivoBD);
    }

    @Override
    @Transactional
    public List<MantenimientoResponseDTO> getTodosLosMantenimientos() {
        return mantenimientoRepositorio.findAll()
                .stream()
                .map(mantenimientoMapper::mapearDeMantResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MantenimientoResponseDTO getMantenimientoPorId(Long id) throws MantenimientoNoEncontradoExcepcion {
        Mantenimiento mantenimiento = mantenimientoRepositorio.findById(id)
                .orElseThrow(() -> new MantenimientoNoEncontradoExcepcion("Mantenimiento no encontrado con ID: " + id));
        return mantenimientoMapper.mapearDeMantResponseDTO(mantenimiento);
    }

    @Override
    @Transactional
    public MantenimientoResponseDTO updateMantenimientoPreventivo(Long idMantenimiento, MantenimientoPreventivoRequestDTO mantPrevenRequestDTO) throws MantenimientoNoEncontradoExcepcion {
            log.info("Actualizar mantenimiento preventivo");
            Mantenimiento mantenimientoExistente = mantenimientoRepositorio.findById(idMantenimiento)
                    .orElseThrow(() -> new MantenimientoNoEncontradoExcepcion("Mantenimiento no encontrado con ID: " + idMantenimiento));

            if (!(mantenimientoExistente instanceof MantenimientoPreventivo)) {
                throw new IllegalArgumentException("El mantenimiento con ID " + idMantenimiento + " no es de tipo Preventivo.");
            }

            MantenimientoPreventivo mantenimientoPreventivo = (MantenimientoPreventivo) mantenimientoExistente;

            mantenimientoPreventivo.setTipoMantenimiento(mantPrevenRequestDTO.getTipoMantenimiento());
            mantenimientoPreventivo.setDescripcion(mantPrevenRequestDTO.getDescripcion());
            mantenimientoPreventivo.setTiempoEmpleadoHoras(mantPrevenRequestDTO.getTiempoEmpleadoHoras());
            mantenimientoPreventivo.setKilometrajeRecomendado(mantPrevenRequestDTO.getKilometrajeRecomendado());
            mantenimientoPreventivo.setEsCambioDeComponente(mantPrevenRequestDTO.isEsCambioDeComponente());

            Mantenimiento mantenimientoActualizado = mantenimientoRepositorio.save(mantenimientoPreventivo);
            return mantenimientoMapper.mapearDeMantResponseDTO(mantenimientoActualizado);
    }

    @Override
    @Transactional
    public MantenimientoResponseDTO updateMantenimientoCorrectivo(Long idMantenimiento, MantenimientoCorrectivoRequestDTO mantCorrecRequestDTO) throws MantenimientoNoEncontradoExcepcion {
            log.info("Actualizar mantenimiento correctivo");
            Mantenimiento mantenimientoExistente = mantenimientoRepositorio.findById(idMantenimiento)
                    .orElseThrow(() -> new MantenimientoNoEncontradoExcepcion("Mantenimiento no encontrado con ID: " + idMantenimiento));

            if (!(mantenimientoExistente instanceof MantenimientoCorrectivo)) {
                throw new IllegalArgumentException("El mantenimiento con ID " + idMantenimiento + " no es de tipo Correctivo.");
            }

            MantenimientoCorrectivo mantenimientoCorrectivo = (MantenimientoCorrectivo) mantenimientoExistente;

            mantenimientoCorrectivo.setTipoMantenimiento(mantCorrecRequestDTO.getTipoMantenimiento());
            mantenimientoCorrectivo.setDescripcion(mantCorrecRequestDTO.getDescripcion());
            mantenimientoCorrectivo.setTiempoEmpleadoHoras(mantCorrecRequestDTO.getTiempoEmpleadoHoras());
            mantenimientoCorrectivo.setDiagnosticoInicial(mantCorrecRequestDTO.getDiagnosticoInicial());
            mantenimientoCorrectivo.setEsReparacionUrgente(mantCorrecRequestDTO.isEsReparacionUrgente());

            Mantenimiento mantenimientoActualizado = mantenimientoRepositorio.save(mantenimientoCorrectivo);
            return mantenimientoMapper.mapearDeMantResponseDTO(mantenimientoActualizado);
    }

    @Override
    @Transactional
    public void deleteMantenimiento(Long id) throws MantenimientoNoEncontradoExcepcion {
        log.info("Eliminar mantenimiento");
        if (!mantenimientoRepositorio.existsById(id)) {
            throw new MantenimientoNoEncontradoExcepcion("No se puede eliminar. Mantenimiento no encontrado con ID: " + id);
        }
        mantenimientoRepositorio.deleteById(id);
    }
}

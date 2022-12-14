package com.grupo2.desafioquality.controller;

import com.grupo2.desafioquality.dto.CreateDistrictDto;
import com.grupo2.desafioquality.model.District;
import com.grupo2.desafioquality.service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/** RestController que gerencia as rotas de bairro. */
@RestController
@RequestMapping("district")
@RequiredArgsConstructor
public class DistrictController {
    private final DistrictService districtService;

    /** Rota HTTP para criar o bairro no repositório
     * @param CreateDistrictDto createDistrictDto - DTO do bairro
     * @return District - retorna o bairro criado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public District createDistrict(@RequestBody @Valid CreateDistrictDto createDistrictDto) {
        return districtService.createDistrict(createDistrictDto);
    }
}

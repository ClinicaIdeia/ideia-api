package com.ideiaapi.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideiaapi.util.DocumentValidator;

@RestController
@RequestMapping("/validacao")
public class ValidacaoResource {

    @GetMapping("/{cpf}")
    public ResponseEntity<Boolean> busca(@PathVariable String cpf) {

        boolean isCPFValid = DocumentValidator.isCPFValid(DocumentValidator.cleanCPF(cpf));

        return new ResponseEntity<>(isCPFValid, HttpStatus.OK);
    }
}

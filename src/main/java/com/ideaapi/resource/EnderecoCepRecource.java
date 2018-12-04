package com.ideaapi.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideaapi.representation.EnderecoRepresentation;
import com.ideaapi.service.EnderecoCepService;

@RestController
@RequestMapping("/cep")
public class EnderecoCepRecource {

    @Autowired
    private EnderecoCepService enderecoCepService;

    @GetMapping("/{cep}")
    public ResponseEntity<EnderecoRepresentation> pesquisar(@PathVariable String cep) {
        final EnderecoRepresentation enderecoCep = this.enderecoCepService.getEnderecoCep(cep);
        return new ResponseEntity<>(enderecoCep, HttpStatus.OK);
    }

}

package com.ideiaapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ideiaapi.model.Motivo;
import com.ideiaapi.repository.MotivoRepository;

@Service
public class MotivoService {

    @Autowired
    private MotivoRepository motivoRepository;

    public List<Motivo> listaTodosMotivos() {
        return this.motivoRepository.findAll();
    }
    
}

package com.ideiaapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ideiaapi.model.Permissao;
import com.ideiaapi.repository.PermissaoRepository;

@Service
public class PermissaoService {

    @Autowired
    private PermissaoRepository permissaoRepository;

    public List<Permissao> listaTodasAsPermissoes() {
        return this.permissaoRepository.findAll();
    }
}

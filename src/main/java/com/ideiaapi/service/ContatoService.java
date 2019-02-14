package com.ideiaapi.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ideiaapi.model.Contato;
import com.ideiaapi.model.Empresa;
import com.ideiaapi.repository.ContatoRepository;

@Service
public class ContatoService {

    @Autowired
    private ContatoRepository contatoRepository;

    public void cadastraContatos(Empresa empresa) {
        List<Contato> contatos = empresa.getContatos();
        if (null != contatos && !contatos.isEmpty()) {
            contatos.forEach(this::cadastraContato);
        }
    }

    private Contato cadastraContato(Contato entity) {
        return this.contatoRepository.save(entity);
    }

    public Contato buscaContato(Long codigo) {
        Contato contato = this.contatoRepository.findOne(codigo);
        if (contato != null) {
            return contato;
        }

        return null;
    }

    public void deletaContato(Long codigo) {
        this.contatoRepository.delete(codigo);
    }

    public ResponseEntity<Contato> atualizaContato(Long codigo, Contato contato) {
        Contato contatoSalvo = this.buscaContato(codigo);
        BeanUtils.copyProperties(contato, contatoSalvo, "codigo");

        this.contatoRepository.save(contatoSalvo);
        return ResponseEntity.ok(contatoSalvo);
    }
}

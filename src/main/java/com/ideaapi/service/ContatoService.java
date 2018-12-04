package com.ideaapi.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ideaapi.model.Contato;
import com.ideaapi.model.Empresa;
import com.ideaapi.repository.ContatoRepository;

@Service
public class ContatoService {

    @Autowired
    private ContatoRepository contatoRepository;

    public List<Contato> listaTodasContatos() {
        return this.contatoRepository.findAll();
    }

    public void cadastraContatos(Empresa empresa) {
        List<Contato> contatos = empresa.getContatos();
        if (!contatos.isEmpty()) {
            contatos.forEach(contato -> this.cadastraContato(contato));
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
        Contato contatoSalva = this.buscaContato(codigo);
        BeanUtils.copyProperties(contato, contatoSalva, "codigo");

        this.contatoRepository.save(contatoSalva);
        return ResponseEntity.ok(contatoSalva);
    }
}

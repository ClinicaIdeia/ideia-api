package com.ideiaapi.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ideiaapi.model.Aptidao;
import com.ideiaapi.repository.AptidaoRepository;

@Service
public class AptidaoService {

    @Autowired
    private AptidaoRepository aptidaoRepository;

    public List<Aptidao> listaTodasAptidoes() {
        return this.aptidaoRepository.findAll();
    }

    public void cadastrarAptidoes(List<Aptidao> aptidoes) {
        if (!aptidoes.isEmpty()) {
            aptidoes.forEach(aptidao -> {
                        if (null == aptidao.getApto()) {
                            aptidao.setApto(false);
                        }
                        this.aptidaoRepository.save(aptidao);
                    }
            );
        }
    }

    public Aptidao buscaAptidao(Long codigo) {
        Aptidao aptidao = this.aptidaoRepository.findOne(codigo);
        if (aptidao != null) {
            return aptidao;
        }

        return null;
    }

    public ResponseEntity<Aptidao> atualizaAptidao(Long codigo, Aptidao aptidao) {
        Aptidao aptidaoSalva = this.buscaAptidao(codigo);
        BeanUtils.copyProperties(aptidao, aptidaoSalva, "codigo");

        this.aptidaoRepository.save(aptidaoSalva);
        return ResponseEntity.ok(aptidaoSalva);
    }
}

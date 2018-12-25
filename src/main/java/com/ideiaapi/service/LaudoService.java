package com.ideiaapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ideiaapi.model.Laudo;
import com.ideiaapi.repository.LaudoRepository;
import com.ideiaapi.repository.filter.LaudoFilter;
import com.ideiaapi.validate.LaudoValidate;

@Service
public class LaudoService {

    @Autowired
    private LaudoRepository laudoRepository;

    @Autowired
    private AptidaoService aptidaoService;

    @Autowired
    private LaudoValidate laudoValidate;

    public Page<Laudo> filtrar(LaudoFilter filter, Pageable pageable) {
        return this.laudoRepository.filtrar(filter, pageable);
    }

//    public Page<ResumoLaudo> resumo(LaudoFilter filter, Pageable pageable) {
//        return this.laudoRepository.resumir(filter, pageable);
//    }

    public Laudo cadastraLaudo(Laudo entity) {
        this.laudoValidate.fluxoCriacao(entity);

//        if (entity.getAtiva() == null) {
//            entity.setAtiva(true);
//        }

        this.aptidaoService.cadastrarAptidoes(entity.getAptidoes());

        return this.laudoRepository.save(entity);
    }

    public Laudo buscaLaudo(Long codigo) {
        Laudo laudo = this.laudoRepository.findOne(codigo);
        if (laudo != null) {
            return laudo;
        }

        return null;
    }

    public void deletaLaudo(Long codigo) {
        this.laudoRepository.delete(codigo);
    }

    public ResponseEntity<Laudo> atualizaLaudo(Long codigo, Laudo laudo) {
        Laudo laudoSalva = this.buscaLaudo(codigo);
        BeanUtils.copyProperties(laudo, laudoSalva, "codigo");

        this.laudoRepository.save(laudoSalva);
        return ResponseEntity.ok(laudoSalva);
    }
}

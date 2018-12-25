package com.ideiaapi.repository.laudo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ideiaapi.model.Laudo;
import com.ideiaapi.repository.filter.LaudoFilter;
import com.ideiaapi.repository.projection.ResumoLaudo;

public interface LaudoRepositoryQuery {

    Page<Laudo> filtrar(LaudoFilter laudoFilter, Pageable pageable);

    Page<ResumoLaudo> resumir(LaudoFilter laudoFilter, Pageable pageable);

}

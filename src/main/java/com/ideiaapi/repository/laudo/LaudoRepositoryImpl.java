package com.ideiaapi.repository.laudo;

import com.ideiaapi.model.*;
import com.ideiaapi.repository.filter.LaudoFilter;
import com.ideiaapi.repository.projection.ResumoLaudo;
import com.ideiaapi.repository.restricoes.paginacao.RestricoesPaginacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class LaudoRepositoryImpl extends RestricoesPaginacao implements LaudoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;
    private Integer castada = 0;

    @Override
    public Page<Laudo> filtrar(LaudoFilter laudoFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Laudo> criteria = builder.createQuery(Laudo.class);
        Root<Laudo> root = criteria.from(Laudo.class);
        criteria.orderBy(builder.desc(root.get(Laudo_.dataExame)));

        Predicate[] predicates = criarRestricoes(laudoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Laudo> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(laudoFilter));
    }


    @Override
    public Page<ResumoLaudo> resumir(LaudoFilter laudoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<ResumoLaudo> criteria = builder.createQuery(ResumoLaudo.class);
        Root<Laudo> root = criteria.from(Laudo.class);

        criteria.select(builder.construct(ResumoLaudo.class, root.get(Laudo_.codigo)));

        Predicate[] predicates = criarRestricoes(laudoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<ResumoLaudo> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(laudoFilter));
    }

    private Predicate[] criarRestricoes(LaudoFilter laudoFilter, CriteriaBuilder builder, Root<Laudo> root) {

        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(laudoFilter.getObservacao())) {
            predicates.add(builder.like(builder.lower(root.get(Laudo_.observacao)), "%" + laudoFilter.getObservacao().toLowerCase() + "%"));
        }

        if (!StringUtils.isEmpty(laudoFilter.getMotivo())) {
            predicates.add(builder.like(builder.lower(root.get(Laudo_.motivo).get(Motivo_.descricao)),
                    "%" + laudoFilter.getMotivo().toLowerCase() + "%"));
        }

        if (null != laudoFilter.getDataAgendamentoDe() && null != laudoFilter.getDataAgendamentoAte()) {

            predicates.add(builder.greaterThanOrEqualTo(root.get(Laudo_.agendamento).get(Agendamento_.agenda).get(Agenda_.diaAgenda),
                    laudoFilter.getDataAgendamentoDe()));

            predicates.add(builder.lessThanOrEqualTo(root.get(Laudo_.agendamento).get(Agendamento_.agenda).get(Agenda_.diaAgenda),
                    laudoFilter.getDataAgendamentoAte()));
        }

        if (null != laudoFilter.getCodEmpresa()) {

            predicates.add(builder.greaterThanOrEqualTo(root.get(Laudo_.agendamento).get(Agendamento_.empresa).get(Empresa_.codigo),
                    laudoFilter.getCodEmpresa()));

            predicates.add(builder.lessThanOrEqualTo(root.get(Laudo_.agendamento).get(Agendamento_.empresa).get(Empresa_.codigo),
                    laudoFilter.getCodEmpresa()));

        }

        if (null != laudoFilter.getCodFuncionario()) {

            predicates.add(builder.greaterThanOrEqualTo(root.get(Laudo_.agendamento).get(Agendamento_.funcionario).get(Funcionario_.codigo),
                    laudoFilter.getCodFuncionario()));

            predicates.add(builder.lessThanOrEqualTo(root.get(Laudo_.agendamento).get(Agendamento_.funcionario).get(Funcionario_.codigo),
                    laudoFilter.getCodFuncionario()));

        }


        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private Long total(LaudoFilter laudoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Laudo> root = criteria.from(Laudo.class);

        Predicate[] predicates = criarRestricoes(laudoFilter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

}

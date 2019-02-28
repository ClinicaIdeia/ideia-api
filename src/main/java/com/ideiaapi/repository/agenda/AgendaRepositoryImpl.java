package com.ideiaapi.repository.agenda;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.ideiaapi.model.Agenda;
import com.ideiaapi.model.Agenda_;
import com.ideiaapi.repository.filter.AgendaFilter;
import com.ideiaapi.repository.projection.ResumoAgenda;
import com.ideiaapi.repository.restricoes.paginacao.RestricoesPaginacao;

public class AgendaRepositoryImpl extends RestricoesPaginacao implements AgendaRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Agenda> filtrar(AgendaFilter agendaFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Agenda> criteria = builder.createQuery(Agenda.class);
        Root<Agenda> root = criteria.from(Agenda.class);
        criteria.orderBy(builder.desc(root.get(Agenda_.diaAgenda)));

        Predicate[] predicates = criarRestricoes(agendaFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Agenda> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(agendaFilter));
    }


    @Override
    public Page<ResumoAgenda> resumir(AgendaFilter agendaFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<ResumoAgenda> criteria = builder.createQuery(ResumoAgenda.class);
        Root<Agenda> root = criteria.from(Agenda.class);

        criteria.select(builder.construct(ResumoAgenda.class
                ,root.get(Agenda_.codigo)
                ,root.get(Agenda_.observacao)
                ,root.get(Agenda_.diaAgenda)
        ));

        Predicate[] predicates = criarRestricoes(agendaFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<ResumoAgenda> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(agendaFilter));
    }

    private Predicate[] criarRestricoes(AgendaFilter agendaFilter, CriteriaBuilder builder,
            Root<Agenda> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(agendaFilter.getObservacao())) {
            predicates.add(builder.like(
                    builder.lower(root.get(Agenda_.observacao)),
                    "%" + agendaFilter.getObservacao().toLowerCase() + "%"));
        }

        if (agendaFilter.getDiaAgendaDe() != null) {
            predicates.add(
                    builder.greaterThanOrEqualTo(root.get(Agenda_.diaAgenda),
                            agendaFilter.getDiaAgendaDe()));
        }

        if (agendaFilter.getDiaAgendaAte() != null) {
            predicates.add(
                    builder.lessThanOrEqualTo(root.get(Agenda_.diaAgenda),
                            agendaFilter.getDiaAgendaAte()));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private Long total(AgendaFilter agendamentoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Agenda> root = criteria.from(Agenda.class);

        Predicate[] predicates = criarRestricoes(agendamentoFilter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

}

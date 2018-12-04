package com.ideaapi.repository.agenda;

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

import com.ideaapi.model.Agenda;
import com.ideaapi.model.Agenda_;
import com.ideaapi.repository.filter.AgendaFilter;
import com.ideaapi.repository.projection.ResumoAgendamento;
import com.ideaapi.repository.restricoes.paginacao.RestricoesPaginacao;

public class AgendaRepositoryImpl extends RestricoesPaginacao implements AgendaRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Agenda> filtrar(AgendaFilter agendamentoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Agenda> criteria = builder.createQuery(Agenda.class);
        Root<Agenda> root = criteria.from(Agenda.class);

        Predicate[] predicates = criarRestricoes(agendamentoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Agenda> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(agendamentoFilter));
    }


    @Override
    public Page<ResumoAgendamento> resumir(AgendaFilter agendamentoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<ResumoAgendamento> criteria = builder.createQuery(ResumoAgendamento.class);
        Root<Agenda> root = criteria.from(Agenda.class);

        criteria.select(builder.construct(ResumoAgendamento.class
                , root.get(Agenda_.codigo)
                , root.get(Agenda_.observacao)
        ));

        Predicate[] predicates = criarRestricoes(agendamentoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<ResumoAgendamento> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(agendamentoFilter));
    }

    private Predicate[] criarRestricoes(AgendaFilter agendamentoFilter, CriteriaBuilder builder,
            Root<Agenda> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(agendamentoFilter.getObservacao())) {
            predicates.add(builder.like(
                    builder.lower(root.get(Agenda_.observacao)),
                    "%" + agendamentoFilter.getObservacao().toLowerCase() + "%"));
        }

//        if (agendamentoFilter.getDataExameDe() != null) {
//            predicates.add(
//                    builder.greaterThanOrEqualTo(root.get(Agenda_.horario).get(Horario_.dataExame),
//                            agendamentoFilter.getDataExameDe()));
//        }
//
//        if (agendamentoFilter.getDataExameAte() != null) {
//            predicates.add(
//                    builder.lessThanOrEqualTo(root.get(Agenda_.horario).get(Horario_.dataExame),
//                            agendamentoFilter.getDataExameAte()));
//        }

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

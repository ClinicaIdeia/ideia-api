package com.ideiaapi.repository.agendamento;

import java.time.LocalDate;
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

import com.ideiaapi.dto.AgendamentoEstatisticaEmpresa;
import com.ideiaapi.model.Agenda_;
import com.ideiaapi.model.Agendamento;
import com.ideiaapi.model.Agendamento_;
import com.ideiaapi.model.Empresa;
import com.ideiaapi.model.Empresa_;
import com.ideiaapi.model.Funcionario_;
import com.ideiaapi.model.Usuario;
import com.ideiaapi.repository.filter.AgendamentoFilter;
import com.ideiaapi.repository.projection.ResumoAgendamento;
import com.ideiaapi.repository.restricoes.paginacao.RestricoesPaginacao;
import com.ideiaapi.security.UsuarioSessao;

public class AgendamentoRepositoryImpl extends RestricoesPaginacao implements AgendamentoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<AgendamentoEstatisticaEmpresa> agendamentosPorEmpresa(LocalDate inicio, LocalDate fim,
            Long codEmpresa, Long codFuncionario) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<AgendamentoEstatisticaEmpresa> criteria = builder.createQuery(
                AgendamentoEstatisticaEmpresa.class);
        Root<Agendamento> root = criteria.from(Agendamento.class);

        criteria.select(builder.construct(
                AgendamentoEstatisticaEmpresa.class,
                root.get(Agendamento_.agenda),
                root.get(Agendamento_.funcionario),
                root.get(Agendamento_.empresa),
                root.get(Agendamento_.motivo),
                root.get(Agendamento_.horaExame)));

        criteria.where(
                builder.greaterThanOrEqualTo(root.get(Agendamento_.agenda).get(Agenda_.diaAgenda), inicio),
                builder.lessThanOrEqualTo(root.get(Agendamento_.agenda).get(Agenda_.diaAgenda), fim)
        );

        if (codEmpresa.compareTo(0l) > 0) {
            criteria.where(
                    builder.equal(root.get(Agendamento_.empresa).get(Empresa_.codigo), codEmpresa)
            );
        }

        if (codFuncionario.compareTo(0l) > 0) {
            criteria.where(
                    builder.equal(root.get(Agendamento_.funcionario).get(Funcionario_.codigo), codFuncionario)
            );
        }

        TypedQuery<AgendamentoEstatisticaEmpresa> tpQuery = manager.createQuery(criteria);


        return tpQuery.getResultList();
    }

    @Override
    public Page<Agendamento> filtrar(AgendamentoFilter agendamentoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Agendamento> criteria = builder.createQuery(Agendamento.class);
        Root<Agendamento> root = criteria.from(Agendamento.class);

        Predicate[] predicates = criarRestricoes(agendamentoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Agendamento> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        final Usuario usuario = UsuarioSessao.getUserLogado();

        final boolean isAdmin = usuario.getPermissoes().stream().anyMatch(permissao -> permissao.getDescricao().equals(
                "ROLE_ADMIN"));

        //TODO alterar total e pageable
        if (!isAdmin) {

            List<Agendamento> resultList = new ArrayList<>();
            query.getResultList().forEach(agendamento ->
                    agendamento.getFuncionario().getEmpresas().forEach(empresa -> {
                        if (empresa.getCodigo().compareTo(usuario.getEmpresa().getCodigo()) == 0) {
                            resultList.add(agendamento);
                        }
                    })
            );


            return new PageImpl<>(resultList, pageable, total(agendamentoFilter));
        }

        return new PageImpl<>(query.getResultList(), pageable, total(agendamentoFilter));
    }


    @Override
    public Page<ResumoAgendamento> resumir(AgendamentoFilter agendamentoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<ResumoAgendamento> criteria = builder.createQuery(ResumoAgendamento.class);
        Root<Agendamento> root = criteria.from(Agendamento.class);

        criteria.select(builder.construct(ResumoAgendamento.class
                , root.get(Agendamento_.codigo)
                , root.get(Agendamento_.observacao)
                , root.get(Agendamento_.laudoGerado)
        ));

        Predicate[] predicates = criarRestricoes(agendamentoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<ResumoAgendamento> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(agendamentoFilter));
    }

    private Predicate[] criarRestricoes(AgendamentoFilter agendamentoFilter, CriteriaBuilder builder,
            Root<Agendamento> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(agendamentoFilter.getObservacao())) {
            predicates.add(builder.like(
                    builder.lower(root.get(Agendamento_.observacao)),
                    "%" + agendamentoFilter.getObservacao().toLowerCase() + "%"));
        }

        if (agendamentoFilter.getLaudoGerado() != null) {
            if (agendamentoFilter.getLaudoGerado()) {
                predicates.add(builder.isTrue(root.get(Agendamento_.laudoGerado)));

            } else {
                predicates.add(builder.isFalse(root.get(Agendamento_.laudoGerado)));
            }
        }


        if (null != agendamentoFilter.getDataExameDe() && null != agendamentoFilter.getDataExameAte()) {

            predicates.add(builder.greaterThanOrEqualTo(root.get(Agendamento_.agenda).get(Agenda_.diaAgenda),
                    agendamentoFilter.getDataExameDe()));

            predicates.add(builder.lessThanOrEqualTo(root.get(Agendamento_.agenda).get(Agenda_.diaAgenda),
                    agendamentoFilter.getDataExameAte()));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private Long total(AgendamentoFilter agendamentoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Agendamento> root = criteria.from(Agendamento.class);

        Predicate[] predicates = criarRestricoes(agendamentoFilter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

}

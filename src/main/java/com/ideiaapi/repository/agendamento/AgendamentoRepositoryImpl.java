package com.ideiaapi.repository.agendamento;

import com.ideiaapi.dto.AgendamentoEstatisticaEmpresa;
import com.ideiaapi.model.*;
import com.ideiaapi.repository.filter.AgendamentoFilter;
import com.ideiaapi.repository.projection.ResumoAgendamento;
import com.ideiaapi.repository.restricoes.paginacao.RestricoesPaginacao;
import com.ideiaapi.security.UsuarioSessao;
import com.ideiaapi.util.datas.UtilsData;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class AgendamentoRepositoryImpl extends RestricoesPaginacao implements AgendamentoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<AgendamentoEstatisticaEmpresa> agendamentosPorEmpresa(LocalDate inicio, LocalDate fim, Long codEmpresa, Long codFuncionario) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<AgendamentoEstatisticaEmpresa> criteria = builder.createQuery(AgendamentoEstatisticaEmpresa.class);

        Root<Agendamento> root = criteria.from(Agendamento.class);

        //TODO verificar pq não esta ordenando
        criteria.orderBy(builder.asc(root.get(Agendamento_.agenda).get(Agenda_.diaAgenda)));

        criteria.select(builder.construct(
                AgendamentoEstatisticaEmpresa.class,
                root.get(Agendamento_.agenda),
                root.get(Agendamento_.funcionario),
                root.get(Agendamento_.empresa),
                root.get(Agendamento_.motivo),
                root.get(Agendamento_.horaExame)));

        //TODO verificar pq não esta filtrando
        criteria.where(
                builder.greaterThanOrEqualTo(root.get(Agendamento_.agenda).get(Agenda_.diaAgenda), inicio),
                builder.lessThanOrEqualTo(root.get(Agendamento_.agenda).get(Agenda_.diaAgenda), fim)
        );

        if (codEmpresa.compareTo(0L) > 0) {
            criteria.where(
                    builder.equal(root.get(Agendamento_.empresa).get(Empresa_.codigo), codEmpresa)
            );
        }

        if (codFuncionario.compareTo(0L) > 0) {
            criteria.where(
                    builder.equal(root.get(Agendamento_.funcionario).get(Funcionario_.codigo), codFuncionario)
            );
        }

        TypedQuery<AgendamentoEstatisticaEmpresa> tpQuery = manager.createQuery(criteria);

        List<AgendamentoEstatisticaEmpresa> resultList = tpQuery.getResultList()
                .stream().filter(age -> age.getAgenda().getDiaAgenda().isAfter(inicio))
                .filter(age -> age.getAgenda().getDiaAgenda().isBefore(fim))
                .collect(Collectors.toList());

        if (resultList.isEmpty()) {
            return null;
        }

        if (codEmpresa.compareTo(0l) > 0) {

            resultList = resultList
                    .stream().filter(age -> age.getFuncionario().getEmpresas().stream()
                            .max(Comparator.comparing(Empresa::getCodigo)).map(Empresa::getCodigo).get().compareTo(codEmpresa) == 0)
                    .collect(Collectors.toList());

        }

        this.incrementResultSetAgendamentos(resultList);
        return this.getAgendamentosOrdenados(resultList);
    }

    private void incrementResultSetAgendamentos(List<AgendamentoEstatisticaEmpresa> resultList) {
        resultList.forEach(age -> {
            age.getAgenda().setDataAgendaTemp(UtilsData.getDataConvertida(age.getAgenda().getDiaAgenda(), "dd/MM/yyyy"));

            LocalDate diaAgenda = age.getAgenda().getDiaAgenda();
            LocalTime horaExame = age.getHoraExame();

            try {
                LocalDateTime dtaExame = LocalDateTime.of(diaAgenda.getYear(), diaAgenda.getMonthValue(), diaAgenda.getDayOfMonth(), horaExame.getHour(),
                        horaExame.getMinute(), horaExame.getSecond());
                age.setDtaTemp(dtaExame);
            } catch (Exception e) {
                e.printStackTrace();
                age.setDtaTemp(LocalDateTime.now());
            }

            age.setNomeEmpresaTemp(age.getEmpresa().getNome());
        });
    }

    private List<AgendamentoEstatisticaEmpresa> getAgendamentosOrdenados(List<AgendamentoEstatisticaEmpresa> resultList) {
        Set<AgendamentoEstatisticaEmpresa> result = new HashSet<>(resultList);
        List<AgendamentoEstatisticaEmpresa> agendamentos = new ArrayList<>(result);
        agendamentos.sort(Comparator.comparing(AgendamentoEstatisticaEmpresa::getDtaTemp));
//        agendamentos.sort(Comparator.comparing(AgendamentoEstatisticaEmpresa::getNomeEmpresaTemp));
        return agendamentos;
    }

    @Override
    public Page<Agendamento> filtrar(AgendamentoFilter agendamentoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Agendamento> criteria = builder.createQuery(Agendamento.class);
        Root<Agendamento> root = criteria.from(Agendamento.class);
        criteria.orderBy(builder.desc(root.get(Agendamento_.agenda).get(Agenda_.diaAgenda)));

        Predicate[] predicates = criarRestricoes(agendamentoFilter, builder, root);
        criteria.where(predicates);

        criteria.orderBy(builder.desc(root.get("codigo")));
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
                , root.get(Agendamento_.funcionario).get(Funcionario_.codigo)
                , root.get(Agendamento_.empresa).get(Empresa_.codigo)
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
        if (null != agendamentoFilter.getCodFuncionario()) {

            predicates.add(builder.greaterThanOrEqualTo(root.get(Agendamento_.funcionario).get(Funcionario_.codigo),
                    agendamentoFilter.getCodFuncionario()));

            predicates.add(builder.lessThanOrEqualTo(root.get(Agendamento_.funcionario).get(Funcionario_.codigo),
                    agendamentoFilter.getCodFuncionario()));

        }
        if (null != agendamentoFilter.getCodEmpresa()) {

            predicates.add(builder.greaterThanOrEqualTo(root.get(Agendamento_.empresa).get(Empresa_.codigo),
                    agendamentoFilter.getCodEmpresa()));

            predicates.add(builder.lessThanOrEqualTo(root.get(Agendamento_.empresa).get(Empresa_.codigo),
                    agendamentoFilter.getCodEmpresa()));

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

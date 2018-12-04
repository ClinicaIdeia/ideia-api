package com.ideaapi.repository.funcionario;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.ideaapi.model.Empresa_;
import com.ideaapi.model.Funcionario;
import com.ideaapi.model.Funcionario_;
import com.ideaapi.repository.filter.FuncionarioFilter;
import com.ideaapi.repository.projection.ResumoFuncionario;
import com.ideaapi.repository.restricoes.paginacao.RestricoesPaginacao;

public class FuncionarioRepositoryImpl extends RestricoesPaginacao implements FuncionarioRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Funcionario> filtrar(FuncionarioFilter funcionarioFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Funcionario> criteria = builder.createQuery(Funcionario.class);
        Root<Funcionario> root = criteria.from(Funcionario.class);

        Predicate[] predicates = criarRestricoes(funcionarioFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Funcionario> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(funcionarioFilter));
    }


    @Override
    public Page<ResumoFuncionario> resumir(FuncionarioFilter funcionarioFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<ResumoFuncionario> criteria = builder.createQuery(ResumoFuncionario.class);
        Root<Funcionario> root = criteria.from(Funcionario.class);

        criteria.select(builder.construct(ResumoFuncionario.class
                , root.get(Funcionario_.codigo)
                , root.get(Funcionario_.nome)
                , root.get(Funcionario_.matricula)
                , root.get(Funcionario_.rg)
                , root.get(Funcionario_.cpf)
                , root.get(Funcionario_.telefone)
                , root.get(Funcionario_.cargo)
//                , root.get(Funcionario_.empresa).get(Empresa_.nome)
        ));

        Predicate[] predicates = criarRestricoes(funcionarioFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<ResumoFuncionario> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(funcionarioFilter));
    }

    private Predicate[] criarRestricoes(FuncionarioFilter funcionarioFilter, CriteriaBuilder builder,
            Root<Funcionario> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(funcionarioFilter.getNome())) {
            predicates.add(builder.like(
                    builder.lower(root.get(Funcionario_.nome)),
                    "%" + funcionarioFilter.getNome().toLowerCase() + "%"));
        }

        if (funcionarioFilter.getTelefone() != null) {
            predicates.add(builder.like(
                    builder.lower(root.get(Funcionario_.telefone)),
                    "%" + funcionarioFilter.getTelefone().toLowerCase() + "%"));
        }

        if (funcionarioFilter.getCpf() != null) {
            predicates.add(builder.like(
                    builder.lower(root.get(Funcionario_.cpf)),
                    "%" + funcionarioFilter.getCpf().toLowerCase() + "%"));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private Long total(FuncionarioFilter funcionarioFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Funcionario> root = criteria.from(Funcionario.class);

        Predicate[] predicates = criarRestricoes(funcionarioFilter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

}

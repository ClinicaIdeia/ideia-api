package com.ideiaapi.repository.funcionario;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import com.ideiaapi.model.Empresa;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.model.Funcionario_;
import com.ideiaapi.model.Usuario;
import com.ideiaapi.repository.filter.FuncionarioFilter;
import com.ideiaapi.repository.projection.ResumoFuncionario;
import com.ideiaapi.repository.restricoes.paginacao.RestricoesPaginacao;
import com.ideiaapi.security.UsuarioSessao;

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

        final Usuario usuario = UsuarioSessao.getUserLogado();

        final boolean isAdmin = UsuarioSessao.isAdmin(usuario);

        //TODO alterar total e pageable
        if (!isAdmin) {
            List<Funcionario> collect = new ArrayList<>();

            //TODO fazer conta do total de itens e do pageable
            query.getResultList().forEach(func -> {
                func.getEmpresas().forEach(empresa -> {
                    if (empresa.getCodigo().compareTo(usuario.getEmpresa().getCodigo()) == 0) {
                        collect.add(func);
                        return;
                    }
                });
            });

            return new PageImpl<>(collect, pageable, total(funcionarioFilter));
        }

        PageImpl<Funcionario> funcionarios = null;
        try {

            funcionarios = new PageImpl<>(query.getResultList(), pageable, total(funcionarioFilter));
        } catch (Exception e) {
            return null;
        }

        return funcionarios;
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
            predicates.add(builder.like(builder.lower(root.get(Funcionario_.telefone)),
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

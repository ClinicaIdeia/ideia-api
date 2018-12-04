package com.ideaapi.repository.usario;

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

import com.ideaapi.model.Usuario;
import com.ideaapi.model.Usuario_;
import com.ideaapi.repository.filter.UsuarioFilter;
import com.ideaapi.repository.restricoes.paginacao.RestricoesPaginacao;

public class UsuarioRepositoryImpl extends RestricoesPaginacao implements UsuarioRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Usuario> filtrar(UsuarioFilter usuarioFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
        Root<Usuario> root = criteria.from(Usuario.class);

        Predicate[] predicates = criarRestricoes(usuarioFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Usuario> query = manager.createQuery(criteria);
        super.adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(usuarioFilter));
    }


    private Predicate[] criarRestricoes(UsuarioFilter usuarioFilter, CriteriaBuilder builder,
            Root<Usuario> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(usuarioFilter.getNome())) {
            predicates.add(builder.like(
                    builder.lower(root.get(Usuario_.nome)),
                    "%" + usuarioFilter.getNome().toLowerCase() + "%"));
        }

        if (usuarioFilter.getEmail() != null) {
            predicates.add(builder.like(
                    builder.lower(root.get(Usuario_.email)),
                    "%" + usuarioFilter.getEmail().toLowerCase() + "%"));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private Long total(UsuarioFilter usuarioFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Usuario> root = criteria.from(Usuario.class);

        Predicate[] predicates = criarRestricoes(usuarioFilter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

}

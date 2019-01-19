package com.ideiaapi.repository.laudo;

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

import com.ideiaapi.model.Laudo;
import com.ideiaapi.model.Laudo_;
import com.ideiaapi.repository.filter.LaudoFilter;
import com.ideiaapi.repository.projection.ResumoLaudo;
import com.ideiaapi.repository.restricoes.paginacao.RestricoesPaginacao;

public class LaudoRepositoryImpl extends RestricoesPaginacao implements LaudoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Laudo> filtrar(LaudoFilter laudoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Laudo> criteria = builder.createQuery(Laudo.class);
        Root<Laudo> root = criteria.from(Laudo.class);

        Predicate[] predicates = criarRestricoes(laudoFilter, builder, root);
        criteria.where(predicates);

        StringBuilder sb = new StringBuilder();
        sb.append("from Laudo l where 1=1");

        if (null != laudoFilter.getObservacao()) {
            sb.append(" and lower(l.observacao) like lower('%");
            sb.append(laudoFilter.getObservacao());
            sb.append("%')");
        }

        if (null != laudoFilter.getDataLaudoDe() && null != laudoFilter.getDataLaudoAte()) {
            sb.append(" and l.dataEmissao between '").append(laudoFilter.getDataLaudoDe());
            sb.append("' and '").append(laudoFilter.getDataLaudoAte()).append("'");
        }


        TypedQuery<Laudo> query =
                manager.createQuery(sb.toString(),
                        Laudo.class);

        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(laudoFilter));
    }


    @Override
    public Page<ResumoLaudo> resumir(LaudoFilter laudoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<ResumoLaudo> criteria = builder.createQuery(ResumoLaudo.class);
        Root<Laudo> root = criteria.from(Laudo.class);

        criteria.select(builder.construct(ResumoLaudo.class
                , root.get(Laudo_.codigo)
        ));

        Predicate[] predicates = criarRestricoes(laudoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<ResumoLaudo> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(laudoFilter));
    }

    private Predicate[] criarRestricoes(LaudoFilter laudoFilter, CriteriaBuilder builder,
            Root<Laudo> root) {
        List<Predicate> predicates = new ArrayList<>();

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

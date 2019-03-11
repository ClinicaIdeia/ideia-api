package com.ideiaapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ideiaapi.model.Permissao;


public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

    @Query(value = "select * from permissao\n" +
            "where codigo not in (\n" +
            "  select p.codigo\n" +
            "  from usuario usu\n" +
            "         inner join usuario_permissao usup on usup.codigo_usuario = usu.codigo\n" +
            "         inner join permissao p on usup.codigo_permissao = p.codigo\n" +
            "\n" +
            "  where usu.codigo = :codigo\n" +
            ")", nativeQuery = true)
    List<Permissao> buscaPermissoesUsuario(@Param(value = "codigo") Long codigo);

}

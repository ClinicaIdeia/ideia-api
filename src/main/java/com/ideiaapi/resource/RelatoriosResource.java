package com.ideiaapi.resource;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ideiaapi.service.AgendamentoService;
import com.ideiaapi.service.LaudoService;

@RestController
@RequestMapping("/relatorios")
public class RelatoriosResource {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private LaudoService laudoService;

    @GetMapping("/agendamentos/{codEmpresa}/{codFuncionario}")
    @PreAuthorize(value = "hasAuthority('ROLE_RELATORIO_AGENDAMENTO') or hasAuthority('ROLE_ADMIN')  and #oauth2"
            + ".hasScope('read')")

    public ResponseEntity<byte[]> relatorioPorEmpresa(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim,
            @PathVariable Long codEmpresa,
            @PathVariable Long codFuncionario
            ) throws Exception {

        byte[] bytes = this.agendamentoService.relatorioPorEmpresa(inicio, fim, codEmpresa, codFuncionario);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE).body(bytes);
    }

    @GetMapping("/laudo/{codigo}")
    @PreAuthorize(value = "hasAuthority('ROLE_LAUDO_INFO') or hasAuthority('ROLE_ADMIN')  and #oauth2"
            + ".hasScope('read')")
    public ResponseEntity<byte[]> laudoFunc(@PathVariable
            ("codigo") Long codigo) throws Exception {

        byte[] bytes = this.laudoService.imprimeLaudo(codigo);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE).body(bytes);
    }

}

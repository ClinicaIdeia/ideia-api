package com.ideiaapi.service;

import static com.ideiaapi.constants.ErrorsCode.LAUDO_NAO_ENCONTRADO;

import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ideiaapi.dto.AptidaoDTO;
import com.ideiaapi.exceptions.BusinessException;
import com.ideiaapi.model.Agendamento;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.model.Laudo;
import com.ideiaapi.repository.LaudoRepository;
import com.ideiaapi.repository.filter.LaudoFilter;
import com.ideiaapi.validate.LaudoValidate;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LaudoService {

    @Autowired
    private LaudoRepository laudoRepository;

    @Autowired
    private AptidaoService aptidaoService;

    @Autowired
    private LaudoValidate laudoValidate;

    @Autowired
    private AgendamentoService agendamentoService;

    public byte[] laudoPorFuncionario(Long codigo) throws Exception {

        Laudo laudo = this.buscaLaudo(codigo);

        if (null == laudo) {
            throw new BusinessException(LAUDO_NAO_ENCONTRADO);
        }

        Map<String, Object> parametros = new HashMap<>();

        final Funcionario funcionario = laudo.getFuncionario();

        parametros.put("FUNC_NOME", funcionario.getNome());
        parametros.put("FUNC_SEXO", funcionario.getSexo());
        parametros.put("FUNC_ESTADO_CIVIL", funcionario.getEstadoCivil());
        parametros.put("FUNC_IDADE", String.valueOf(funcionario.getIdade()));
        parametros.put("FUNC_ESCOLARIDADE", funcionario.getEscolaridade());
        parametros.put("FUNC_CPF", funcionario.getCpf());
        parametros.put("FUNC_PROFISSAO", funcionario.getCargo());
        parametros.put("DT_AVALIACAO", Date.valueOf(laudo.getDataExame()));

        InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/laudo.jasper");

        List<AptidaoDTO> aptidoes = new ArrayList<>();

        laudo.getAptidoes().forEach(aptidao ->
                aptidoes.add(new AptidaoDTO(aptidao.getApto() ? "( X )" : "(   )", aptidao.getDescricao()))
        );

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
                new JRBeanCollectionDataSource(aptidoes));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] atestadoPorFuncionario(Long codigo) throws Exception {

        Laudo laudo = this.buscaLaudo(codigo);

        if (null == laudo) {
            throw new BusinessException(LAUDO_NAO_ENCONTRADO);
        }

        Map<String, Object> parametros = new HashMap<>();

        final Funcionario funcionario = laudo.getFuncionario();

        parametros.put("FUNC_NOME", funcionario.getNome());
        parametros.put("FUNC_SEXO", funcionario.getDataNascimento());
        parametros.put("FUNC_CPF", funcionario.getCpf());
        parametros.put("FUNC_PROFISSAO", funcionario.getCargo());
        parametros.put("DT_AVALIACAO", Date.valueOf(laudo.getDataExame()));

        InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/atestado.jasper");

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


    public Page<Laudo> filtrar(LaudoFilter filter, Pageable pageable) {
        return this.laudoRepository.filtrar(filter, pageable);
    }

    public Laudo cadastraLaudo(Laudo entity) {
        this.laudoValidate.fluxoCriacao(entity);

        this.aptidaoService.cadastrarAptidoes(entity.getAptidoes());

        final Laudo save = this.laudoRepository.save(entity);
        this.agendamentoService.marcarLaudoGerado(save);
        return save;
    }

    public Laudo buscaLaudo(Long codigo) {
        Laudo laudo = this.laudoRepository.findOne(codigo);
        if (laudo != null) {
            return laudo;
        }

        return null;
    }

    public void deletaLaudo(Long codigo) {
        Laudo laudo = this.buscaLaudo(codigo);
        this.reativaAgendamentoRelacionado(laudo);
        this.laudoRepository.delete(codigo);
    }

    private void reativaAgendamentoRelacionado(Laudo laudo) {
        Long codAgendamento = laudo.getCodAgendamento();
        Agendamento agendamento = agendamentoService.buscaAgendamento(codAgendamento);
        agendamento.setLaudoGerado(false);
        agendamentoService.atualizaAgendamento(codAgendamento, agendamento);
    }

    public ResponseEntity<Laudo> atualizaLaudo(Long codigo, Laudo laudo) {
        Laudo laudoSalva = this.buscaLaudo(codigo);
        BeanUtils.copyProperties(laudo, laudoSalva, "codigo");

        this.laudoRepository.save(laudoSalva);
        return ResponseEntity.ok(laudoSalva);
    }
}

package com.ideaapi.repository.projection;

import java.time.LocalDate;

import com.ideaapi.enums.TipoAgendamento;

public class ResumoAgendamento {

    private Long codigo;
    private String obsrevacao;
    private LocalDate dataExame;
    private String horaExame;
    private TipoAgendamento tipo;
    private String empresa;
    private String funcionario;

    public ResumoAgendamento(Long codigo, String obsrevacao, LocalDate dataExame, String horaExame,
            TipoAgendamento tipo, String empresa, String funcionario) {
        this.codigo = codigo;
        this.obsrevacao = obsrevacao;
        this.dataExame = dataExame;
        this.horaExame = horaExame;
        this.tipo = tipo;
        this.empresa = empresa;
        this.funcionario = funcionario;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getObsrevacao() {
        return obsrevacao;
    }

    public void setObsrevacao(String obsrevacao) {
        this.obsrevacao = obsrevacao;
    }

    public LocalDate getDataExame() {
        return dataExame;
    }

    public void setDataExame(LocalDate dataExame) {
        this.dataExame = dataExame;
    }

    public String getHoraExame() {
        return horaExame;
    }

    public void setHoraExame(String horaExame) {
        this.horaExame = horaExame;
    }

    public TipoAgendamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoAgendamento tipo) {
        this.tipo = tipo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }
}

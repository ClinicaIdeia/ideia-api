package com.ideiaapi.repository.projection;

public class ResumoLaudo {

    private Long codigo;
    private String funcionario;
    private String motivo;

    public ResumoLaudo(Long codigo, String funcionario, String motivo) {
        this.codigo = codigo;
        this.funcionario = funcionario;
        this.motivo = motivo;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}


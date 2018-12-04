package com.ideaapi.repository.projection;

public class ResumoEmpresa {

    private Long codigo;
    private String razaoSocial;
    private String cnpj;
    private String contato;
    private String telefone;
    private String email;
    private Boolean ativa;

    public ResumoEmpresa(Long codigo, String razaoSocial, String cnpj, String contato, String telefone,
            String email, Boolean ativa) {
        this.codigo = codigo;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.contato = contato;
        this.telefone = telefone;
        this.email = email;
        this.ativa = ativa;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}


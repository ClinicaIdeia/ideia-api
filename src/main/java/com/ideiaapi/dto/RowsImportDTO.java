package com.ideiaapi.dto;

import java.util.List;

public class RowsImportDTO {

    private Integer totalImportado;
    private Integer totalFalhas;
    private List<String> falhas;

    public Integer getTotalImportado() {
        return totalImportado;
    }

    public void setTotalImportado(Integer totalImportado) {
        this.totalImportado = totalImportado;
    }

    public Integer getTotalFalhas() {
        return totalFalhas;
    }

    public void setTotalFalhas(Integer totalFalhas) {
        this.totalFalhas = totalFalhas;
    }

    public List<String> getFalhas() {
        return falhas;
    }

    public void setFalhas(List<String> falhas) {
        this.falhas = falhas;
    }
}

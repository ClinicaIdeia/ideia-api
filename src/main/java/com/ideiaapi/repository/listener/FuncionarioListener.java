package com.ideiaapi.repository.listener;

import com.ideiaapi.IdeiaApiApplication;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.storage.S3;
import org.springframework.util.StringUtils;

import javax.persistence.PostLoad;

public class FuncionarioListener {

    @PostLoad
    public void funcionarioPostLoad(Funcionario funcionario) {
        if (StringUtils.hasText(funcionario.getAnexo())) {
            S3 s3 = IdeiaApiApplication.getBEan(S3.class);
            funcionario.setUrlAnexo(s3.configuraUrl(funcionario.getAnexo()));
        }

    }

}

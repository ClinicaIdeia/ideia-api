package com.ideiaapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ideiaapi.dto.s3.AnexoS3DTO;
import com.ideiaapi.storage.S3;

@Service
public class UploadAdminService {

    @Autowired
    private S3 s3;

    public AnexoS3DTO uploadConfigAdmin(MultipartFile file) {
        String nome = s3.salvarArquivoS3Temporatimente(file, Boolean.FALSE);
        return new AnexoS3DTO(nome, s3.configuraUrl(nome));
    }

}

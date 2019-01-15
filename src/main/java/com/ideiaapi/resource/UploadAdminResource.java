package com.ideiaapi.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ideiaapi.dto.s3.AnexoS3DTO;
import com.ideiaapi.service.LaudoService;
import com.ideiaapi.service.UploadAdminService;

@RestController
@RequestMapping("/uploads")
public class UploadAdminResource {

    @Autowired
    public LaudoService laudoService;

    @Autowired
    private UploadAdminService uploadAdminService;

    @PostMapping("/anexo")
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
    public AnexoS3DTO salvarFotoUsuario(@RequestParam MultipartFile anexo) {
        return this.uploadAdminService.uploadConfigAdmin(anexo);
    }

}

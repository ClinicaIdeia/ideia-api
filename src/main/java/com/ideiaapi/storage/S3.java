package com.ideiaapi.storage;

import static com.ideiaapi.constants.ErrorsCode.ERRO_ENVIAR_ARQUIVO_S3;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.SetObjectTaggingRequest;
import com.amazonaws.services.s3.model.Tag;

@Component
public class S3 {

    private static final Logger logger = LoggerFactory.getLogger(S3.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${ideia.s3.bucket-id}")
    private String s3Bucket;

    public String salvarArquivoS3Temporatimente(MultipartFile file) {

        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

        ObjectMetadata obj = new ObjectMetadata();
        obj.setContentType(file.getContentType());
        obj.setContentLength(file.getSize());

        final String nomeUnico = this.geraNomeUnicoArquivo(file.getOriginalFilename());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(s3Bucket, nomeUnico, file.getInputStream(),
                    obj).withAccessControlList(acl);

//            putObjectRequest.setTagging(new ObjectTagging(Arrays.asList(new Tag("expirar", "true"))));
            amazonS3.putObject(putObjectRequest);

            if (logger.isDebugEnabled()) {
                logger.debug("[DEGUGER] Arquivo {} enviado com sucesso para S3.", file.getOriginalFilename());
            } else {
                logger.debug("[APPLICATION] Arquivo {} enviado com sucesso para S3.", file.getOriginalFilename());
            }

            return nomeUnico;
        } catch (IOException e) {
            throw new RuntimeException(ERRO_ENVIAR_ARQUIVO_S3, e);
        }

    }

    public String configuraUrl(String objeto) {
//        return "\\\\" + this.s3Bucket + ".s3.amazonaws.com/" + objeto;
        return "https://" + this.s3Bucket + ".s3.amazonaws.com/" + objeto;
    }

    public void remover(String objeto) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(this.s3Bucket, objeto);
        amazonS3.deleteObject(deleteObjectRequest);
    }

    public void substituir(String objetoAntigo, String objetoNovo) {
        if (StringUtils.hasText(objetoAntigo)) {
            this.remover(objetoAntigo);
        }
        this.salvar(objetoNovo);
    }

    public void salvar(String objeto) {
        SetObjectTaggingRequest setObjectTaggingRequest = new SetObjectTaggingRequest(this.s3Bucket, objeto,
                new ObjectTagging(Collections.emptyList()));
        amazonS3.setObjectTagging(setObjectTaggingRequest);
    }

    private String geraNomeUnicoArquivo(String nomeArquivo) {
        return UUID.randomUUID().toString() + "__" + nomeArquivo;
    }
}

package com.ideaapi.exceptionhandler;


import static com.ideaapi.constansts.ErrorsCode.MENSAGEM_INVALIDA;
import static com.ideaapi.constansts.ErrorsCode.RECURSO_NAO_ENCONTRADO;
import static com.ideaapi.constansts.ErrorsCode.RECURSO_OPERACAO_NAO_PERMITIDA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ideaapi.exceptions.BusinessException;

@ControllerAdvice
public class IdeaApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
            WebRequest request) {

        String mensagemUsuario = this.messageSource.getMessage(RECURSO_NAO_ENCONTRADO, null,
                LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.toString();

        List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<Object> handleBussinessException(BusinessException ex,
            WebRequest request) {

        String mensagemUsuario = this.messageSource.getMessage(ex.getMessage(), null,
                LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.toString();

        List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
            WebRequest request) {

        String mensagemUsuario = this.messageSource.getMessage(RECURSO_OPERACAO_NAO_PERMITIDA, null,
                LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);

        List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {


        String mensagemUsuario = this.messageSource.getMessage(MENSAGEM_INVALIDA, null,
                LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.getCause() != null ? ex.getCause().toString() : ex.toString();

        List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

        return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<Erro> erros = this.criaListaDeErrosApi(ex.getBindingResult());
        return super.handleExceptionInternal(ex, erros, headers, status, request);
    }

    private List<Erro> criaListaDeErrosApi(BindingResult bindingResult) {
        List<Erro> erros = new ArrayList<>();

        bindingResult.getFieldErrors().forEach(erro -> {

            String mensagemUsuario = messageSource.getMessage(erro, LocaleContextHolder.getLocale());
            String mensagemDesenvolvedor = erro.toString();

            erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));

        });

        return erros;
    }

    public static class Erro {
        private String mensagemUsuario;
        private String mensagemDesenvolvedor;

        public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
            this.mensagemUsuario = mensagemUsuario;
            this.mensagemDesenvolvedor = mensagemDesenvolvedor;
        }

        public String getMensagemUsuario() {
            return mensagemUsuario;
        }

        public String getMensagemDesenvolvedor() {
            return mensagemDesenvolvedor;
        }
    }
}

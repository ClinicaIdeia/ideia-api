package com.ideiaapi.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ideiaapi.base.BaseTest;

public class DocumentValidatorTest extends BaseTest {

    @Test
    public void cleanCPF() {
        String cpf = "123.456.789-00";
        assertTrue("12345678900".equals(DocumentValidator.cleanCPF(cpf)));
    }

    @Test
    public void isCPFValidTrue() {
        String cpf = "65171599246";
        assertTrue(DocumentValidator.isCPFValid(cpf));
    }

    @Test
    public void isCPFValidFalse() {
        String cpf = "00000000000";
        assertFalse(DocumentValidator.isCPFValid(cpf));
    }
}
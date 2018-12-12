package com.ideaapi.util;

import static com.ideaapi.constansts.ErrorsCode.CPF_INVALIDO;

import java.util.InputMismatchException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ideaapi.exceptions.BusinessException;

public class DocumentValidator {

    private static final Logger log = LoggerFactory.getLogger(DocumentValidator.class);

    public static String cleanCPF(String cpf) {
        if (StringUtils.isBlank(cpf))
            throw new BusinessException(CPF_INVALIDO);

        return cpf.trim().replace(" ", "").replace(",", "")
                .replace(".", "").replace("-", "");
    }

    public static boolean isCPFValid(final String CPF) {
        if ((CPF.length() != 11) ||
                "00000000000".equals(CPF) || "11111111111".equals(CPF) ||
                "22222222222".equals(CPF) || "33333333333".equals(CPF) ||
                "44444444444".equals(CPF) || "55555555555".equals(CPF) ||
                "66666666666".equals(CPF) || "77777777777".equals(CPF) ||
                "88888888888".equals(CPF) || "99999999999".equals(CPF))
            return (false);

        char dig10;
        char dig11;
        int sm;
        int i;
        int r;
        int num;
        int peso;

        try {
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = CPF.charAt(i) - 48;
                sm = sm + (num * peso);
                peso = peso - 1;
            }
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else
                dig10 = (char) (r + 48);
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = CPF.charAt(i) - 48;
                sm = sm + (num * peso);
                peso = peso - 1;
            }
            r = 11 - (sm % 11);
            dig11 = ((r == 10) || (r == 11)) ? '0' : (char) (r + 48);
            return (dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10));

        } catch (InputMismatchException erro) {
            log.error("Error validating CPF", erro);
            return (false);
        }
    }
}

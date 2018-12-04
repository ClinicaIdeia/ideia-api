package com.ideaapi.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.stereotype.Service;

import com.ideaapi.representation.EnderecoCEP;
import com.ideaapi.representation.EnderecoRepresentation;
import com.ideaapi.util.JsonUtil;

@Service
public class EnderecoCepService {

    public EnderecoRepresentation getEnderecoCep(String cep) {

        cep = cep.replace(".", "").replace("-", "");

        String json;

        try {
            URL url = new URL("http://viacep.com.br/ws/" + cep + "/json");
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder jsonSb = new StringBuilder();

            br.lines().forEach(l -> jsonSb.append(l.trim()));

            json = jsonSb.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return this.getEndereco(JsonUtil.toObject(json, EnderecoCEP.class));
    }

    private EnderecoRepresentation getEndereco(EnderecoCEP enderecoCEP) {

        EnderecoRepresentation representation = new EnderecoRepresentation();
        representation.setBairro(enderecoCEP.getBairro());
        representation.setCep(enderecoCEP.getCep());
        representation.setCidade(enderecoCEP.getLocalidade());
        representation.setComplemento(enderecoCEP.getComplemento());
        representation.setEstado(enderecoCEP.getUf());
        representation.setLogradouro(enderecoCEP.getLogradouro());

        return representation;
    }
}

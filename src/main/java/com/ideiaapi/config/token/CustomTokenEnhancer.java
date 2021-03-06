package com.ideiaapi.config.token;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.ideiaapi.security.UsuarioSistema;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        UsuarioSistema usuarioSistema = (UsuarioSistema) authentication.getPrincipal();

        usuarioSistema.getUsuario().getEmpresa();

        Map<String, Object> addInfo = new HashMap<>();
        addInfo.put("nome", usuarioSistema.getUsuario().getNome());
        addInfo.put("cod_usuario", usuarioSistema.getUsuario().getCodigo());
        addInfo.put("empresa", usuarioSistema.getUsuario().getEmpresa().getNome());
        addInfo.put("cod_empresa", usuarioSistema.getUsuario().getEmpresa().getCodigo());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo);
        return accessToken;
    }

}

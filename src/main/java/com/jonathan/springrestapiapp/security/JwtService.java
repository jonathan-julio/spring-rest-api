package com.jonathan.springrestapiapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import com.jonathan.springrestapiapp.model.Usuario;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${security.jwt.expiracao}") //injetando propriedades do applications.properties
    private String expiracao;

    @Value("${security.jwt.chave-assinatura}")
    private String chaveAssinatura;

   
    

    public String gerarToken( Usuario usuario ){

        Date data = converter(expiracao); //a forma de passar para o jwt a data de expiração 
        //chave de assinatura 
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(chaveAssinatura));

        return Jwts //retorna o jwt
                .builder()
                .subject(usuario.getLogin()) //colocar informação útil para o token
                .expiration(data) //data de expieração
                .signWith(key) //assinatura do token (recebe parametro key)
                .compact();
    }
    //https://jwt.io

    

     private Claims obterClaims( String token ) throws ExpiredJwtException { //lança erro caso o token tenha sido expirado

        //chave de assinatura 
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(chaveAssinatura));

        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
     
    public boolean tokenValido( String token ){
        try{
            Claims claims = (Claims) obterClaims(token);
            Date dataExpiracao = claims.getExpiration();
            LocalDateTime data =
                    dataExpiracao.toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDateTime();
            return !LocalDateTime.now().isAfter(data); //data atual nao é depois da data de expiração
        }catch (Exception e){ //se ocorrer uma exceção o token não é mais válido
            return false;
        }
    }
    

    public String obterLoginUsuario(String token) throws ExpiredJwtException{
        return (String) (obterClaims(token)).getSubject();
    }

    public Date converter(String expiration){

        long expString = Long.valueOf(expiration);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString);
        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant(); //localdatetime to objeto instante
        Date data = Date.from(instant); //a forma de passar para o jwt a data de expiração 
        return data;
    }
}

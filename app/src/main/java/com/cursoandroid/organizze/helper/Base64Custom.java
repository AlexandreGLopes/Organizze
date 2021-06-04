package com.cursoandroid.organizze.helper;

import android.util.Base64;

// Base 64 é a classe que vai nos ajudar a criptografar e descriptografar. Precisamos fazer isso para adicionar o e-mail como identificador do usuário. Pois o Firebase não aceita um e-mail padrão par isso
public class Base64Custom {

    // estático para não ter que instanciar para usar
    public static String codificarBase64(String texto) {
        //replace all ali está tirando caracteres inválidos como espaços vazios no começo e no final (quebra de linha ou retorno de carro)
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }

    public static String decodificarBase64(String textoCodificado) {
        //método decode não retorna uma String, por isso vamos instaciar uma String que recebe o método no seu parâmetro
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}

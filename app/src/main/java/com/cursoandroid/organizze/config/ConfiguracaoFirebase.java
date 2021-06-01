package com.cursoandroid.organizze.config;

import com.google.firebase.auth.FirebaseAuth;

public class ConfiguracaoFirebase {

    //Definindo como estático para que o atributo seja sempre o mesmo, independente da instância
    private static FirebaseAuth autenticacao;

    //retorna a instancia do Firebase. Definido o método como estático podemos chamá-lo sem instanciar objeto
    public static FirebaseAuth getFirebaseAutenticacao() {
        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

}

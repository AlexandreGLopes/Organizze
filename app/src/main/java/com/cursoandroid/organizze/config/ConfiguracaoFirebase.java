package com.cursoandroid.organizze.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    //Definindo como estático para que o atributo seja sempre o mesmo, independente da instância
    private static FirebaseAuth autenticacao;
    private static DatabaseReference firebase;

    //retorna a instancia do FirebaseDATABASE que permite salvar no banco de dados. Definido o método como estático podemos chamá-lo sem instanciar objeto
    public static DatabaseReference getFirebaseDatabase() {
        if (firebase == null) {
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }

    //retorna a instancia do FirebaseAUTH. Definido o método como estático podemos chamá-lo sem instanciar objeto
    public static FirebaseAuth getFirebaseAutenticacao() {
        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

}

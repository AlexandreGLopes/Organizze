package com.cursoandroid.organizze.model;

import android.util.Log;

import com.cursoandroid.organizze.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {

    private String idUsuario;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
    }

    //Método para salvar no Firebase real Time Database
    public void salvarUsuario() {
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        //Criando o nó "usuários", criando o nó com o IdUsuário e passando a ele o setValue(this). Isso representa que estaremos salvando um objeto Usuario inteiro com todos os seus parametros configurados. Por isso usamos o @Exclude em cima dos métodos getSenha e getIdUsuario para estes não serem salvos juntos no processo. Só precisamos do IdUsuario como nó e do nome e email como valores.
        firebase.child("usuarios")
                .child(this.idUsuario)
                .setValue(this);

        Log.i("Alexandre", this.email+ this.idUsuario + this.nome + this.senha);

    }

    //@Exclude é uma anotação do firebaseDatabase. @Exclude em cima dos métodos getSenha e getIdUsuario para estes não serem salvos juntos no processo. Só precisamos do IdUsuario como nó e do nome e email como valores.
    @Exclude
    public String getIdUsuario() { return idUsuario; }

    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

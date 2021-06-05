package com.cursoandroid.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cursoandroid.organizze.R;
import com.cursoandroid.organizze.config.ConfiguracaoFirebase;
import com.cursoandroid.organizze.helper.Base64Custom;
import com.cursoandroid.organizze.helper.DateCustom;
import com.cursoandroid.organizze.model.Movimentacao;
import com.cursoandroid.organizze.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double despesaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);

        //Preenche o campoData com a data atual
        campoData.setText(DateCustom.dataAtual());
        //Queremos que o valor total da despesa seja recuperado antes do usuário digitar uma nova despesa nesta activity
        recuperarDespesaTotal();
    }

    public void salvarDespesa(View view) {
        //Validando os campos
        if (validarCamposDespesa()) {
            //Configiurando a despesa ser salva
            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperaodo = Double.parseDouble(campoValor.getText().toString());
            movimentacao.setValor(valorRecuperaodo);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("d");

            //No momento em que salvarmos a depesa é que precisamos atualizar os valores da movimentação total
            Double despesaAtualizada = despesaTotal + valorRecuperaodo;
            atualizarDespesa(despesaAtualizada);

            movimentacao.salvar(data);
            finish();
        }
    }

    public Boolean validarCamposDespesa() {

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textocategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if (!textoValor.isEmpty()){
            if (!textoData.isEmpty()){
                if (!textocategoria.isEmpty()){
                    if (!textoDescricao.isEmpty()){
                        return true;
                    }
                    else {
                        Toast.makeText(DespesasActivity.this,
                                "Descrição não foi preenchida",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

                }
                else {
                    Toast.makeText(DespesasActivity.this,
                            "Categoria não foi preenchida",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
            else {
                Toast.makeText(DespesasActivity.this,
                        "Data não foi preenchida",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else {
            Toast.makeText(DespesasActivity.this,
                    "Valor não foi preenchido",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void recuperarDespesaTotal() {

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void atualizarDespesa(Double despesa) {

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesa);
    }

}
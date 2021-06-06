package com.cursoandroid.organizze.activity;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.cursoandroid.organizze.config.ConfiguracaoFirebase;
import com.cursoandroid.organizze.helper.Base64Custom;
import com.cursoandroid.organizze.model.Movimentacao;
import com.cursoandroid.organizze.model.Usuario;
import com.cursoandroid.organizze.adapter.AdapterMovimentacoes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//RETIRADO: import com.cursoandroid.organizze.activity.databinding.ActivityPrincipalBinding;

import com.cursoandroid.organizze.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/*

    MUITO IMPORTANTE:
    TODOS OS CÓDIGOS COMENTADOS AQUI E MARCADOS COMO "RETIRADO" OCORRERAM PORQUE HOUVE UM CONFLITO QUANDO MONTEI A NOVA BASIC ACTIVITY COM FLOATING ACTION BUTTON
    A NOVA VERSÃO DO ANDROID CRIA ELA COM MÉTODOS DIFERENTES E JÁ COLOCA TAMBÉM NOVOS FRAGMENTS E CLASSES PARA CHAMÁ-LOS
    DELETEI OS ARQUIVOS E COLOQUEI TUDO NO PADRÃO ANTIGO E ESPERO QUE ISSO FUNCIONE DIREITO.

    NA INTERFACE, NO ARQUIVO CONTENT PRINCIPAL, DELETEI O ITEM QUE ESTAVA COM ERRO ABAIXO DO "CONSTRAINT LAYOUT". ERA UM nav_host_fragment_content_principal
    EM CÓDIGO XML A PARTE RETIRADA COM ESSA DELEÇÃO FOI:
    <fragment
        android:id="@+id/nav_host_fragment_content_principal"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:defaultNavHost="true"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:navGraph="@navigation/nav_graph" />

        DENTRO DE ACTIVITY_PRINCIPAL COMENTEI O CÓDIGO:
     <!--<com.google.android.material.appbar.AppBarLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:theme="@style/Theme.Organizze.AppBarOverlay">

        <androidx.appcompat.widget.Toolbar
            android:id="@+id/toolbar"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:background="?attr/colorPrimary"
            app:popupTheme="@style/Theme.Organizze.PopupOverlay" />

    </com.google.android.material.appbar.AppBarLayout>-->
    ESTE CÓDIGO COMENTADO RETIROU A TOOLBAR.
    TALVEZ SEJA OPCIONAL, POIS DEPOIS DESCOOMENTEI E TAMBÉM DESCOMENTEI A PARTE DA TOOLBAR AQUI ABAIXO E FUNCIONOU DO MESMO JEITO.

 */
public class PrincipalActivity extends AppCompatActivity {

    //RETIRADO: private AppBarConfiguration appBarConfiguration;
    //RETIRADO: private ActivityPrincipalBinding binding;

    //Atributos para mostrar informações gerais do usuario e da pagina principal
    private MaterialCalendarView calendarView;
    private TextView textoSaudacao, textoSaldo;
    private Double despesaTotal = 0.00;
    private Double receitaTotal = 0.00;
    private Double resumoUsuario = 0.00;

    //Atributos para utilizar o Firebase
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    //Vamos criar um segundo database reference para que ele seja instanciado como um objeto. Aí poderemos anexar um evento a ele, desanexar um evento e chamá-lo de diversos locais do código. Assim, quando precisar chamar o evento de Listener do Firebase para atualizar o resumo poderemos retira-lo em dado momento, quando sairmos do app por exemplo, para não ficar sendo notificados do firebase
    private DatabaseReference usuarioRef;
    //Objeto que vai poder tratar e receber um event event listener
    private ValueEventListener valueEventListenerUsuario;

    //atributos do RecyclerView
    private RecyclerView recyclerMovimentos;
    private AdapterMovimentacoes adapterMovimentacoes;
    private List<Movimentacao> listaMovimentacoes = new ArrayList<>();

    //atributos de movimentações
    private DatabaseReference movimentacaoRef;
    private String mesAnoSelecionado;
    private ValueEventListener valueEventListenerMovimentacoes;
    //Atributo para pegar uma unica movimentação ( a principio usado na hora excluir com swipe)
    private Movimentacao movimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //RETIRADO: binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        //RETIRADO: setContentView(binding.getRoot());
        setContentView(R.layout.activity_principal);

        //RETIRADO: setSupportActionBar(binding.toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);

        //RETIRADO: NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
        //RETIRADO: appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        //RETIRADO: NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        /*
        //Não vamos adicionar o click listener abaixo neste projeto. Vamos adicionar um evento onClick no xml dos FABs
        FloatingActionButton fab = findViewById(R.id.fab);
        //MODIFICADO: Abaixo era: binding.fab.setOnClickListener(new View.OnClickListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

         */

        textoSaldo = findViewById(R.id.textSaldo);
        textoSaudacao = findViewById(R.id.textSaudacao);
        calendarView = findViewById(R.id.calendarView);
        configuraCalendarView();
        //inicia o swipe
        //swipe();

        recyclerMovimentos = findViewById(R.id.recyclerMovimentos);

        //Condigurar adapter
        adapterMovimentacoes = new AdapterMovimentacoes(listaMovimentacoes, this);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMovimentos.setLayoutManager(layoutManager);
        recyclerMovimentos.setHasFixedSize(true);
        recyclerMovimentos.setAdapter(adapterMovimentacoes);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerMovimentos);
    }

    //removendo event listener do objeto usuarioRef
    @Override
    protected void onStop() {
        super.onStop();
        //Log.i("evento", "Evento foi removido");
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentacoes();
    }

    //método para adicionar o swipe (arrastar para os lados) no recyclerview
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //Log.i("swipe", "Item foi arrastado");
            excluirMovimentacao(viewHolder);
        }
    };

    //Método chamado no swipe para excluir
    public void excluirMovimentacao(RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configura AlertDialog
        alertDialog.setTitle("Excluir Movimentação da Conta");
        alertDialog.setMessage("Você tem certeza que deseja excluir esta movimentação da sua conta?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //variavel recebe o método que recupera a posição do item que arrastamos
                int position = viewHolder.getAdapterPosition();
                //variavel que recebe o acesso ao item da lista que arrastamos
                movimentacao = listaMovimentacoes.get(position);
                //Log.i("movimentacao", movimentacao.getKey());

                //Configurando o acesso ao Firebase para poder excluir o item arrastado da lista
                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64(emailUsuario);

                movimentacaoRef = firebaseRef.child("movimentacoes")
                        .child(idUsuario)
                        .child(mesAnoSelecionado);

                //variavel de referencia do dado do banco sendo usada com método removeValue
                movimentacaoRef.child(movimentacao.getKey()).removeValue();
                //Ainda não sei pra que usar este adapter abaixo
                adapterMovimentacoes.notifyItemRemoved(position);
                atualizarSaldo();
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(PrincipalActivity.this,
                        "Nenhuma alteração realizada",
                        Toast.LENGTH_SHORT).show();
                //para que o item arrastado volte para a lista usamos o método para cghmar novamente todos os dados
                adapterMovimentacoes.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }
    /*
    public void swipe() {

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Log.i("swipe", "Item foi arrastado");
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerMovimentos);

    }*/

    //RETIRADO: @Override
    //RETIRADO: public boolean onSupportNavigateUp() {
    //RETIRADO:     NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
    //RETIRADO:     return NavigationUI.navigateUp(navController, appBarConfiguration)
    //RETIRADO:             || super.onSupportNavigateUp();
    //RETIRADO: }

    public void atualizarSaldo() {

        //referencia do usuário
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        if (movimentacao.getTipo().equals("r")){
            receitaTotal = receitaTotal - movimentacao.getValor();
            usuarioRef.child("receitaTotal").setValue(receitaTotal);
        }

        if (movimentacao.getTipo().equals("d")){
            despesaTotal = despesaTotal - movimentacao.getValor();
            usuarioRef.child("despesaTotal").setValue(despesaTotal);
        }

    }

    //recuperar os dados de movimentacoes
    public void recuperarMovimentacoes() {

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        movimentacaoRef = firebaseRef.child("movimentacoes")
                .child(idUsuario)
                .child(mesAnoSelecionado);

        //Log.i("dadosRetorno", "dados: " + mesAnoSelecionado);

        //Log.i("Mes", "mes: " + mesAnoSelecionado);
        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Limpando as movimentações
                listaMovimentacoes.clear();
                //For para percorrer todos os filhos dentro de mesAnoSelecionado. GetChildren para recuperar todos os filhos de snapshot. GetValue vai recuperar vários objetos do tipo movimentacao
                for (DataSnapshot dados: snapshot.getChildren()) {
                    //Log.i("dados", "retorno: " + dados.toString());
                    Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                    //Log.i("dados", "retorno: " + movimentacao.getCategoria());
                    movimentacao.setKey(dados.getKey());
                    listaMovimentacoes.add(movimentacao);

                }
                //último método que vai chamar todos os dados que recuperamos no for
                adapterMovimentacoes.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //método para recuperar o saldo do usuario
    public void recuperarResumo() {

        //referencia do usuário
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        //Log.i("evento", "Evento foi adicionado");
        //Event listener para receber as mudanças no firebase, passar para um objeto usuarios e fazer as contas entre despesa total e receita total
        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String resultadoFormatado = decimalFormat.format(resumoUsuario);

                textoSaudacao.setText("Olá, " + usuario.getNome());
                textoSaldo.setText("R$ " + resultadoFormatado);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    //recuperando e inflando o xml do menu_principal em um menu na toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //método para verificar se algum item do menu foi clicado
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //estrutura de decisão para saber qual item foi clicado. Se for o botão sair, dá logout, termina a activity atual e inicia outra activity
        switch (item.getItemId()) {
            case R.id.menuSair:
                autenticacao.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void adicionarDespesa(View view) {
        startActivity(new Intent(this, DespesasActivity.class));
    }

    public void adicionarReceita(View view) {
        startActivity(new Intent(this, ReceitasActivity.class));
    }

    public void configuraCalendarView() {
        //Definir meses em portugues
        CharSequence meses[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "DALEmbro"};
        calendarView.setTitleMonths(meses);

        CalendarDay dataAtual = calendarView.getCurrentDate();
        //A variável abaixo está sendo usada para tratar o getMonth do dataAtual. Por padrão, se fosse um mês com apenas um dígito ele sai com apenas um dígito mesmo. Vamos padronizar para que tenhamos sempre dois dígitos no mês. % representa um caractere coringa para indicar que iniciaremoos uma formatação. Segue o 0 porque queremos preencher com 0. depois vem o 2 porque queremos dois caractereres. Depois vem o d de dígito, de número.
        String mesSelecionado = String.format("%02d", dataAtual.getMonth()+1);
        mesAnoSelecionado = String.valueOf(mesSelecionado + "" + dataAtual.getYear());

        //Método que verifica a navegação entre os meses
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                //mesma coisa da variável acima, fora deste @Override
                String mesSelecionado = String.format("%02d", date.getMonth()+1);
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear());
                //Log.i("data: ", "valor: " + (date.getMonth()+1) + "/" + date.getYear());
                //Na versão 1.4.3 que estamos usando temos que somar 1 ao Mês porque ele está começando do 0 e aí o número do mês com seu nome não bate. Na versão 2.0.0 isso foi corrigido

                //Já startamos o App anexando um evento de Listener das movimentações. Se não removermos este evento ficaremos adicionando mais e mais eventos toda vez que mudarmos o mês. E a segunda linha abaixo chama o método que recupera as movimentações toda vez que mudamos o mês
                movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
                recuperarMovimentacoes();
            }
        });
    }

}
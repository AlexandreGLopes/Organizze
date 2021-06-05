package com.cursoandroid.organizze.activity;

import android.content.Intent;
import android.os.Bundle;

import com.cursoandroid.organizze.config.ConfiguracaoFirebase;
import com.cursoandroid.organizze.helper.Base64Custom;
import com.cursoandroid.organizze.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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

    private MaterialCalendarView calendarView;
    private TextView textoSaudacao, textoSaldo;
    private Double despesaTotal = 0.00;
    private Double receitaTotal = 0.00;
    private Double resumoUsuario = 0.00;

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

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
        recuperarResumo();

    }

    //RETIRADO: @Override
    //RETIRADO: public boolean onSupportNavigateUp() {
    //RETIRADO:     NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
    //RETIRADO:     return NavigationUI.navigateUp(navController, appBarConfiguration)
    //RETIRADO:             || super.onSupportNavigateUp();
    //RETIRADO: }

    //método para recuperar o saldo do usuario
    public void recuperarResumo() {

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        //Event listener para receber as mudanças no firebase, passar para um objeto usuarios e fazer as contas entre despesa total e receita total
        usuarioRef.addValueEventListener(new ValueEventListener() {
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

        //Método que verifica a navegação entre os meses
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                Log.i("data: ", "valor: " + (date.getMonth()+1) + "/" + date.getYear());
                //Na versão 1.4.3 que estamos usando temos que somar 1 ao Mês porque ele está começando do 0 e aí o número do mês com seu nome não bate. Na versão 2.0.0 isso foi corrigido
            }
        });
    }
}
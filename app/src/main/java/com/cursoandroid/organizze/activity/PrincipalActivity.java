package com.cursoandroid.organizze.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

//RETIRADO: import com.cursoandroid.organizze.activity.databinding.ActivityPrincipalBinding;

import com.cursoandroid.organizze.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //RETIRADO: binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        //RETIRADO: setContentView(binding.getRoot());
        setContentView(R.layout.activity_principal);

        //RETIRADO: setSupportActionBar(binding.toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //RETIRADO: NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
        //RETIRADO: appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        //RETIRADO: NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        FloatingActionButton fab = findViewById(R.id.fab);
        //MODIFICADO: Abaixo era: binding.fab.setOnClickListener(new View.OnClickListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //RETIRADO: @Override
    //RETIRADO: public boolean onSupportNavigateUp() {
    //RETIRADO:     NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
    //RETIRADO:     return NavigationUI.navigateUp(navController, appBarConfiguration)
    //RETIRADO:             || super.onSupportNavigateUp();
    //RETIRADO: }
}
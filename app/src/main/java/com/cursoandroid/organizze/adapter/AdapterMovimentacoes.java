package com.cursoandroid.organizze.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.organizze.R;
import com.cursoandroid.organizze.model.Movimentacao;

import java.util.List;

public class AdapterMovimentacoes extends RecyclerView.Adapter<AdapterMovimentacoes.MyViewHolder> {

    private List<Movimentacao> listaMovimentacoes;
    Context context;


    public AdapterMovimentacoes(List<Movimentacao> lista, Context context) {
        this.listaMovimentacoes = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_lista, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMovimentacoes.MyViewHolder holder, int position) {

        Movimentacao movimentacao = listaMovimentacoes.get(position);
        holder.descricao.setText(movimentacao.getCategoria());
        holder.valor.setText(String.valueOf(movimentacao.getValor()));
        holder.categoria.setText(movimentacao.getCategoria());

        if (movimentacao.getTipo().equals("d")) {
            holder.valor.setTextColor(context.getResources().getColor(R.color.colorAccentDespesa));
            holder.valor.setText("-" + movimentacao.getValor());
        }

    }

    @Override
    public int getItemCount() {
        return listaMovimentacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView descricao;
        TextView valor;
        TextView categoria;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            descricao = itemView.findViewById(R.id.textDescricao);
            valor = itemView.findViewById(R.id.textValor);
            categoria = itemView.findViewById(R.id.textCategoria);

        }
    }
}

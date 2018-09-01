package br.fib.eleicaoapp.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.List;

import br.fib.eleicaoapp.R;
import br.fib.eleicaoapp.api.Candidato;
import br.fib.eleicaoapp.api.Constants;

public class ListaAdapter extends BaseAdapter {
    private Context context;
    private List<Candidato> lista;
    private ViewHolder holder;

    public ListaAdapter(Context applicationContext, List<Candidato> candidatos) {
        this.context = applicationContext;
        this.lista = candidatos;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lista.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Candidato candidato = lista.get(i);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.linha, null);
            holder = new ViewHolder();
            holder.nome = convertView.findViewById(R.id.nome);
            holder.foto = convertView.findViewById(R.id.foto);
            holder.partido = convertView.findViewById(R.id.partido);
            holder.percentual = convertView.findViewById(R.id.percentual);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nome.setText(candidato.getNome());
        holder.partido.setText(candidato.getPartido());
        holder.percentual.setText("Votos válidos: " + candidato.getVotosPercentuais() + "%");

        Ion.with(holder.foto)
                .centerCrop()
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error)
                .animateIn(R.anim.fade_in)
                .load(Constants.PATH_URL + "/" + candidato.getFoto());

        return convertView;
    }

    static class ViewHolder {
        TextView nome;
        ImageView foto;
        TextView partido;
        TextView percentual;
    }
}

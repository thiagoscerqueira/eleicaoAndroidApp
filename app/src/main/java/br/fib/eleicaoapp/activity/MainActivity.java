package br.fib.eleicaoapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toolbar;
import java.util.List;
import br.fib.eleicaoapp.R;
import br.fib.eleicaoapp.api.ApiClient;
import br.fib.eleicaoapp.api.ApiServices;
import br.fib.eleicaoapp.api.Candidato;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progress;
    private ApiServices apiServices;
    private List<Candidato> candidatos;
    private ListaAdapter adapter;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = findViewById(R.id.lista);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetalhesCandidatoActivity.class);
                System.out.println("iii:" + i);
                intent.putExtra("id",candidatos.get(i).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCandidatos();

    }

    private void getCandidatos(){
        progress = ProgressDialog.show(MainActivity.this, "Aguarde...", "Recebendo informações da WEB", true, true);
        apiServices = ApiClient.getClient().create(ApiServices.class);

        Call<List<Candidato>> call = apiServices.findAllCandidatos("application/json");
        call.enqueue(new Callback<List<Candidato>>() {
            @Override
            public void onResponse(Call<List<Candidato>> call, Response<List<Candidato>> response) {
                if (response.isSuccessful()) {
                    candidatos = response.body();
                    updateList();
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<List<Candidato>> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
            }
        });
    }

    private void updateList() {
        adapter = new ListaAdapter(getApplicationContext(), candidatos);
        lista.setAdapter(adapter);
    }
}

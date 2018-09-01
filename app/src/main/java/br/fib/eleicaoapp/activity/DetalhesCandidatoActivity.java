package br.fib.eleicaoapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import br.fib.eleicaoapp.R;
import br.fib.eleicaoapp.api.ApiClient;
import br.fib.eleicaoapp.api.ApiServices;
import br.fib.eleicaoapp.api.Candidato;
import br.fib.eleicaoapp.api.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhesCandidatoActivity extends AppCompatActivity {

    private TextView titulo;
    private ImageView capa;
    private TextView detalhes;
    private TextView site;
    private TextView propostas;
    private Long idCandidato;
    private Candidato candidato;
    private ProgressDialog progress;
    private ApiServices apiServices;
    private Button botao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_candidato);

        Toolbar toolbar = findViewById(R.id.toolbar_detalhes);
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        titulo = findViewById(R.id.nome_detalhe);
        capa = findViewById(R.id.capa);
        detalhes = findViewById(R.id.detalhes);
        site = findViewById(R.id.site);
        propostas = findViewById(R.id.propostas);

        botao = findViewById(R.id.botaoVoto);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = ProgressDialog.show(DetalhesCandidatoActivity.this, "Aguarde...", "Computando voto", true, true);
                apiServices = ApiClient.getClient().create(ApiServices.class);

                Call<Candidato> call = apiServices.sendVota("application/json", idCandidato);
                call.enqueue(new Callback<Candidato>() {
                    @Override
                    public void onResponse(Call<Candidato> call, Response<Candidato> response) {
                        progress.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                Toast.makeText(DetalhesCandidatoActivity.this, "Voto computado com sucesso!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Candidato> call, Throwable t) {
                        progress.dismiss();
                        t.printStackTrace();
                    }
                });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        if (intent == null) {
            return;
        }

        idCandidato = intent.getLongExtra("id", 1);

        if (idCandidato != null) {
            getCandidato(idCandidato);
        }
    }

    private void getCandidato(long idCandidato) {
        progress = ProgressDialog.show(DetalhesCandidatoActivity.this, "Aguarde...", "Recebendo informações da WEB", true, true);
        apiServices = ApiClient.getClient().create(ApiServices.class);

        Call<Candidato> call = apiServices.getCandidato("application/json", idCandidato);
        call.enqueue(new Callback<Candidato>() {
            @Override
            public void onResponse(Call<Candidato> call, Response<Candidato> response) {
                if (response.isSuccessful()) {
                    candidato = response.body();

                    if (candidato != null) {
                        titulo.setText(candidato.getNome());

                        Ion.with(capa)
                                .centerCrop()
                                .placeholder(R.drawable.place_holder)
                                .error(R.drawable.error)
                                .animateIn(R.anim.fade_in)
                                .load(Constants.PATH_URL + "/" + candidato.getFoto());

                        detalhes.setText(candidato.getDetalhes());
                        site.setText(candidato.getSite());
                        propostas.setText(candidato.getPropostas());
                    }

                }

                progress.dismiss();
            }

            @Override
            public void onFailure(Call<Candidato> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
            }
        });
    }

}

package br.edu.ifsp.scl.dicesdm;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int setupRequestCode = 1;
    private static final String setupStateKey = "setup";
    private Setup setup = Setup.getDefault();

    // Random usado para simular o lançamento do dado
    private Random geradorRandomico;

    // Componentes visuais
    private TextView resultadoTextView;
    private Button jogarDadoButton;
    private ImageView resultadoImageView;
    private ImageView resultado2ImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Após a criação da tela
        geradorRandomico = new Random(System.currentTimeMillis());

        // Recuperando referência para o resultadoTextView do arquivo de layout
        resultadoTextView = findViewById(R.id.resultadoTextView);

        // Recuperando referência para o jogarDadoButton do arquivo de layout
        jogarDadoButton = findViewById(R.id.jogarDadoButton);
        jogarDadoButton.setOnClickListener(this);

        // Recuperando referência para o resultadoImageView do arquivo de layout
        resultadoImageView = findViewById(R.id.resultadoImageView);

        resultado2ImageView = findViewById(R.id.resultado2ImageView);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(setupStateKey, setup);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        setup = (Setup) savedInstanceState.getSerializable(setupStateKey);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.jogarDadoButton) {
            // String que armazena números sorteados
            String resultadoText = "Faces sorteadas: ";

            if (setup.diceFacesAmount > 6) {
                resultadoImageView.setVisibility(View.GONE);
                resultado2ImageView.setVisibility(View.GONE);
            } else {
                resultadoImageView.setVisibility(View.VISIBLE);

                // Visibilidade do resultado2ImageView de acordo com número de dados
                if (setup.dicesAmount == 2) {
                    resultado2ImageView.setVisibility(View.VISIBLE);
                } else {
                    resultado2ImageView.setVisibility(View.GONE);
                    resultadoText = "Face sorteada: ";
                }
            }

            // Sorteando números de acordo com número de dados
            for (int i = 1; i <= setup.dicesAmount; i++) {
                int resultado = geradorRandomico.nextInt(setup.diceFacesAmount) + 1;
                resultadoText += resultado + ", ";
                ImageView iv = (i == 1) ? resultadoImageView : resultado2ImageView;
                setImageResource(iv, resultado);
            }

            resultadoTextView.setText(
                    resultadoText.substring(0,
                            resultadoText.lastIndexOf(',')));
        }
    }

    private void setImageResource(ImageView iv, int face) {
        switch (face) {
            case 1: iv.setImageResource(R.drawable.dice_1);
                break;
            case 2: iv.setImageResource(R.drawable.dice_2);
                break;
            case 3: iv.setImageResource(R.drawable.dice_3);
                break;
            case 4: iv.setImageResource(R.drawable.dice_4);
                break;
            case 5: iv.setImageResource(R.drawable.dice_5);
                break;
            case 6: iv.setImageResource(R.drawable.dice_6);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setup) {
            Intent intent = new Intent(this, SetupActivity.class);
            intent.putExtra(SetupActivity.setupExtra, setup);
            startActivityForResult(intent, setupRequestCode);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode != setupRequestCode) return;
        if (data == null) return;
        if (!data.hasExtra(SetupActivity.setupExtra)) return;
        setup = (Setup) data.getSerializableExtra(SetupActivity.setupExtra);
    }
}

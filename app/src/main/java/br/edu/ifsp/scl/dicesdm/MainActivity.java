package br.edu.ifsp.scl.dicesdm;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Random geradorRandomico;
    private static final int maxValue = 75;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        geradorRandomico = new Random(System.currentTimeMillis());

        GridView gridView = findViewById(R.id.bingo_grid_view);
        BingoGridAdapter adapter = new BingoGridAdapter(maxValue);
        gridView.setAdapter(adapter);

        TextView lastNumberTextView = findViewById(R.id.lastNumberTextView);

        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(button -> {
            if (adapter.isFilled()) {
                adapter.clearSelectedNumbers();
                playButton.setText(R.string.play);
                lastNumberTextView.setText(null);
                return;
            }

            int result;
            do result = geradorRandomico.nextInt(maxValue) + 1;
            while (adapter.alreadySelectedNumber(result));

            adapter.selectNumber(result);
            lastNumberTextView.setText(String.format(getString(R.string.lastNumber), result));

            if (adapter.isFilled()) playButton.setText(R.string.restart);
        });
    }
}

package br.edu.ifsp.scl.dicesdm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SetupActivity extends AppCompatActivity {
    private Spinner dicesAmountSpinner;
    private EditText diceFacesAmountEditText;

    public static final String setupExtra = "setup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        dicesAmountSpinner = findViewById(R.id.numDadosSpinner);
        diceFacesAmountEditText = findViewById(R.id.numFacesEditText);

        updateFieldsWithReceivedOrDefaultSetup();
        setupButtonListeners();
    }

    private void updateFieldsWithReceivedOrDefaultSetup() {
        Setup setup = getIntent().hasExtra(setupExtra) ?
                (Setup) getIntent().getSerializableExtra(setupExtra) :
                Setup.getDefault();
        updateFieldsWithSetup(setup);
    }

    private void updateFieldsWithSetup(Setup setup) {
        updateDicesAmountFieldWithSetup(setup);
        updateDiceFacesAmountFieldWithSetup(setup);
    }

    private void updateDicesAmountFieldWithSetup(Setup setup) {
        int selectedIndex = 0;
        for (int i = 0; i < dicesAmountSpinner.getCount(); i++) {
            String item = dicesAmountSpinner.getItemAtPosition(i).toString();
            if (item.equals(String.valueOf(setup.dicesAmount))) {
                selectedIndex = i;
                break;
            }
        }
        dicesAmountSpinner.setSelection(selectedIndex);
    }

    private void updateDiceFacesAmountFieldWithSetup(Setup setup) {
        diceFacesAmountEditText.setText(String.valueOf(setup.diceFacesAmount));
    }

    private void setupButtonListeners() {
        findViewById(R.id.apply).setOnClickListener(button -> {
            Intent result = new Intent();
            result.putExtra(setupExtra, new Setup(getDicesAmount(), getDiceFacesAmount()));
            setResult(RESULT_OK, result);
            finish();
        });

        findViewById(R.id.cancel).setOnClickListener(button -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private int getDicesAmount() {
        return Integer.parseInt(dicesAmountSpinner.getSelectedItem().toString());
    }

    private int getDiceFacesAmount() {
        try {
            return Integer.parseInt(diceFacesAmountEditText.getText().toString());
        } catch (NumberFormatException e) {
            return Setup.getDefault().diceFacesAmount;
        }
    }
}

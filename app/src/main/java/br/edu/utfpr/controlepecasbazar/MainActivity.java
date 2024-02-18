package br.edu.utfpr.controlepecasbazar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editTextValorVenda;
    private AutoCompleteTextView autoCompleteCor;
    private AutoCompleteTextView autoCompleteVendedora;
    private CheckBox cbUsado, cbNovo;
    private RadioGroup radioGroupCategorias;
    private Spinner spinnerEstampa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Iniciando os componentes
        autoCompleteVendedora = findViewById(R.id.autoCompleteVendedor);
        editTextValorVenda = findViewById(R.id.editTextValorVenda);
        radioGroupCategorias = findViewById(R.id.radioGroupCategorias);
        spinnerEstampa = findViewById(R.id.spinnerEstampa);
        autoCompleteCor = findViewById(R.id.autoCompleteCor);
        cbUsado = findViewById(R.id.checkBoxUsado);
        cbNovo = findViewById(R.id.checkBoxNovo);

        //AutoComplete das vendedoras
        ArrayAdapter<CharSequence> adapterVendedora = ArrayAdapter.createFromResource(this, R.array.nomeVendedoras, android.R.layout.simple_dropdown_item_1line);
        autoCompleteVendedora.setAdapter(adapterVendedora);

        //AutoComplete das cores
        ArrayAdapter<CharSequence> adapterCor = ArrayAdapter.createFromResource(this, R.array.cores, android.R.layout.simple_dropdown_item_1line);
        autoCompleteCor.setAdapter(adapterCor);

        popularSpinnerEstampas();
    }

    public void limparCampos(View view){
        autoCompleteVendedora.setText(null);
        radioGroupCategorias.clearCheck();
        autoCompleteCor.setText(null);
        cbUsado.setChecked(false);
        cbNovo.setChecked(false);
        editTextValorVenda.setText(null);
        spinnerEstampa.setSelection(0);

        autoCompleteVendedora.requestFocus();

        Toast.makeText(this, R.string.mensagem_limpeza, Toast.LENGTH_LONG).show();
    }

    public void cadastrar(View view){
        //vendedora
        String vendedora = autoCompleteVendedora.getText().toString();
        if (vendedora.trim().isEmpty()) {
            Toast.makeText(this, R.string.erro_vazio_vendedora, Toast.LENGTH_LONG).show();
            autoCompleteVendedora.requestFocus();
            return;
        }

        //cor
        String cor = autoCompleteCor.getText().toString();
        if (cor.trim().isEmpty()) {
            Toast.makeText(this, R.string.erro_vazio_cor, Toast.LENGTH_LONG).show();
            autoCompleteCor.requestFocus();
            return;
        }

        //valor de venda
        float valorVenda;
        try {
            valorVenda = Float.parseFloat(editTextValorVenda.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.erro_vazio_valorVenda, Toast.LENGTH_LONG).show();
            editTextValorVenda.requestFocus();
            return;
        }

        //categoria
        int botaoSelecionado = radioGroupCategorias.getCheckedRadioButtonId();
        String mensagem2 = "";

        if (botaoSelecionado == R.id.radioButtonAcessorio) {
            mensagem2 = getString(R.string.acessorio);
        } else if (botaoSelecionado == R.id.radioButtonBolsa) {
            mensagem2 = getString(R.string.bolsa);
        } else if (botaoSelecionado == R.id.radioButtonCalca) {
            mensagem2 = getString(R.string.calca);
        } else if (botaoSelecionado == R.id.radioButtonShortSaia) {
            mensagem2 = getString(R.string.shortsaia);
        } else if (botaoSelecionado == R.id.radioButtonSueter) {
            mensagem2 = getString(R.string.sueter);
        } else if (botaoSelecionado == R.id.radioButtonCamiseta) {
            mensagem2 = getString(R.string.camiseta);
        } else if (botaoSelecionado == R.id.radioButtonVestido) {
            mensagem2 = getString(R.string.vestido);
        } else if (botaoSelecionado == R.id.radioButtonCasaco) {
            mensagem2 = getString(R.string.casaco);
        } else if (botaoSelecionado == R.id.radioButtonPraia) {
            mensagem2 = getString(R.string.modapraia);
        } else if (botaoSelecionado == R.id.radioButtonCalcado) {
            mensagem2 = getString(R.string.calcado);
        } else {
            mensagem2 = getString(R.string.erro_vazio_categoria);
        }

        //estado
        String mensagem1 = "";
        if (cbUsado.isChecked()){
            mensagem1 += getString(R.string.checkboxUsado) + "\n";
        } else if (cbNovo.isChecked()){
            mensagem1 += getString(R.string.checkBoxNovo) + "\n";
        } else if (mensagem1.isEmpty()){
            mensagem1 = getString(R.string.erro_vazio_estado);
        }

        //Mensagem TOAST
        String mensagemFinal = getString(R.string.mensagem_cadastro) + "\n" +
                getString(R.string.vendedora) + vendedora + "\n" +
                getString(R.string.categoria) + mensagem2 + "\n" +
                getString(R.string.estampa) + spinnerEstampa.getSelectedItem().toString() + "\n" +
                getString(R.string.cor) + cor + "\n" +
                getString(R.string.estado) + mensagem1+
                getString(R.string.valor_venda) + valorVenda;

        Toast.makeText(this, mensagemFinal, Toast.LENGTH_LONG).show();
    }

    private void popularSpinnerEstampas(){
        String[] arrayEstampas = getResources().getStringArray(R.array.estampas);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayEstampas);

        spinnerEstampa.setAdapter(adapter);
    }
}
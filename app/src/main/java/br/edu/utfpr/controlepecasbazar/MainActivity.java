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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNome, editTextValorVenda;
    private AutoCompleteTextView autoCompleteCor;
    private CheckBox cbUsado, cbNovo;
    private RadioGroup radioGroupCategorias;
    private Spinner spinnerEstampa;
    private ListView listViewVendedoras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Iniciando os componentes
        editTextNome = findViewById(R.id.editTextNome);
        editTextValorVenda = findViewById(R.id.editTextValorVenda);
        radioGroupCategorias = findViewById(R.id.radioGroupCategorias);
        spinnerEstampa = findViewById(R.id.spinnerEstampa);
        autoCompleteCor = findViewById(R.id.autoCompleteCor);
        cbUsado = findViewById(R.id.checkBoxUsado);
        cbNovo = findViewById(R.id.checkBoxNovo);
        listViewVendedoras = findViewById(R.id.listViewVendedoras);

        //AutoComplete das cores
        ArrayAdapter<CharSequence> adapterCor = ArrayAdapter.createFromResource(this, R.array.cores, android.R.layout.simple_dropdown_item_1line);
        autoCompleteCor.setAdapter(adapterCor);

        //ListView das Vendedoras
        listViewVendedoras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String vendedora = (String) listViewVendedoras.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(), vendedora + getString(R.string.foi_selecionada), Toast.LENGTH_LONG).show();
            }
        });

        popularListaVendedoras();
        popularSpinnerEstampas();
    }

    public void limparCampos(View view){
        editTextNome.setText(null);
        radioGroupCategorias.clearCheck();
        autoCompleteCor.setText(null);
        cbUsado.setChecked(false);
        cbNovo.setChecked(false);
        editTextValorVenda.setText(null);
        spinnerEstampa.setSelection(0);

        editTextNome.requestFocus();

        Toast.makeText(this, R.string.mensagem_limpeza, Toast.LENGTH_LONG).show();
    }

    public void cadastrar(View view){
        //nome
        String nome = editTextNome.getText().toString();
        if (nome.trim().isEmpty()) {
            Toast.makeText(this, R.string.erro_vazio_categoria, Toast.LENGTH_LONG).show();
            editTextNome.requestFocus();
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
        double valorVenda;
        try {
            valorVenda = Double.parseDouble(editTextValorVenda.getText().toString());
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
        } else if (botaoSelecionado == R.id.radioButtonCalca) {
            mensagem2 = getString(R.string.calca);
        } else if (botaoSelecionado == R.id.radioButtonShortSaia) {
            mensagem2 = getString(R.string.shortsaia);
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
            mensagem2 = getString(R.string.nenhuma_opcao_selecionada);
        }

        //estado
        String mensagem1 = "";
        if (cbUsado.isChecked()){
            mensagem1 += getString(R.string.checkboxUsado) + "\n";
        }

        if (cbNovo.isChecked()){
            mensagem1 += getString(R.string.checkBoxNovo) + "\n";
        }

        if (mensagem1.isEmpty()){
            mensagem1 = getString(R.string.nenhuma_opcao_selecionada);
        }

        //Mensagem TOAST
        String mensagemFinal = getString(R.string.mensagem_cadastro) + "\n" +
                getString(R.string.nome) + nome + "\n" +
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

    private void popularListaVendedoras(){
        String[] nomesVendedoras = getResources().getStringArray(R.array.nomeVendedoras);

        ArrayAdapter<String> adapterPecas = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nomesVendedoras);

        listViewVendedoras.setAdapter(adapterPecas);
    }
}
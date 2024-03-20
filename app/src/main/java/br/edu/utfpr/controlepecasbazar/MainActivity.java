package br.edu.utfpr.controlepecasbazar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
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

    public static final String VENDEDORA = "VENDEDORA";
    public static final String CATEGORIA = "CATEGORIA";
    public static final String COR = "COR";
    public static final String VALOR = "VALOR";
    public static final String ESTAMPA = "ESTAMPA";
    public static final String ESTADO = "ESTADO";

    public static final String MODO = "MODO";
    public static final int SEMEDICAO = 1;
    public static final int EDITAR = 2;
    private int modo;

    private String vendedorOriginal;
    private String categoriaOriginal;
    private String corOriginal;
    private float valorOriginal;
    private String estampaOriginal;
    private String estadoOriginal;


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

        //botao Up
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Editar campos
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            modo = bundle.getInt(MODO, SEMEDICAO);

            if(modo == SEMEDICAO){
                setTitle(getString(R.string.nova_peca));
            } else if(modo == EDITAR){
                setTitle(getString(R.string.editar_peca));

                vendedorOriginal = bundle.getString(VENDEDORA);
                autoCompleteVendedora.setText(vendedorOriginal);
                autoCompleteVendedora.setSelection(autoCompleteVendedora.getText().length());

                corOriginal = bundle.getString(COR);
                autoCompleteCor.setText(corOriginal);
                autoCompleteCor.setSelection(autoCompleteCor.getText().length());

                valorOriginal = bundle.getFloat(VALOR);
                editTextValorVenda.setText(Float.toString(valorOriginal));
                editTextValorVenda.setSelection(editTextValorVenda.getText().length());

                estadoOriginal = bundle.getString(ESTADO);
                if (estadoOriginal != null) {
                    if (estadoOriginal.equals(getString(R.string.checkboxUsado))) {
                        cbUsado.setChecked(true);
                        cbNovo.setChecked(false);
                    } else if (estadoOriginal.equals(getString(R.string.checkBoxNovo))) {
                        cbNovo.setChecked(true);
                        cbUsado.setChecked(false);
                    }
                }

                categoriaOriginal = bundle.getString(CATEGORIA);
                for (int i = 0; i < radioGroupCategorias.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) radioGroupCategorias.getChildAt(i);

                    if (radioButton.getText().toString().equals(categoriaOriginal)) {
                        radioButton.setChecked(true);
                        break;
                    }
                }

                estampaOriginal = bundle.getString(ESTAMPA);
                if (estampaOriginal != null) {
                    String[] arrayEstampas = getResources().getStringArray(R.array.estampas);

                    for (int i = 0; i < arrayEstampas.length; i++) {
                        if (arrayEstampas[i].equals(estampaOriginal)) {
                            spinnerEstampa.setSelection(i);
                            break;
                        }
                    }
                }

            }

        }

        popularSpinnerEstampas();

        setTitle(R.string.tituloPagCadastro);
    }

    public void limparCampos(){
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

    public void cadastrar(){
        Intent intent = new Intent();

        //vendedora
        String vendedora = autoCompleteVendedora.getText().toString();
        if (vendedora.trim().isEmpty()) {
            Toast.makeText(this, R.string.erro_vazio_vendedora, Toast.LENGTH_LONG).show();
            autoCompleteVendedora.requestFocus();
            return;
        }
        intent.putExtra(VENDEDORA, vendedora);

        //cor
        String cor = autoCompleteCor.getText().toString();
        if (cor.trim().isEmpty()) {
            Toast.makeText(this, R.string.erro_vazio_cor, Toast.LENGTH_LONG).show();
            autoCompleteCor.requestFocus();
            return;
        }
        intent.putExtra(COR, cor);

        //valor de venda
        float valorVenda;
        try {
            valorVenda = Float.parseFloat(editTextValorVenda.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.erro_vazio_valorVenda, Toast.LENGTH_LONG).show();
            editTextValorVenda.requestFocus();
            return;
        }
        intent.putExtra(VALOR, valorVenda);

        //categoria
        int botaoSelecionado = radioGroupCategorias.getCheckedRadioButtonId();
        String mensagem2 = "";
        String categoria = "";

        if (botaoSelecionado == R.id.radioButtonAcessorio) {
            mensagem2 = getString(R.string.acessorio);
            categoria = getString(R.string.acessorio);
        } else if (botaoSelecionado == R.id.radioButtonBolsa) {
            mensagem2 = getString(R.string.bolsa);
            categoria = getString(R.string.bolsa);
        } else if (botaoSelecionado == R.id.radioButtonCalca) {
            mensagem2 = getString(R.string.calca);
            categoria = getString(R.string.calca);
        } else if (botaoSelecionado == R.id.radioButtonShortSaia) {
            mensagem2 = getString(R.string.shortsaia);
            categoria = getString(R.string.shortsaia);
        } else if (botaoSelecionado == R.id.radioButtonSueter) {
            mensagem2 = getString(R.string.sueter);
            categoria = getString(R.string.sueter);
        } else if (botaoSelecionado == R.id.radioButtonCamiseta) {
            mensagem2 = getString(R.string.camiseta);
            categoria = getString(R.string.camiseta);
        } else if (botaoSelecionado == R.id.radioButtonVestido) {
            mensagem2 = getString(R.string.vestido);
            categoria = getString(R.string.vestido);
        } else if (botaoSelecionado == R.id.radioButtonCasaco) {
            mensagem2 = getString(R.string.casaco);
            categoria = getString(R.string.casaco);
        } else if (botaoSelecionado == R.id.radioButtonPraia) {
            mensagem2 = getString(R.string.modapraia);
            categoria = getString(R.string.modapraia);
        } else if (botaoSelecionado == R.id.radioButtonCalcado) {
            mensagem2 = getString(R.string.calcado);
            categoria = getString(R.string.calcado);
        } else {
            mensagem2 = getString(R.string.erro_vazio_categoria);
        }
        intent.putExtra(CATEGORIA, categoria);

        //estado
        String mensagem1 = "";
        String estado = "";

        if (cbUsado.isChecked()){
            mensagem1 += getString(R.string.checkboxUsado) + "\n";
            estado = getString(R.string.checkboxUsado);
        } else if (cbNovo.isChecked()){
            mensagem1 += getString(R.string.checkBoxNovo) + "\n";
            estado = getString(R.string.checkBoxNovo);
        } else if (mensagem1.isEmpty()){
            mensagem1 = getString(R.string.erro_vazio_estado);
        }
        intent.putExtra(ESTADO, estado);

        String estampa = spinnerEstampa.getSelectedItem().toString();
        intent.putExtra(ESTAMPA, estampa);

        //Mensagem TOAST
        String mensagemFinal = getString(R.string.mensagem_cadastro) + "\n" +
                getString(R.string.vendedora) + vendedora + "\n" +
                getString(R.string.categoria) + mensagem2 + "\n" +
                getString(R.string.estampa) + spinnerEstampa.getSelectedItem().toString() + "\n" +
                getString(R.string.cor) + cor + "\n" +
                getString(R.string.estado) + mensagem1+
                getString(R.string.valor_venda) + valorVenda;

        Toast.makeText(this, mensagemFinal, Toast.LENGTH_LONG).show();

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void popularSpinnerEstampas(){
        String[] arrayEstampas = getResources().getStringArray(R.array.estampas);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayEstampas);

        spinnerEstampa.setAdapter(adapter);
    }

    public static void novaPeca(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher){
        Intent intent = new Intent(activity, MainActivity.class);

        intent.putExtra(MODO, SEMEDICAO);

        launcher.launch(intent);
    }

    public static void editarPeca(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher, Pecas peca){
        Intent intent = new Intent(activity, MainActivity.class);

        intent.putExtra(MODO, EDITAR);
        intent.putExtra(VENDEDORA, peca.getVendedora());
        intent.putExtra(CATEGORIA, peca.getCategoria());
        intent.putExtra(COR, peca.getCor());
        intent.putExtra(VALOR, peca.getValor());
        intent.putExtra(ESTADO, peca.getEstado());
        intent.putExtra(ESTAMPA, peca.getEstampa());

        launcher.launch(intent);
    }

    public void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cadastro_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idMenuItem = item.getItemId();

        if(idMenuItem == R.id.menuItemSalvar){
            Toast.makeText(this, getString(R.string.salvar) + getString(R.string.foi_selecionada), Toast.LENGTH_LONG).show();
            cadastrar();
            return true;
        } else if (idMenuItem == R.id.menuItemLimpar){
            Toast.makeText(this, getString(R.string.limpar) + getString(R.string.foi_selecionada), Toast.LENGTH_LONG).show();
            limparCampos();
            return true;
        } else if (idMenuItem == android.R.id.home){
            Toast.makeText(this, getString(R.string.cancelar) + getString(R.string.foi_selecionada), Toast.LENGTH_LONG).show();
            cancelar();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}
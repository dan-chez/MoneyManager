package co.danchez.moneymanager.ui.Transactions;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import co.danchez.moneymanager.R;
import co.danchez.moneymanager.Utilidades.Intefaces.TextWatcherInterface;
import co.danchez.moneymanager.Utilidades.OwnTextWatcher;
import co.danchez.moneymanager.Utilidades.SpinnerAdapter;
import co.danchez.moneymanager.Utilidades.Util;

public class AddTransactionsFragment extends Fragment implements TextWatcherInterface {

    private SpinnerAdapter tipdocAdapter;
    private AppCompatSpinner sp_persons;
    private boolean primeraCarga;
    private TextInputEditText et_value;
    private OwnTextWatcher twValue;

    public AddTransactionsFragment() {
        // Required empty public constructor
    }

    public static AddTransactionsFragment newInstance() {
        AddTransactionsFragment fragment = new AddTransactionsFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_transactions, container, false);
        loadObjects(view);
        return view;
    }

    private void loadObjects(View view) {
        sp_persons = view.findViewById(R.id.sp_persons);
        et_value = view.findViewById(R.id.et_value);
        TextView tv_error_sp_persons = view.findViewById(R.id.tv_error_sp_persons);
        TextView tv_error_et_value = view.findViewById(R.id.tv_error_et_value);

        twValue = new OwnTextWatcher(getContext(), et_value, this, tv_error_et_value);
        twValue.setValidaObligatorio(true, getResources().getString(R.string.error_edittext_requiered));
        et_value.addTextChangedListener(twValue);
        tipdocAdapter = new SpinnerAdapter(getContext(), R.layout.layout_spinner, new ArrayList<String>(), tv_error_sp_persons, getResources().getString(R.string.error_spinner_requiered));
        ArrayList<String> listaString = new ArrayList<>();
        listaString.add("Selecciona");
        listaString.add("Selecciona 1");
        listaString.add("Selecciona 2");
        listaString.add("Selecciona 3");
        /*for (int cTD = 0; cTD < typesDocuments.size(); cTD++)
            listaString.add(typesDocuments.get(cTD).name);*/
        tipdocAdapter.setItemList(listaString);
        sp_persons.setAdapter(tipdocAdapter);
        sp_persons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Util.hideSoftKeyboard(getActivity());
                if (!primeraCarga) {
                    tipdocAdapter.setSelectedItem(i);
                    tipdocAdapter.validar(true);
                    validarEstadoCampos();
                    /*userEditText.requestFocus();
                    GeneralCore.showSoftKeyboard(getActivity());*/
                } else {
                    primeraCarga = false;
                }
                /*if (typesDocuments.get(type_document_spinner.getSelectedItemPosition()).param.equals("Pasaporte")) {
                    userEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                    userEditText.setKeyListener(DigitsKeyListener.getInstance(getResources().getString(R.string.edittext_digits_placa)));
                } else {
                    userEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    userEditText.setKeyListener(DigitsKeyListener.getInstance(getResources().getString(R.string.edittext_digits_telefono)));
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private boolean validarEstadoCampos() {
        twValue.validar(true);
        tipdocAdapter.validar(true);
        /*if (!twValue.getConError() && !twPassword.getConError() && !tipdocAdapter.isConError() && !userEditText.getText().toString().equals("") && !passwordEditText.getText().toString().equals("")) {
            Util.cambiarEstadoButton(getContext(), loginBtn, true);
            return true;
        } else {
            GeneralCore.cambiarEstadoButton(getContext(), loginBtn, false);
            return false;
        }*/
        return true;
    }

    @Override
    public void onTextChange(EditText view) {

    }
}
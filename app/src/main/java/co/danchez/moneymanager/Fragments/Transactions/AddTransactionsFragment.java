package co.danchez.moneymanager.Fragments.Transactions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import co.danchez.moneymanager.Activities.MainActivity;
import co.danchez.moneymanager.Connectivity.FirebaseManager;
import co.danchez.moneymanager.Connectivity.Models.Users;
import co.danchez.moneymanager.R;
import co.danchez.moneymanager.Utilidades.ConstantList;
import co.danchez.moneymanager.Utilidades.DialogGeneral;
import co.danchez.moneymanager.Utilidades.Intefaces.TextWatcherInterface;
import co.danchez.moneymanager.Utilidades.OwnTextWatcher;
import co.danchez.moneymanager.Utilidades.SharedPreferencesUtil;
import co.danchez.moneymanager.Utilidades.SpinnerAdapter;
import co.danchez.moneymanager.Utilidades.Util;

public class AddTransactionsFragment extends Fragment implements TextWatcherInterface, View.OnClickListener {

    private SpinnerAdapter tipdocAdapter;
    private AppCompatSpinner sp_persons;
    private boolean firstLoad = true;
    private TextInputEditText et_value, et_comment;
    private OwnTextWatcher twValue;
    private Button btn_add_transaction;
    private FirebaseManager firebaseManager;
    private List<Users> usersList;
    private ArrayList<String> namesList;

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
        View view = inflater.inflate(R.layout.fragment_add_transactions, container, false);
        Util.hideSoftKeyboard(Objects.requireNonNull(getActivity()));
        loadObjects(view);
        return view;
    }

    private void loadSpinners() {
        ArrayList<String> namesList = new ArrayList<>();
        namesList.add("Seleccionar usuario");
        for (int i = 0; i < usersList.size(); i++) namesList.add(usersList.get(i).getNAME_USER());
        tipdocAdapter.setItemList(namesList);
        sp_persons.setAdapter(tipdocAdapter);
    }

    private void loadObjects(View view) {
        sp_persons = view.findViewById(R.id.sp_persons);
        et_value = view.findViewById(R.id.et_value);
        et_comment = view.findViewById(R.id.et_comment);
        btn_add_transaction = view.findViewById(R.id.btn_add_transaction);
        btn_add_transaction.setOnClickListener(this);
        TextView tv_error_sp_persons = view.findViewById(R.id.tv_error_sp_persons);
        TextView tv_error_et_value = view.findViewById(R.id.tv_error_et_value);

        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(Objects.requireNonNull(getActivity()));

        firebaseManager = ((MainActivity) getActivity()).getFirebaseManager();
        ((MainActivity) Objects.requireNonNull(getActivity())).loadingView.showLoading();
        firebaseManager.readAllDataFromCollection(task -> {
            ((MainActivity) getActivity()).loadingView.hideLoading();
            if (task.isSuccessful()) {
                usersList = new ArrayList<>();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Users user = document.toObject(Users.class);
                    user.setIdUser(document.getId());
                    usersList.add(user);
                }
                loadSpinners();
            } else {
                Toast.makeText(getActivity(), getString(R.string.error_get_data), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "Error getting users. ", task.getException());
            }
        }, e -> {
            ((MainActivity) getActivity()).loadingView.hideLoading();
            DialogGeneral
                    .newInstance()
                    .setIcon(R.drawable.ic_error)
                    .setTitle(getString(R.string.error))
                    .setSubtitle(getString(R.string.error_set_data))
                    .isAccept(null)
                    .show(getFragmentManager(), "");
            Log.e("TAG", "Error getting users. ", e.getCause());
        }, ConstantList.USERS_COLLECTION, ConstantList.ID_TEAM_USER, sharedPreferencesUtil.readStringPreference(ConstantList.ID_TEAM_PREFERENCES));

        twValue = new OwnTextWatcher(getContext(), et_value, this, tv_error_et_value);
        twValue.setValidaObligatorio(true, getResources().getString(R.string.error_edittext_requiered));
        et_value.addTextChangedListener(twValue);
        tipdocAdapter = new SpinnerAdapter(getContext(), R.layout.layout_spinner, new ArrayList<>(), tv_error_sp_persons, getResources().getString(R.string.error_spinner_requiered));
        sp_persons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Util.hideSoftKeyboard(getActivity());
                if (!firstLoad) {
                    tipdocAdapter.setSelectedItem(i);
                    tipdocAdapter.validar(true);
                    validateFields();
                    et_value.requestFocus();
                } else firstLoad = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "Debes seleccionar ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateFields() {
        twValue.validar(true);
        tipdocAdapter.validar(true);
        if (!twValue.getConError() && !tipdocAdapter.isConError()) {
            Util.changeButtonState(getContext(), btn_add_transaction, true);
            return true;
        } else {
            Util.changeButtonState(getContext(), btn_add_transaction, false);
            return false;
        }
    }

    @Override
    public void onTextChange(EditText view) {
        validateFields();
    }

    @Override
    public void onClick(View v) {
        if (validateFields()) {
            Util.hideSoftKeyboard(Objects.requireNonNull(getActivity()));
            Map<String, Object> newObject = new HashMap<>();
            newObject.put(ConstantList.ID_USER_TRANSACTION, usersList.get(sp_persons.getSelectedItemPosition() - 1).getIdUser());
            newObject.put(ConstantList.NAME_USER_TRANSACTION, usersList.get(sp_persons.getSelectedItemPosition() - 1).getNAME_USER());
            newObject.put(ConstantList.ID_TEAM_TRANSACTION, usersList.get(sp_persons.getSelectedItemPosition() - 1).getID_TEAM_USER());
            newObject.put(ConstantList.VALUE_TRANSACTION, Objects.requireNonNull(et_value.getText()).toString());
            newObject.put(ConstantList.COMMENT_TRANSACTION, Objects.requireNonNull(et_comment.getText()).toString());
            newObject.put(ConstantList.PAID_TRANSACTION, false);
            newObject.put(ConstantList.CREATION_DATE_TRANSACTION, FieldValue.serverTimestamp());
            firebaseManager.addElement(newObject, documentReference -> {
                Toast.makeText(getActivity(), getString(R.string.add_transaction_success), Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }, e -> {
                DialogGeneral
                        .newInstance()
                        .setIcon(R.drawable.ic_error)
                        .setTitle(getString(R.string.error))
                        .setSubtitle(getString(R.string.error_set_data))
                        .isAccept(null)
                        .show(getFragmentManager(), "");
                Log.e("TAG", "Error adding transaction. ", e.getCause());
            }, ConstantList.TRANSACTION_COLLECTION);
        }
    }

}
package co.danchez.moneymanager.Fragments.Transactions;

import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import co.danchez.moneymanager.Activities.MainActivity;
import co.danchez.moneymanager.Connectivity.FirebaseManager;
import co.danchez.moneymanager.R;
import co.danchez.moneymanager.Utilidades.ConstantList;
import co.danchez.moneymanager.Utilidades.SharedPreferencesUtil;
import co.danchez.moneymanager.Utilidades.Util;

import static co.danchez.moneymanager.Utilidades.ConstantList.ID_TEAM_PREFERENCES;
import static co.danchez.moneymanager.Utilidades.ConstantList.ID_USER;
import static co.danchez.moneymanager.Utilidades.ConstantList.TEAMS_COLLECTION;

public class AddJoinTeamFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText et_id_team, et_name_team;
    private Button btn_finish, btn_create;
    private FirebaseManager firebaseManager;
    private LinearLayout ll_create_team, ll_join_team;
    private ViewGroup redLayout;
    private TextView tv_error_et_id_team, tv_error_et_name_team;
    private SharedPreferencesUtil sharedPreferencesUtil;

    public AddJoinTeamFragment() {
        // Required empty public constructor
    }

    public static AddJoinTeamFragment newInstance() {
        AddJoinTeamFragment fragment = new AddJoinTeamFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_join_team, container, false);
        Util.hideSoftKeyboard(Objects.requireNonNull(getActivity()));
        loadObjects(view);
        return view;
    }

    private void loadObjects(View view) {
        et_id_team = view.findViewById(R.id.et_id_team);
        et_name_team = view.findViewById(R.id.et_name_team);
        btn_finish = view.findViewById(R.id.btn_finish);
        btn_create = view.findViewById(R.id.btn_create);
        ll_create_team = view.findViewById(R.id.ll_create_team);
        ll_join_team = view.findViewById(R.id.ll_join_team);
        redLayout = view.findViewById(R.id.redLayout);
        tv_error_et_id_team = view.findViewById(R.id.tv_error_et_id_team);
        tv_error_et_name_team = view.findViewById(R.id.tv_error_et_name_team);

        btn_finish.setOnClickListener(this);
        btn_create.setOnClickListener(this);

        firebaseManager = new FirebaseManager();
        sharedPreferencesUtil = new SharedPreferencesUtil(Objects.requireNonNull(getActivity()));

        TextView tv_create_team_title = view.findViewById(R.id.tv_create_team_title);
        TextView tv_join_team_title = view.findViewById(R.id.tv_join_team_title);
        tv_create_team_title.setOnClickListener(this);
        SpannableString mitextoU = new SpannableString(getString(R.string.create_team_title));
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        SpannableString mitextoU1 = new SpannableString(getString(R.string.add_team_title));
        mitextoU1.setSpan(new UnderlineSpan(), 0, mitextoU1.length(), 0);
        tv_create_team_title.setText(mitextoU);
        tv_join_team_title.setOnClickListener(this);
        tv_join_team_title.setText(mitextoU1);

        et_id_team.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    tv_error_et_id_team.setVisibility(View.VISIBLE);
                    Util.changeButtonState(getContext(), btn_finish, false);
                } else {
                    tv_error_et_id_team.setVisibility(View.INVISIBLE);
                    Util.changeButtonState(getContext(), btn_finish, true);
                }
            }
        });
        et_name_team.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    tv_error_et_name_team.setVisibility(View.VISIBLE);
                    Util.changeButtonState(getContext(), btn_create, false);
                } else {
                    tv_error_et_name_team.setVisibility(View.INVISIBLE);
                    Util.changeButtonState(getContext(), btn_create, true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Util.hideSoftKeyboard(Objects.requireNonNull(getActivity()));
        int id = v.getId();
        if (id == R.id.tv_create_team_title || id == R.id.tv_join_team_title) {
            ll_create_team.setVisibility(ll_create_team.getVisibility() != View.VISIBLE ? View.VISIBLE : View.GONE);
            ll_join_team.setVisibility(ll_create_team.getVisibility() != View.VISIBLE ? View.VISIBLE : View.GONE);
            tv_error_et_id_team.setVisibility(View.INVISIBLE);
            tv_error_et_name_team.setVisibility(View.INVISIBLE);
        } else if (id == R.id.btn_finish) {
            if (!Objects.requireNonNull(et_id_team.getText()).toString().isEmpty()) {
                tv_error_et_id_team.setVisibility(View.INVISIBLE);
                ((MainActivity) Objects.requireNonNull(getActivity())).showProgress();
                firebaseManager.readDataFromCollection(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ((MainActivity) Objects.requireNonNull(getActivity())).hideProgress();
                        if (task.isSuccessful()) {
                            if (Objects.requireNonNull(task.getResult()).size() > 0) {
                                sharedPreferencesUtil.saveStringPreference(ID_TEAM_PREFERENCES, et_id_team.getText().toString());
                                updateUserInfo(et_id_team.getText().toString());
                            } else {
                                Util.alertDialogSimple(getActivity(), getString(R.string.error_team_doesnt_found));
                            }
                        } else {
                            Util.alertDialogSimple(getActivity(), getString(R.string.error_team_doesnt_found));
                        }
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ((MainActivity) Objects.requireNonNull(getActivity())).hideProgress();
                        Util.alertDialogSimple(getActivity(), getString(R.string.error_get_data));
                    }
                }, TEAMS_COLLECTION, ID_TEAM_PREFERENCES, et_id_team.getText().toString());
            } else {
                tv_error_et_id_team.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.btn_create) {
            if (!Objects.requireNonNull(et_name_team.getText()).toString().isEmpty()) {
                tv_error_et_name_team.setVisibility(View.INVISIBLE);
                ((MainActivity) Objects.requireNonNull(getActivity())).showProgress();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                Map<String, Object> newObject = new HashMap<>();
                newObject.put(ConstantList.NAME_TEAM, et_name_team.getText().toString());
                newObject.put(ConstantList.CREATOR_NAME_TEAM, Objects.requireNonNull(currentUser).getDisplayName());
                newObject.put(ConstantList.CREATION_DATE_TEAM, FieldValue.serverTimestamp());
                firebaseManager.addElement(newObject, new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        sharedPreferencesUtil.saveStringPreference(ID_TEAM_PREFERENCES, documentReference.getId());
                        updateUserInfo(documentReference.getId());
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ((MainActivity) Objects.requireNonNull(getActivity())).hideProgress();
                        Util.alertDialogSimple(getActivity(), getString(R.string.error_set_data));
                    }
                }, ConstantList.TEAMS_COLLECTION);
            } else {
                tv_error_et_name_team.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateUserInfo(String idTeam) {
        Map<String, Object> newObject = new HashMap<>();
        newObject.put(ConstantList.ID_TEAM_USER, idTeam);
        firebaseManager.updateElement(newObject, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ((MainActivity) Objects.requireNonNull(getActivity())).hideProgress();
                ((MainActivity) Objects.requireNonNull(getActivity())).loadTransactionsFragment();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ((MainActivity) Objects.requireNonNull(getActivity())).hideProgress();
                Util.alertDialogSimple(getActivity(), getString(R.string.error_set_data));
            }
        }, ConstantList.USERS_COLLECTION, ID_USER);
    }

}
package co.danchez.moneymanager.Fragments.Transactions;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import co.danchez.moneymanager.Activities.MainActivity;
import co.danchez.moneymanager.Adapters.TransactionsAdapter;
import co.danchez.moneymanager.Connectivity.FirebaseManager;
import co.danchez.moneymanager.Connectivity.Models.Transaction;
import co.danchez.moneymanager.R;
import co.danchez.moneymanager.Utilidades.ConstantList;
import co.danchez.moneymanager.Utilidades.SharedPreferencesUtil;

import static co.danchez.moneymanager.Utilidades.ConstantList.CREATION_DATE_TRANSACTION;
import static co.danchez.moneymanager.Utilidades.ConstantList.ID_TEAM_TRANSACTION;
import static co.danchez.moneymanager.Utilidades.ConstantList.TRANSACTION_COLLECTION;

public class TransactionsFragment extends Fragment {

    private ListenerRegistration transactionsFragmentListener;
    private FirebaseManager firebaseManager;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private List<Transaction> transactionsList;
    private RecyclerView transactionsRecyclerView;
    private TransactionsAdapter transactionsAdapter;
    private TextView tv_no_transactions;

    public TransactionsFragment() {
        // Required empty public constructor
    }

    public static TransactionsFragment newInstance() {
        TransactionsFragment fragment = new TransactionsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);
        sharedPreferencesUtil = ((MainActivity) getActivity()).getSharedPreferencesUtil();
        firebaseManager = ((MainActivity) getActivity()).getFirebaseManager();
        loadObjects(view);
        return view;
    }

    private void loadObjects(View view) {
        transactionsRecyclerView = view.findViewById(R.id.transactionsRecyclerView);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        tv_no_transactions = view.findViewById(R.id.tv_no_transactions);
        transactionsAdapter = new TransactionsAdapter(getActivity());
        transactionsFragmentListener = firebaseManager.getDb().collection(TRANSACTION_COLLECTION)
                .whereEqualTo(ID_TEAM_TRANSACTION, sharedPreferencesUtil.getIdTeamUser())
                .orderBy(CREATION_DATE_TRANSACTION, Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e("TransactionsListener", String.valueOf(e));
                        return;
                    }
                    transactionsList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Transaction transaction = document.toObject(Transaction.class);
                        transaction.setIdTransaction(document.getId());
                        transactionsList.add(transaction);
                    }
                    transactionsAdapter.setData(transactionsList);
                    transactionsRecyclerView.setAdapter(transactionsAdapter);
                    if (transactionsList.size() == 0) {
                        tv_no_transactions.setVisibility(View.VISIBLE);
                    } else {
                        tv_no_transactions.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onStop() {
        if (transactionsFragmentListener != null)
            transactionsFragmentListener.remove();
        super.onStop();
    }

}
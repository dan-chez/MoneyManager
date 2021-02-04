package co.danchez.moneymanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.danchez.moneymanager.Connectivity.Models.Transaction;
import co.danchez.moneymanager.R;
import co.danchez.moneymanager.Utilidades.Util;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    private List<Transaction> transactionList;
    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView tv_date, tv_buyer, tv_value;
        public final RelativeLayout rl_bg;

        public ViewHolder(View view) {
            super(view);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_buyer = (TextView) view.findViewById(R.id.tv_buyer);
            tv_value = (TextView) view.findViewById(R.id.tv_value);
            rl_bg = (RelativeLayout) view.findViewById(R.id.rl_bg);
        }

    }

    public TransactionsAdapter(Context _context) {
        this.context = _context;
    }

    public void setData(List<Transaction> _transactionList) {
        this.transactionList = _transactionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_transactions_adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Transaction transaction = transactionList.get(position);
        if (transaction != null) {
            viewHolder.tv_date.setText(Util.convertTimestampToString(transaction.getCREATION_DATE_TRANSACTION(), "dd/MM/yyyy"));
            viewHolder.tv_buyer.setText(transaction.getNAME_USER_TRANSACTION());
            viewHolder.tv_value.setText(transaction.getVALUE_TRANSACTION());
            if (transaction.isPAID_TRANSACTION())
                viewHolder.rl_bg.setBackground(context.getResources().getDrawable(R.drawable.bg_transaction_paid));
            else
                viewHolder.rl_bg.setBackground(context.getResources().getDrawable(R.drawable.bg_transaction_not_paid));
        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }
}
package co.danchez.moneymanager.Utilidades;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import co.danchez.moneymanager.R;

public class DialogGeneral extends DialogFragment {

    private int icon;
    private String title, subtitle;
    private boolean isConfirm = false;
    private View.OnClickListener confirmListener, cancelListener;
    private View view;
    private Button btn_confirm_true, btn_confirm_false;

    public static DialogGeneral newInstance() {
        return new DialogGeneral();
    }

    public DialogGeneral setIcon(int _icon) {
        this.icon = _icon;
        return this;
    }

    public DialogGeneral setTitle(String _title) {
        this.title = _title;
        return this;
    }

    public DialogGeneral setSubtitle(String _subtitle) {
        this.subtitle = _subtitle;
        return this;
    }

    public DialogGeneral isConfirm(View.OnClickListener listener) {
        this.isConfirm = true;
        this.confirmListener = listener;
        return this;
    }

    public DialogGeneral setCancelListener(View.OnClickListener listener) {
        this.cancelListener = listener;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_general, container, false);

        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_subtitle = view.findViewById(R.id.tv_subtitle);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        Button btn_close = view.findViewById(R.id.btn_close);
        btn_confirm_true = view.findViewById(R.id.btn_confirm_true);
        btn_confirm_false = view.findViewById(R.id.btn_confirm_false);

        btn_close.setOnClickListener(v -> DialogGeneral.this.dismiss());

        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        if (iv_icon != null) {
            iv_icon.setImageDrawable(getResources().getDrawable(icon));
        }
        manageText(tv_title, title);
        manageText(tv_subtitle, subtitle);
        if (isConfirm) {
            if (this.cancelListener == null) {
                btn_confirm_false.setOnClickListener(v -> DialogGeneral.this.dismiss());
            } else {
                btn_confirm_false.setOnClickListener(cancelListener);
            }
            btn_confirm_true.setOnClickListener(confirmListener);
        }
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    private void manageText(TextView tv, String text) {
        if (text != null) {
            tv.setText(text);
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
    }

}

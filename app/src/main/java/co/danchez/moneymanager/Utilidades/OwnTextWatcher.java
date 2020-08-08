package co.danchez.moneymanager.Utilidades;

import android.content.Context;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.danchez.moneymanager.R;
import co.danchez.moneymanager.Utilidades.Intefaces.TextWatcherInterface;

public class OwnTextWatcher implements android.text.TextWatcher {

    private Context ctx;
    private EditText view;
    private TextWatcherInterface callback;
    private Boolean conError = false;
    private Boolean validaObligatorio = false;
    private Boolean validaEmail = false;
    private int longitudMaxima = -1;
    private int longitudMinima = -1;
    private String regex = "";
    private TextView tvError;
    private String mensajeErrorObligatorio;
    private String mensajeErrorMaxlen;
    private String mensajeErrorMinlen;
    private String mensajeErrorEmail;
    private String mensajeErrorRegex;

    public OwnTextWatcher(Context ctx_, EditText view_, TextWatcherInterface callback_, TextView tvError_) {
        this.ctx = ctx_;
        this.view = view_;
        this.callback = callback_;
        this.tvError = tvError_;
        this.view.addTextChangedListener(this);
    }

    public Boolean getConError() {
        return conError;
    }

    public void setConError(Boolean conError) {
        this.conError = conError;
    }

    public void setErrorMessage(String msgError) {
        view.setBackground(ctx.getResources().getDrawable(R.drawable.background_border_red_fill_empty));
        //Agregar el mensaje de error
        if(tvError != null && !msgError.equals("")){
            tvError.setText(msgError);
            tvError.setVisibility(View.VISIBLE);
        }
    }

    public Boolean getValidaObligatorio() {
        return validaObligatorio;
    }

    public void setValidaObligatorio(Boolean validaObligatorio, String mensajeErrorObligatorio_) {
        this.validaObligatorio = validaObligatorio;
        this.mensajeErrorObligatorio = mensajeErrorObligatorio_;
    }

    public void setValidaEmail(Boolean validaEmail_, String mensajeErrorEmail_) {
        this.validaEmail = validaEmail_;
        this.mensajeErrorEmail = mensajeErrorEmail_;
    }

    public int getLongitudMaxima() {
        return longitudMaxima;
    }

    public void setLongitudMaxima(int longitudMaxima, String mensajeErrorMaxlen_) {
        this.longitudMaxima = longitudMaxima;
        this.mensajeErrorMaxlen = mensajeErrorMaxlen_;
    }

    public int getLongitudMinima() {
        return longitudMinima;
    }

    public void setLongitudMinima(int longitudMinima, String mensajeErrorMinlen_) {
        this.longitudMinima = longitudMinima;
        this.mensajeErrorMinlen = mensajeErrorMinlen_;
    }

    public void setRegex(String regex_, String mensajeErrorRegex_) {
        this.regex = regex_;
        this.mensajeErrorRegex = mensajeErrorRegex_;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        validar(true);
    }

    public void validar(boolean mostrarMensaje){
        conError = false;
        String msgError = "";
        if(validaObligatorio){
            if(view.getText().toString().trim().equals("")){
                conError = true;
                msgError = mensajeErrorObligatorio;
            }
        }
        if(longitudMaxima != -1){
            //Valida la longitud maxima
            if(view.getText().toString().trim().length() > longitudMaxima){
                conError = true;
                msgError = mensajeErrorMaxlen;
            }
        }
        if(longitudMinima != -1){
            //Valida la longitud maxima
            if(validaObligatorio) {
                if (view.getText().toString().trim().length() < longitudMinima) {
                    conError = true;
                    if(view.getText().toString().length()==0)
                        msgError = mensajeErrorObligatorio;

                    else
                        msgError = mensajeErrorMinlen;
                }
            }
            else{
                if (view.getText().toString().trim().length() < longitudMinima && view.getText().toString().length()>0) {
                    conError = true;
                    msgError = mensajeErrorMinlen;
                }
            }

        }

        if(validaEmail){
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            if(!pattern.matcher(view.getText().toString().trim()).matches()) {
                conError = true;
                if(validaObligatorio) {
                    if(view.getText().toString().trim().equals(""))
                        msgError = mensajeErrorObligatorio;
                    else
                        msgError = mensajeErrorEmail;
                }
                else msgError = mensajeErrorEmail;
            }
        }

        if(!regex.equals("")){
            Pattern patternRegex = Pattern.compile(regex);
            Matcher matchRegex = patternRegex.matcher(view.getText().toString());

            if(!matchRegex.matches()) {
                conError = true;
                msgError = mensajeErrorRegex;
            }
        }


        if(mostrarMensaje)
            actualizarEstado(msgError, mostrarMensaje);
    }

    private void actualizarEstado(String msgError, boolean mostrarMensaje){
        if(conError){
            view.setBackground(ctx.getResources().getDrawable(R.drawable.background_border_red_fill_empty));
            //Agregar el mensaje de error
            if(tvError != null && !msgError.equals("")){
                tvError.setText(msgError);
                tvError.setVisibility(View.VISIBLE);
            }
        }else{
            view.setBackground(ctx.getResources().getDrawable(R.drawable.background_border_gray_fill_empty_rounded));
            if(tvError != null){
                tvError.setVisibility(View.GONE);
            }
        }
    }

    public void initEstado(){
        view.setBackground(ctx.getResources().getDrawable(R.drawable.background_border_gray_fill_empty_rounded));
        if(tvError != null){
            tvError.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        callback.onTextChange(view);
    }
}

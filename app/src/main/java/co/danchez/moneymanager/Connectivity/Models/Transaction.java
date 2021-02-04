package co.danchez.moneymanager.Connectivity.Models;

import com.google.firebase.Timestamp;

import co.danchez.moneymanager.Utilidades.Util;

public class Transaction {

    private String idTransaction;
    private String ID_USER_TRANSACTION;
    private String NAME_USER_TRANSACTION;
    private String VALUE_TRANSACTION;
    private String COMMENT_TRANSACTION;
    private Timestamp CREATION_DATE_TRANSACTION;
    private String ID_TEAM_TRANSACTION;
    private boolean PAID_TRANSACTION;

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getID_USER_TRANSACTION() {
        return ID_USER_TRANSACTION;
    }

    public void setID_USER_TRANSACTION(String ID_USER_TRANSACTION) {
        this.ID_USER_TRANSACTION = ID_USER_TRANSACTION;
    }

    public String getNAME_USER_TRANSACTION() {
        return NAME_USER_TRANSACTION;
    }

    public void setNAME_USER_TRANSACTION(String NAME_USER_TRANSACTION) {
        this.NAME_USER_TRANSACTION = NAME_USER_TRANSACTION;
    }

    public String getVALUE_TRANSACTION() {
        return VALUE_TRANSACTION;
    }

    public void setVALUE_TRANSACTION(String VALUE_TRANSACTION) {
        this.VALUE_TRANSACTION = VALUE_TRANSACTION;
    }

    public String getCOMMENT_TRANSACTION() {
        return COMMENT_TRANSACTION;
    }

    public void setCOMMENT_TRANSACTION(String COMMENT_TRANSACTION) {
        this.COMMENT_TRANSACTION = COMMENT_TRANSACTION;
    }

    public Timestamp getCREATION_DATE_TRANSACTION() {
        return CREATION_DATE_TRANSACTION;
    }

    public void setCREATION_DATE_TRANSACTION(Timestamp CREATION_DATE_TRANSACTION) {
        this.CREATION_DATE_TRANSACTION = CREATION_DATE_TRANSACTION;
    }

    public String getID_TEAM_TRANSACTION() {
        return ID_TEAM_TRANSACTION;
    }

    public void setID_TEAM_TRANSACTION(String ID_TEAM_TRANSACTION) {
        this.ID_TEAM_TRANSACTION = ID_TEAM_TRANSACTION;
    }

    public String getCreationDate() {
        return Util.convertTimestampToString(CREATION_DATE_TRANSACTION, null);
    }

    public boolean isPAID_TRANSACTION() {
        return PAID_TRANSACTION;
    }

    public void setPAID_TRANSACTION(boolean PAID_TRANSACTION) {
        this.PAID_TRANSACTION = PAID_TRANSACTION;
    }
}

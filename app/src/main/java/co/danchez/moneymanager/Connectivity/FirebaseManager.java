package co.danchez.moneymanager.Connectivity;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseManager {

    private FirebaseFirestore db;

    public FirebaseManager() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    public void readAllDataFromCollection(OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener, String collectionName, String fieldName, String fieldValue) {
        db.collection(collectionName)
                .whereEqualTo(fieldName, fieldValue)
                .get()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    public void readDataFromCollection(OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener, String collectionName, String fieldName, String valueField) {
        db.collection(collectionName)
                .whereEqualTo(fieldName, valueField)
                .get()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    public void addElement(Map<String, Object> newObject, OnSuccessListener<DocumentReference> onSuccessListener, OnFailureListener onFailureListener, String collectionName) {
        db.collection(collectionName)
                .add(newObject)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void updateElement(Map<String, Object> newObject, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener, String collectionName, String idElement) {
        db.collection(collectionName)
                .document(idElement)
                .update(newObject)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);

    }

    public void deleteElement(OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener, String collectionName, String idElement) {
        db.collection(collectionName)
                .document(idElement)
                .delete()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

}

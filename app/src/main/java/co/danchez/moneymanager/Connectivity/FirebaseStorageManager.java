package co.danchez.moneymanager.Connectivity;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseStorageManager {

    private FirebaseStorage firebaseStorage;

    public FirebaseStorageManager() {
        firebaseStorage = FirebaseStorage.getInstance();
    }

    private void deleteElement(OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener, String idElement) {
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(idElement);
        storageReference
                .delete()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

}

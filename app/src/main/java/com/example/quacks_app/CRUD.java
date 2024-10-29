package com.example.quacks_app;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class CRUD<T extends RepoModel> {
    private Database database;
    private CollectionReference colRef;
    private Class<T> classType;

    public CRUD(Class<T> model) {
        database = new Database();
        colRef = database.getDb().collection(model.getSimpleName());
        classType = model;
    }

    public void create(T model) {
        String id = colRef.document().getId();
        colRef.document(id).set(model);
        model.setId(id);
    }

    public void readStatic(String id, ReadCallback<T> callback) {
        colRef.document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        T model = documentSnapshot.toObject(classType);
                        callback.onDataRead(model);
                    }
                });
    }

    public ListenerRegistration readLive(String id, ReadCallback<T> callback) {
        DocumentReference docRef = colRef.document(id);
        return docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snapshot,
                                FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle error
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    T model = snapshot.toObject(classType);
                    callback.onDataRead(model);
                } else {
                    // Data is null
                }
            }
        });

    }

}

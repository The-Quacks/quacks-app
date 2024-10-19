package com.example.quacks_app;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

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

    public void read(String id, ReadCallback<T> callback) {
        colRef.document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        T model = documentSnapshot.toObject(classType);
                        callback.onDataRead(model);
                    }
                });
    }

}

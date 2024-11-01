package com.example.quacks_app;


import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Map;

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

    public void readAllStatic(ReadAllCallback<T> callback) {
        colRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<T> dataList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                T model = document.toObject(classType);
                                dataList.add(model);
                            }
                            callback.onDataRead(dataList);
                        } else {
                            // Error
                        }
                    }
                });
    }

    public void readQueryStatic(Map<String, String> map, ReadAllCallback<T> callback) {
        Query query = colRef;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            query = query.whereEqualTo(entry.getKey(), entry.getValue());
        }
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<T> dataList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                T model = document.toObject(classType);
                                dataList.add(model);
                            }
                            callback.onDataRead(dataList);
                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public ListenerRegistration readQueryLive(Map<String, String> map, ReadAllCallback<T> callback) {
        Query query = colRef;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            query = query.whereEqualTo(entry.getKey(), entry.getValue());
        }
        return query
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot value,
                                        FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        ArrayList<T> dataList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            T model = document.toObject(classType);
                            dataList.add(model);
                        }
                        callback.onDataRead(dataList);
                    }
                });
    }

    public ListenerRegistration readAllLive(ReadAllCallback<T> callback) {
        return colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value,
                                FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                ArrayList<T> dataList = new ArrayList<>();
                for (QueryDocumentSnapshot document : value) {
                    T model = document.toObject(classType);
                    dataList.add(model);
                }
                callback.onDataRead(dataList);
            }
        });
    }

    // Update method
    public void update(String id, T model, UpdateCallback callback) {
        colRef.document(id).set(model, SetOptions.merge())
                .addOnSuccessListener(aVoid -> callback.onUpdateSuccess())
                .addOnFailureListener(callback::onUpdateFailure);
    }

    // Delete method
    public void delete(String id, DeleteCallback callback) {
        colRef.document(id).delete()
                .addOnSuccessListener(aVoid -> callback.onDeleteSuccess())
                .addOnFailureListener(callback::onDeleteFailure);
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
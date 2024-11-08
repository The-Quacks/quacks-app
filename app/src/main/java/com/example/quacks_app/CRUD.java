package com.example.quacks_app;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Map;

/**
 * The {@code CRUD} class provides functionality for interfacing with the firestore
 * database. It allows creating, reading, updating, and deleting models extending the
 * {@code RepoModel} class.
 *
 * @param <T> the type of elements used for CRUD operations
 */
public class CRUD<T extends RepoModel> {

    private final CollectionReference colRef;
    private final Class<T> classType;

    /**
     * Constructs a new {@code CRUD} class of the specified model type.
     *
     * @param model the class type of the elements used for CRUD operations
     */
    public CRUD(Class<T> model) {

        colRef = FirebaseFirestore.getInstance().collection(model.getSimpleName());
        classType = model;
    }

    /**
     * Creates an instance of a data model in the database
     *
     * @param model the model to be created
     * @param callback the callback instance that contains the logic for
     *                 success and failure of the data creation
     */
    public void create(T model, CreateCallback callback) {
        String id = colRef.document().getId();
        colRef.document(id).set(model)
                .addOnSuccessListener(aVoid -> callback.onCreateSuccess())
                .addOnFailureListener(callback::onCreateFailure);
        model.setId(id);
    }

    /**
     * Reads a static instance of a data model from the database
     *
     * @param id id of the model to be read
     * @param callback the callback instance that contains the logic for
     *                 success and failure of the data reading
     */
    public void readStatic(String id, ReadCallback<T> callback) {
        colRef.document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    T model = documentSnapshot.toObject(classType);
                    callback.onReadSuccess(model);
                })
                .addOnFailureListener(callback::onReadFailure);
    }

    /**
     * Reads a real-time instance of a data model from the database
     *
     * @param id id of the model to be read
     * @param callback the callback instance that contains the logic for
     *                 success and failure of the data reading
     * @return a {@code ListenerRegistration} that contains a listener that
     *         listens for changes in the data
     */
    public ListenerRegistration readLive(String id, ReadCallback<T> callback) {
        DocumentReference docRef = colRef.document(id);
        return docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                callback.onReadFailure(e);
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                T model = snapshot.toObject(classType);
                callback.onReadSuccess(model);
            } else {
                callback.onReadFailure(new Exception("Null data"));
            }
        });
    }

    /**
     * Reads all the data within a collection from the database statically
     *
     * @param callback the callback instance that contains the logic for
     *                 success and failure of the data reading
     */
    public void readAllStatic(ReadMultipleCallback<T> callback) {
        colRef.get()
                .addOnCompleteListener(task -> {
                    ArrayList<T> dataList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        T model = document.toObject(classType);
                        dataList.add(model);
                    }
                    callback.onReadMultipleSuccess(dataList);
                })
                .addOnFailureListener(callback::onReadMultipleFailure);
    }

    /**
     * Reads all the data within a collection from the database in real-time
     *
     * @param callback the callback instance that contains the logic for
     *                 success and failure of the data reading
     * @return a {@code ListenerRegistration} that contains a listener that
     *         listens for changes in the data
     */
    public ListenerRegistration readAllLive(ReadMultipleCallback<T> callback) {
        return colRef.addSnapshotListener((value, e) -> {
            if (e != null) {
                callback.onReadMultipleFailure(e);
            }

            if (value != null) {
                ArrayList<T> dataList = new ArrayList<>();
                for (QueryDocumentSnapshot document : value) {
                    T model = document.toObject(classType);
                    dataList.add(model);
                }
                callback.onReadMultipleSuccess(dataList);
            }
            else {
                callback.onReadMultipleFailure(new Exception("Null data"));
            }
        });
    }

    /**
     * Reads all the data within a collection that conforms to a query statically
     *
     * @param fields a mapping where the key is the field and the value is the value the field
     *              of the object searched for should be
     * @param callback the callback instance that contains the logic for
     *                 success and failure of the data reading
     */
    public void readQueryStatic(Map<String, String> fields, ReadMultipleCallback<T> callback) {
        Query query = colRef;
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            query = query.whereEqualTo(entry.getKey(), entry.getValue());
        }
        query.get()
                .addOnCompleteListener(task -> {
                    ArrayList<T> dataList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        T model = document.toObject(classType);
                        dataList.add(model);
                    }
                    callback.onReadMultipleSuccess(dataList);
                })
                .addOnFailureListener(callback::onReadMultipleFailure);
    }

    /**
     * Reads all the data within a collection that conforms to a query in real-time
     *
     * @param fields a mapping where the key is the field and the value is the value the field
     *              of the object searched for should be
     * @param callback the callback instance that contains the logic for
     *                 success and failure of the data reading
     * @return a {@code ListenerRegistration} that contains a listener that
     *         listens for changes in the data
     */
    public ListenerRegistration readQueryLive(Map<String, String> fields, ReadMultipleCallback<T> callback) {
        Query query = colRef;
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            query = query.whereEqualTo(entry.getKey(), entry.getValue());
        }
        return query
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        callback.onReadMultipleFailure(e);
                    }
                    if (value != null) {
                        ArrayList<T> dataList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            T model = document.toObject(classType);
                            dataList.add(model);
                        }
                        callback.onReadMultipleSuccess(dataList);
                    }
                    else {
                        callback.onReadMultipleFailure(new Exception("Null data"));
                    }
                });
    }

    /**
     * Updates a data model in the database
     *
     * @param id id of the model to be updated
     * @param model the model used to update the model in the database
     * @param callback the callback instance that contains the logic for
     *                 success and failure of updating the data
     */

    public void update(String id, T model, UpdateCallback callback) {
        colRef.document(id).set(model, SetOptions.merge())
                .addOnSuccessListener(aVoid -> callback.onUpdateSuccess())
                .addOnFailureListener(callback::onUpdateFailure);
    }

    /**
     * Deletes a data model in the database
     *
     * @param id id of the model to be updated
     * @param callback the callback instance that contains the logic for
     *                 success and failure of deleting the data
     */

    public void delete(String id, DeleteCallback callback) {
        colRef.document(id).delete()
                .addOnSuccessListener(aVoid -> callback.onDeleteSuccess())
                .addOnFailureListener(callback::onDeleteFailure);
    }
}
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
 * The {@code CRUD} class provides static methods for interfacing with the Firestore
 * database. It allows creating, reading, updating, and deleting models extending the
 * {@code RepoModel} class.
 */
public class CRUD {

    /**
     * Creates an instance of a data model in the database.
     *
     * @param model    the model to be created
     * @param callback the callback instance that contains the logic for
     *                 success and failure of the data creation
     */
    public static <T extends RepoModel> void create(T model, CreateCallback callback) {
        CollectionReference colRef = FirebaseFirestore.getInstance().collection(model.getClass().getSimpleName());
        String id = colRef.document().getId();
        colRef.document(id).set(model)
                .addOnSuccessListener(aVoid -> callback.onCreateSuccess())
                .addOnFailureListener(callback::onCreateFailure);
        model.setId(id);
    }

    /**
     * Reads a static instance of a data model from the database.
     *
     * @param id        id of the model to be read
     * @param classType the class type of the model
     * @param callback  the callback instance that contains the logic for
     *                  success and failure of the data reading
     */
    public static <T extends RepoModel> void readStatic(String id, Class<T> classType, ReadCallback<T> callback) {
        CollectionReference colRef = FirebaseFirestore.getInstance().collection(classType.getSimpleName());
        colRef.document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    T model = documentSnapshot.toObject(classType);
                    callback.onReadSuccess(model);
                })
                .addOnFailureListener(callback::onReadFailure);
    }

    /**
     * Reads a real-time instance of a data model from the database.
     *
     * @param id        id of the model to be read
     * @param classType the class type of the model
     * @param callback  the callback instance that contains the logic for
     *                  success and failure of the data reading
     * @return a {@code ListenerRegistration} that contains a listener that
     *         listens for changes in the data
     */
    public static <T extends RepoModel> ListenerRegistration readLive(String id, Class<T> classType, ReadCallback<T> callback) {
        CollectionReference colRef = FirebaseFirestore.getInstance().collection(classType.getSimpleName());
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
     * Reads all the data within a collection from the database statically.
     *
     * @param classType the class type of the model
     * @param callback  the callback instance that contains the logic for
     *                  success and failure of the data reading
     */
    public static <T extends RepoModel> void readAllStatic(Class<T> classType, ReadMultipleCallback<T> callback) {
        CollectionReference colRef = FirebaseFirestore.getInstance().collection(classType.getSimpleName());
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
     * Reads all the data within a collection from the database in real-time.
     *
     * @param classType the class type of the model
     * @param callback  the callback instance that contains the logic for
     *                  success and failure of the data reading
     * @return a {@code ListenerRegistration} that contains a listener that
     *         listens for changes in the data
     */
    public static <T extends RepoModel> ListenerRegistration readAllLive(Class<T> classType, ReadMultipleCallback<T> callback) {
        CollectionReference colRef = FirebaseFirestore.getInstance().collection(classType.getSimpleName());
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
            } else {
                callback.onReadMultipleFailure(new Exception("Null data"));
            }
        });
    }

    /**
     * Reads all the data within a collection that conforms to a query statically.
     *
     * @param fields    a mapping where the key is the field and the value is the value the field
     *                  of the object searched for should be
     * @param classType the class type of the model
     * @param callback  the callback instance that contains the logic for
     *                  success and failure of the data reading
     */
    public static <T extends RepoModel> void readQueryStatic(Map<String, String> fields, Class<T> classType, ReadMultipleCallback<T> callback) {
        CollectionReference colRef = FirebaseFirestore.getInstance().collection(classType.getSimpleName());
        Query query = colRef;
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            query = query.whereEqualTo(entry.getKey(), entry.getValue());
        }
        query.get()
                .addOnCompleteListener(task -> {
                    ArrayList<T> dataList = new ArrayList<>();
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            T model = document.toObject(classType);
                            dataList.add(model);
                        }
                        callback.onReadMultipleSuccess(dataList);
                    } else {
                        callback.onReadMultipleFailure(task.getException());
                    }
                })
                .addOnFailureListener(callback::onReadMultipleFailure);
    }

    /**
     * Reads all the data within a collection that conforms to a query in real-time.
     *
     * @param fields    a mapping where the key is the field and the value is the value the field
     *                  of the object searched for should be
     * @param classType the class type of the model
     * @param callback  the callback instance that contains the logic for
     *                  success and failure of the data reading
     * @return a {@code ListenerRegistration} that contains a listener that
     *         listens for changes in the data
     */
    public static <T extends RepoModel> ListenerRegistration readQueryLive(Map<String, String> fields, Class<T> classType, ReadMultipleCallback<T> callback) {
        CollectionReference colRef = FirebaseFirestore.getInstance().collection(classType.getSimpleName());
        Query query = colRef;
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            query = query.whereEqualTo(entry.getKey(), entry.getValue());
        }
        return query.addSnapshotListener((value, e) -> {
            if (e != null) {
                callback.onReadMultipleFailure(e);
                return;
            }

            if (value != null) {
                ArrayList<T> dataList = new ArrayList<>();
                for (QueryDocumentSnapshot document : value) {
                    T model = document.toObject(classType);
                    dataList.add(model);
                }
                callback.onReadMultipleSuccess(dataList);
            } else {
                callback.onReadMultipleFailure(new Exception("Null data"));
            }
        });
    }

    /**
     * Updates a data model in the database.
     *
     * @param id        id of the model to be updated
     * @param model     the model used to update the model in the database
     * @param callback  the callback instance that contains the logic for
     *                  success and failure of updating the data
     */
    public static <T extends RepoModel> void update(String id, T model, UpdateCallback callback) {
        CollectionReference colRef = FirebaseFirestore.getInstance().collection(model.getClass().getSimpleName());
        colRef.document(id).set(model, SetOptions.merge())
                .addOnSuccessListener(aVoid -> callback.onUpdateSuccess())
                .addOnFailureListener(callback::onUpdateFailure);
    }

    /**
     * Deletes a data model in the database.
     *
     * @param id       id of the model to be deleted
     * @param classType the class type of the model
     * @param callback the callback instance that contains the logic for
     *                 success and failure of deleting the data
     */
    public static <T extends RepoModel> void delete(String id, Class<T> classType, DeleteCallback callback) {
        CollectionReference colRef = FirebaseFirestore.getInstance().collection(classType.getSimpleName());
        colRef.document(id).delete()
                .addOnSuccessListener(aVoid -> callback.onDeleteSuccess())
                .addOnFailureListener(callback::onDeleteFailure);
    }
}
package com.example.quacks_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.RequiresPermission;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * The {@code CRUD} class provides static methods for interfacing with the Firestore
 * database. It allows creating, reading, updating, and deleting models extending the
 * {@code RepoModel} class.
 */
public class CRUD {

    // Gets the firebase instance. Returns default instance in normal use and
    // a test instance in testing
    private static FirebaseFirestore getInstance() {
        if ("true".equals(System.getProperty("junit.test"))) {
            return FirebaseFirestore.getInstance("testquacksdb");
        }
        else {
            return FirebaseFirestore.getInstance();
        }
    }

    /**
     * Creates an instance of a data model in the database.
     *
     * @param model    the model to be created
     * @param callback the callback instance that contains the logic for
     *                 success and failure of the data creation
     */
    public static <T extends RepoModel> void create(T model, CreateCallback callback) {
        CollectionReference colRef = getInstance().collection(model.getClass().getSimpleName());
        String id = colRef.document().getId();
        colRef.document(id).set(model)
                .addOnSuccessListener(aVoid -> callback.onCreateSuccess())
                .addOnFailureListener(callback::onCreateFailure);
        model.setDocumentId(id);
    }

    /**
     * Updates a data model in the database. If the model does not exist,
     * it is created.
     *
     * @param model     the model used to create/update the model in the database
     * @param callback  the callback instance that contains the logic for
     *                  success and failure of creating/updating the data
     */
    public static <T extends RepoModel> void createOrUpdate(T model, UpdateCallback callback) {
        CollectionReference colRef = getInstance().collection(model.getClass().getSimpleName());
        if (model.getDocumentId() == null) {
            model.setDocumentId(colRef.document().getId());
        }
        colRef.document(model.getDocumentId()).set(model, SetOptions.merge())
                .addOnSuccessListener(aVoid -> callback.onUpdateSuccess())
                .addOnFailureListener(callback::onUpdateFailure);
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
        CollectionReference colRef = getInstance().collection(classType.getSimpleName());
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
        CollectionReference colRef = getInstance().collection(classType.getSimpleName());
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
        CollectionReference colRef = getInstance().collection(classType.getSimpleName());
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
        CollectionReference colRef = getInstance().collection(classType.getSimpleName());
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
    public static <T extends RepoModel> void readQueryStatic(Map<String, Object> fields, Class<T> classType, ReadMultipleCallback<T> callback) {
        CollectionReference colRef = getInstance().collection(classType.getSimpleName());
        Query query = colRef;
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
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
    public static <T extends RepoModel> ListenerRegistration readQueryLive(Map<String, Object> fields, Class<T> classType, ReadMultipleCallback<T> callback) {
        CollectionReference colRef = getInstance().collection(classType.getSimpleName());
        Query query = colRef;
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
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
     * @param model     the model used to update the model in the database
     * @param callback  the callback instance that contains the logic for
     *                  success and failure of updating the data
     */
    public static <T extends RepoModel> void update(T model, UpdateCallback callback) {
        CollectionReference colRef = getInstance().collection(model.getClass().getSimpleName());
        colRef.document(model.getDocumentId()).set(model, SetOptions.merge())
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
        CollectionReference colRef = getInstance().collection(classType.getSimpleName());
        colRef.document(id).delete()
                .addOnSuccessListener(aVoid -> callback.onDeleteSuccess())
                .addOnFailureListener(callback::onDeleteFailure);
    }

    /**
     * Stores an image using Firebase Cloud Storage.
     *
     * @param image    the image to be stored
     * @param callback the callback instance that contains the logic for
     *                 success and failure of the image storage. The success
     *                 callback is passed the firestore path of the stored image
     */
    public static void storeImage(Bitmap image, ReadCallback<String> callback) {
        StorageReference storageRef =  FirebaseStorage.getInstance().getReference();
        String uniquePath = "images/" + UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storageRef.child(uniquePath);

        CRUD.readLive("yYc9ikIhSODAcZ4180Hi", ImageList.class, new ReadCallback<ImageList>(){
            @Override
            public void onReadSuccess(ImageList data) {
                data.addImage(uniquePath);
            }

            @Override
            public void onReadFailure(Exception e) {

            }
        });

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask
                .addOnFailureListener(callback::onReadFailure)
                .addOnSuccessListener(taskSnapshot -> callback.onReadSuccess(uniquePath));

    }

    /**
     * Stores an image using Firebase Cloud Storage.
     *
     * @param uri       the uri of the image to be stored
     * @param callback the callback instance that contains the logic for
     *                 success and failure of the image storage. The success
     *                 callback is passed the firestore path of the stored image
     */
    public static void storeImage(Uri uri, ReadCallback<String> callback) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String uniquePath = "images/" + UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storageRef.child(uniquePath);
        CRUD.readLive("yYc9ikIhSODAcZ4180Hi", ImageList.class, new ReadCallback<ImageList>(){
            @Override
            public void onReadSuccess(ImageList data) {
                data.addImage(uniquePath);
            }

            @Override
            public void onReadFailure(Exception e) {

            }
        });


        UploadTask uploadTask = imageRef.putFile(uri);
        uploadTask
                .addOnFailureListener(callback::onReadFailure)
                .addOnSuccessListener(taskSnapshot -> callback.onReadSuccess(uniquePath));
    }

    /**
     * Downloads an image from Firebase Cloud Storage.
     *
     * @param imagePath the image path in Firebase Cloud Storage
     * @param callback the callback instance that contains the logic for
     *                 success and failure of the image downloading. The success
     *                 callback is passed bitmap downloaded
     */
    public static void downloadImage(String imagePath, ReadCallback<Bitmap> callback) {
        StorageReference storageRef =  FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child(imagePath);

        // 4 MB limit
        long sizeLimit = (long) Math.pow(2, 22);
        imageRef.getBytes(sizeLimit).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            callback.onReadSuccess(bitmap);
        }).addOnFailureListener(callback::onReadFailure);

    }

    /**
     * Removes an image from Firebase Cloud Storage.
     *
     * @param imagePath the image path in Firebase Cloud Storage
     * @param callback the callback instance that contains the logic for
     *                 success and failure of the image deletion
     */
    public static void removeImage(String imagePath, DeleteCallback callback) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child(imagePath);
        CRUD.readLive("yYc9ikIhSODAcZ4180Hi", ImageList.class, new ReadCallback<ImageList>(){
            @Override
            public void onReadSuccess(ImageList data) {
                data.removeImage(imagePath);
                CRUD.update(data, new UpdateCallback() {
                    @Override
                    public void onUpdateSuccess() {

                    }

                    @Override
                    public void onUpdateFailure(Exception e) {

                    }
                });
            }

            @Override
            public void onReadFailure(Exception e) {

            }
        });

        imageRef.delete()
                .addOnSuccessListener(aVoid -> callback.onDeleteSuccess())
                .addOnFailureListener(callback::onDeleteFailure);
    }

    public static <T extends RepoModel> void clearColection(Class<T> classType, DeleteCallback callback) {
        CollectionReference colRef = getInstance().collection(classType.getSimpleName());
        colRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<T> documents = task.getResult().toObjects(classType);
                        int totalDocuments = documents.size();
                        AtomicInteger deletedDocuments = new AtomicInteger(0);

                        for (T document : documents) {
                            colRef.document(document.getDocumentId()).delete()
                                    .addOnSuccessListener(aVoid -> {
                                        deletedDocuments.incrementAndGet();

                                        if (deletedDocuments.get() == totalDocuments) {
                                            callback.onDeleteSuccess();
                                        }
                                    })
                                    .addOnFailureListener(callback::onDeleteFailure);
                        }

                        if (totalDocuments == 0) {
                            callback.onDeleteSuccess();
                        }

                    } else {
                        callback.onDeleteFailure(task.getException());
                    }
                });
    }
}
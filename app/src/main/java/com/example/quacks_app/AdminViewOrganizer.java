package com.example.quacks_app;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * The {@code AdminViewOrganizer} class represents the screen for viewing, editing, and deleting
 * various types of data in the Quacks app. It supports handling Events, Users, and Facilities,
 * providing an interface for admins to manage these entities.
 */
public class AdminViewOrganizer extends AppCompatActivity {
    String title;
    String fieldOne;
    String fieldTwo;
    String fieldThree;
    String fieldFour;

    /**
     * Deletes a facility and all related data such as associated events and their applicant lists.
     *
     * @param realFacility the facility to delete
     * @param delCall      the callback to handle success or failure of the delete operation
     */
    public void deleteFacility(Facility realFacility, DeleteCallback delCall){

        CRUD.readAllLive(Event.class, new ReadMultipleCallback<Event>() {
            @Override
            public void onReadMultipleSuccess(ArrayList<Event> eventDataList) {
                for (Event eventData : eventDataList){
                    if (eventData.getFacility().equals(realFacility.getDocumentId())){
                        String appList = eventData.getApplicantList();
                        if (eventData.getEventPosterPath() != null){
                            CRUD.removeImage(eventData.getEventPosterPath(), delCall);
                        }
                        if (eventData.getQrCodePath() != null){
                            CRUD.removeImage(eventData.getQrCodePath(), delCall);
                        }

                        CRUD.delete(appList, ApplicantList.class, delCall);
                        CRUD.delete(eventData.getDocumentId(), Event.class, delCall);
                        CRUD.delete(realFacility.getDocumentId(), Facility.class, delCall);
                    }

                }
            }

            @Override
            public void onReadMultipleFailure(Exception e) {
                CRUD.delete(realFacility.getDocumentId(), Facility.class, delCall);

            }
        });
    }

    /**
     * Initializes the activity. Sets up UI components and displays details of the selected entity
     * (Event, Facility, or User). Also enables delete functionality.
     *
     * @param savedInstanceState {@code null} if the activity is being created for the first time.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_admin);
        EdgeToEdge.enable(this);

        Bundle bundle = getIntent().getExtras();
        Listable editEvent = (Listable) bundle.getSerializable("value");
        String id = bundle.getString("id");

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Button delete = findViewById(R.id.delete_button);

        delete.setOnClickListener(view -> {
            DeleteCallback delCall = new DeleteCallback(){
                @Override
                public void onDeleteSuccess() {
                    System.out.println("Document successfully deleted!");
                    Toast.makeText(AdminViewOrganizer.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onDeleteFailure(Exception e) {
                    System.err.println("Error deleting document: " + e.getMessage());
                }
            };
            if (editEvent instanceof Event){
                Event realEvent = (Event) editEvent;
                if (realEvent.getEventPosterPath() != null){
                    CRUD.removeImage(realEvent.getEventPosterPath(), delCall);
                }
                if (realEvent.getQrCodePath() != null){
                    CRUD.removeImage(realEvent.getQrCodePath(), delCall);
                }
                CRUD.delete(id, Event.class, delCall);
                CRUD.delete(realEvent.getApplicantList(), ApplicantList.class, delCall);
            }
            else if (editEvent instanceof User){
                User realUser = (User) editEvent;
                if (realUser.getRoles().contains(Role.ENTRANT)){
                    CRUD.readAllLive(ApplicantList.class, new ReadMultipleCallback<ApplicantList>() {
                        @Override
                        public void onReadMultipleSuccess(ArrayList<ApplicantList> appData) {
                            for (ApplicantList app : appData){
                                if (app.getApplicantIds().contains(realUser.getDocumentId())){
                                    app.getApplicantIds().remove(realUser.getDocumentId());
                                    CRUD.update(app, new UpdateCallback() {
                                        @Override
                                        public void onUpdateSuccess() {
                                            Toast.makeText(AdminViewOrganizer.this, "ApplicantList Updated!", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onUpdateFailure(Exception e) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onReadMultipleFailure(Exception e) {

                        }
                    });
                    CRUD.readAllLive(Notification.class, new ReadMultipleCallback<Notification>() {
                        @Override
                        public void onReadMultipleSuccess(ArrayList<Notification> data) {
                            for (Notification notif : data){
                                if (notif.getUser().getDeviceId().equals(realUser.getDeviceId())){
                                    CRUD.delete(notif.getDocumentId(), Notification.class, new DeleteCallback() {
                                        @Override
                                        public void onDeleteSuccess() {

                                        }

                                        @Override
                                        public void onDeleteFailure(Exception e) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onReadMultipleFailure(Exception e) {

                        }
                    });


                }
                if (realUser.getRoles().contains(Role.ORGANIZER)){
                    CRUD.readAllLive(Facility.class, new ReadMultipleCallback<Facility>() {
                        @Override
                        public void onReadMultipleSuccess(ArrayList<Facility> facData) {
                            for (Facility fac : facData) {
                                if (fac.getOrganizerId().equals(realUser.getDocumentId())) {
                                    deleteFacility(fac, delCall);
                                }
                            }
                        }
                        @Override
                        public void onReadMultipleFailure(Exception e) {
                            CRUD.delete(realUser.getDocumentId(), User.class, delCall);
                        }
                    });
                }
                if (realUser.getUserProfile().getProfilePicturePath() != null){
                    CRUD.removeImage(realUser.getUserProfile().getProfilePicturePath(), delCall);
                }

                CRUD.delete(realUser.getDocumentId(), User.class, delCall);
            }

            else if (editEvent instanceof Facility) {
                Facility realFacility = (Facility) editEvent;
                deleteFacility(realFacility, delCall);

            }
        });



        if (editEvent instanceof Event){
            Event specEvent = (Event) editEvent;
            CRUD.readLive(specEvent.getApplicantList(), ApplicantList.class, new ReadCallback<ApplicantList>() {
                @Override
                public void onReadSuccess(ApplicantList appData) {
                    CRUD.readLive(specEvent.getFacility(), Facility.class, new ReadCallback<Facility>() {
                        @Override
                        public void onReadSuccess(Facility facData) {
                            if (specEvent.getEventPosterPath() != null){
                                CRUD.downloadImage(specEvent.getEventPosterPath(), new ReadCallback<Bitmap>() {
                                    @Override
                                    public void onReadSuccess(Bitmap data) {
                                        ImageView poster = findViewById(R.id.prof_pic);
                                        poster.setImageBitmap(data);
                                    }

                                    @Override
                                    public void onReadFailure(Exception e) {

                                    }
                                });
                            }
                            title = "View Event";

                            fieldOne = "Name: " + specEvent.getDisplay();
                            fieldTwo = "Capacity: "+ appData.getLimit();
                            fieldThree = "Availability: " + (appData.getLimit() - appData.getApplicantIds().size());
                            fieldFour = "Location: " + facData.getGeoPointString(AdminViewOrganizer.this);

                            TextView titleText = findViewById(R.id.title);
                            EditText name = findViewById(R.id.Name);
                            EditText capacity = findViewById(R.id.capacity);
                            EditText available = findViewById(R.id.availability);
                            EditText location = findViewById(R.id.location);

                            titleText.setText(title);
                            name.setText(fieldOne);
                            capacity.setText(fieldTwo);
                            available.setText(fieldThree);
                            location.setText(fieldFour);

                        }
                        @Override
                        public void onReadFailure(Exception e) {

                        }
                    });

                }
                @Override
                public void onReadFailure(Exception e) {

                }
            });
        }

        else if (editEvent instanceof Facility){
            Facility specFacility = (Facility) editEvent;
            title = "View Facility";
            fieldOne = "Name: " + specFacility.getName();
            fieldTwo = "Contact Info: "+  specFacility.getPhone();
            fieldThree = "Location: " + specFacility.getGeoPointString(AdminViewOrganizer.this);
            CRUD.readLive(specFacility.getOrganizerId(), User.class, new ReadCallback<User>() {
                @Override
                public void onReadSuccess(User specUser) {
                    fieldFour = "Organizer: " + specUser.getUserProfile().getUserName();

                    TextView titleText = findViewById(R.id.title);
                    EditText name = findViewById(R.id.Name);
                    EditText capacity = findViewById(R.id.capacity);
                    EditText available = findViewById(R.id.availability);
                    EditText location = findViewById(R.id.location);

                    titleText.setText(title);
                    name.setText(fieldOne);
                    capacity.setText(fieldTwo);
                    available.setText(fieldThree);
                    location.setText(fieldFour);
                }

                @Override
                public void onReadFailure(Exception e) {

                }
            });
        }
        else if (editEvent instanceof User){
            User specUser = (User) editEvent;
            if (specUser.getUserProfile().getProfilePicturePath() != null){
                CRUD.downloadImage(specUser.getUserProfile().getProfilePicturePath(), new ReadCallback<Bitmap>() {
                    @Override
                    public void onReadSuccess(Bitmap data) {
                        ImageView profPic = findViewById(R.id.prof_pic);
                        profPic.setImageBitmap(data);
                    }

                    @Override
                    public void onReadFailure(Exception e) {
                        Toast.makeText(AdminViewOrganizer.this, "Image Does Not Exist", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            title = "View User";
            fieldOne = "Name: " + specUser.getUserProfile().getUserName();
            fieldTwo = "Phone Number: " + specUser.getUserProfile().getPhoneNumber();
            fieldThree = "Email: " + specUser.getUserProfile().getEmail();
            fieldFour = "Roles: " + specUser.getSubDisplay();

            TextView titleText = findViewById(R.id.title);
            EditText name = findViewById(R.id.Name);
            EditText capacity = findViewById(R.id.capacity);
            EditText available = findViewById(R.id.availability);
            EditText location = findViewById(R.id.location);

            titleText.setText(title);
            name.setText(fieldOne);
            capacity.setText(fieldTwo);
            available.setText(fieldThree);
            location.setText(fieldFour);
        }
    }
}

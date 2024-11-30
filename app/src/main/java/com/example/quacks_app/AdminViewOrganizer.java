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

/*
This is the individual edit and delete screen to view events, can be multipurposed for not just admin
 */

public class AdminViewOrganizer extends AppCompatActivity {
    String title;
    String fieldOne;
    String fieldTwo;
    String fieldThree;
    String fieldFour;

    public void deleteFacility(Facility realFacility, DeleteCallback delCall){
        CRUD.readLive(realFacility.getEventListId(), EventList.class, new ReadCallback<EventList>() {
            @Override
            public void onReadSuccess(EventList eventListData) {
                ArrayList<String> evIds = eventListData.getEventIds();
                for (String event : evIds){
                    CRUD.readLive(event, Event.class, new ReadCallback<Event>() {
                        @Override
                        public void onReadSuccess(Event eventData) {
                            String appList = eventData.getApplicantList();
                            if (eventData.getPosterId() != null){
                                CRUD.removeImage(eventData.getPosterId(), delCall);
                            }
                            CRUD.delete(appList, ApplicantList.class, delCall);
                            CRUD.delete(eventData.getDocumentId(), Event.class, delCall);
                            CRUD.delete(realFacility.getEventListId(), EventList.class, delCall);
                            CRUD.delete(realFacility.getDocumentId(), Facility.class, delCall);
                        }

                        @Override
                        public void onReadFailure(Exception e) {
                            CRUD.delete(realFacility.getEventListId(), EventList.class, delCall);
                            CRUD.delete(realFacility.getDocumentId(), Facility.class, delCall);

                        }
                    });
                }


            }

            @Override
            public void onReadFailure(Exception e) {
                CRUD.delete(realFacility.getDocumentId(), Facility.class, delCall);

            }
        });

    }



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
                if (realEvent.getPosterId() != null){
                    CRUD.removeImage(realEvent.getPosterId(), delCall);
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

                }
                if (realUser.getRoles().contains(Role.ORGANIZER)){
                    CRUD.readAllLive(Facility.class, new ReadMultipleCallback<Facility>() {
                        @Override
                        public void onReadMultipleSuccess(ArrayList<Facility> facData) {
                            for (Facility fac : facData) {
                                if (fac.getOrganizerId() == realUser.getDocumentId()) {
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
                            if (specEvent.getPosterId() != null){
                                CRUD.downloadImage(specEvent.getPosterId(), new ReadCallback<Bitmap>() {
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

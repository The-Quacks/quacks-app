package com.example.quacks_app;

import java.io.Serializable;
import java.util.ArrayList;

public class EventList extends RepoModel implements Serializable {
    private ArrayList<String> eventIds;


    public EventList() {
        eventIds = new ArrayList<>();
    }

    public ArrayList<String> getEventIds() {
        return eventIds;
    }

    public ArrayList<Event> getEvent(ReadCallback readCallback) {
        return new ArrayList<>(); // placeholder
    }

    public void setEventIds(ArrayList<String> eventIds) {
        this.eventIds = eventIds;
    }

    public void addEvent(Event event) {
        this.eventIds.add(event.getId());
    }

    public void addEvent(String eventId) {
        this.eventIds.add(eventId);
    }

    public void removeEvent(Event event) {
        this.eventIds.remove(event.getId());
    }

    public void removeEvent(String eventId) {
        this.eventIds.remove(eventId);
    }

    public void setEvents(ArrayList<Event> events) {
        eventIds = new ArrayList<>();
        for (Event event : events) {
            eventIds.add(event.getId());
        }
    }

}

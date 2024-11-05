package com.example.quacks_app;

import java.util.ArrayList;

public class EventList {
    private ArrayList<String> eventIds;

    public EventList() {}

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
        this.eventIds.add(event.getOrganizerId());
    }

    public void addEvent(String eventId) {
        this.eventIds.add(eventId);
    }

    public void removeEvent(Event event) {
        this.eventIds.remove(event.getOrganizerId());
    }

    public void removeEvent(String eventId) {
        this.eventIds.remove(eventId);
    }

    public void setEvents(ArrayList<Event> events) {
        eventIds = new ArrayList<>();
        for (Event event : events) {
            eventIds.add(event.getOrganizerId());
        }
    }
}

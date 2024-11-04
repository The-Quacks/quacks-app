package com.example.quacks_app;

import java.util.ArrayList;

/**
 * Represents a list of events with functionality to manage and retrieve event information.
 */
public class EventList extends RepoModel {
    private ArrayList<String> eventIds;

    /**
     * Constructs a new EventList.
     */
    public EventList() {this.eventIds = new ArrayList<>();}

    /**
     * Retrieves the list of event IDs.
     *
     * @return An {@code ArrayList} of {@code String} representing event IDs.
     */
    public ArrayList<String> getEventIds() {
        return eventIds;
    }

    /**
     * Retrieves the list of {@code Event} objects.
     *
     * @param readCallback A callback to handle read operations.
     * @return An {@code ArrayList} of {@code Event} objects representing the events.
     */
    public ArrayList<Event> getEvent(ReadCallback readCallback) {
        return new ArrayList<>(); // placeholder
    }

    /**
     * Sets the list of event IDs.
     *
     * @param eventIds An {@code ArrayList} of {@code String} representing event IDs to be set.
     */
    public void setEventIds(ArrayList<String> eventIds) {
        this.eventIds = eventIds;
    }

    /**
     * Adds an {@code Event} to the list by its ID.
     *
     * @param event The {@code Event} object to be added.
     */
    public void addEvent(Event event) {
        this.eventIds.add(event.getId());
    }

    /**
     * Adds an event ID to the list of event IDs.
     *
     * @param eventId The {@code String} representing the event ID to be added.
     */
    public void addEvent(String eventId) {
        this.eventIds.add(eventId);
    }

    /**
     * Removes an {@code Event} from the list by its ID.
     *
     * @param event The {@code Event} object to be removed.
     */
    public void removeEvent(Event event) {
        this.eventIds.remove(event.getId());
    }

    /**
     * Removes an event ID from the list of event IDs.
     *
     * @param eventId The {@code String} representing the event ID to be removed.
     */
    public void removeEvent(String eventId) {
        this.eventIds.remove(eventId);
    }

    /**
     * Sets the list of events by extracting their IDs.
     *
     * @param events An {@code ArrayList} of {@code Event} objects representing the events to be set.
     */
    public void setEvents(ArrayList<Event> events) {
        eventIds = new ArrayList<>();
        for (Event event : events) {
            eventIds.add(event.getId());
        }
    }
}
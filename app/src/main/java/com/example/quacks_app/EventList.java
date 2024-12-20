package com.example.quacks_app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a list of events with functionality to manage and retrieve event information.
 */
public class EventList extends RepoModel implements Serializable {
    private ArrayList<String> eventIds;

    /**
     * Constructs a new EventList.
     */
    public EventList() {
        eventIds = new ArrayList<>();
    }

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
        this.eventIds.add(event.getDocumentId());
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
        this.eventIds.remove(event.getDocumentId());
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
            eventIds.add(event.getDocumentId());
        }
    }

    /**
     * Checks if the list contains a specific event ID.
     *
     * @param eventId The {@code String} representing the event ID to check.
     * @return {@code true} if the event ID is present in the list, {@code false} otherwise.
     */
    public boolean contains(String eventId) {
        return eventIds.contains(eventId);
    }
}
package com.example.quacks_app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a list of applicants with functionality to manage and retrieve applicant information.
 */
public class ApplicantList extends RepoModel implements Serializable {
    private ArrayList<String> applicantIds;
    private Integer limit;

    /**
     * Constructs a new ApplicantList with an empty list of applicant IDs.
     */
    public ApplicantList() {
        applicantIds = new ArrayList<>();
    }

    /**
     * Retrieves the list of applicant IDs.
     *
     * @return An {@code ArrayList} of {@code String} representing applicant IDs.
     */
    public ArrayList<String> getApplicantIds() {
        return applicantIds;
    }

    /**
     * Retrieves the list of {@code User} applicants.
     *
     * @param readCallback A callback to handle read operations.
     * @return An {@code ArrayList} of {@code User} objects representing the applicants.
     */
    public ArrayList<User> getApplicants(ReadCallback readCallback) {
        return new ArrayList<>(); // placeholder
    }

    /**
     * Sets the list of applicant IDs.
     *
     * @param applicantIds An {@code ArrayList} of {@code String} representing applicant IDs to be set.
     */
    public void setApplicantIds(ArrayList<String> applicantIds) {
        this.applicantIds = applicantIds;
    }

    /**
     * Sets the list of applicants by extracting their IDs.
     *
     * @param applicants An {@code ArrayList} of {@code User} objects representing the applicants to be set.
     */
    public void setApplicants(ArrayList<User> applicants) {
        applicantIds = new ArrayList<>();
        for (User user : applicants) {
            applicantIds.add(user.getDocumentId());
        }
    }

    /**
     * Adds a {@code User} to the list of applicant IDs.
     *
     * @param user The {@code User} object to be added.
     */
    public void addUser(User user) {
        this.applicantIds.add(user.getDocumentId());
    }

    /**
     * Adds a user ID to the list of applicant IDs.
     *
     * @param userId The {@code String} representing the user ID to be added.
     */
    public void addUser(String userId) {
        this.applicantIds.add(userId);
    }

    /**
     * Removes a {@code User} from the list of applicant IDs.
     *
     * @param user The {@code User} object to be removed.
     */
    public void removeUser(User user) {
        this.applicantIds.remove(user.getDocumentId());
    }

    /**
     * Removes a user ID from the list of applicant IDs.
     *
     * @param userId The {@code String} representing the user ID to be removed.
     */
    public void removeUser(String userId) {
        this.applicantIds.remove(userId);
    }

    /**
     * Retrieves the current limit on the number of applicants.
     *
     * @return An {@code Integer} representing the limit on applicants.
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Sets the limit on the number of applicants.
     *
     * @param limit An {@code Integer} specifying the maximum number of applicants allowed.
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * Checks if the given user ID is present in the list of applicant IDs.
     *
     * @param userId The {@code String} representing the user ID to be checked.
     * @return {@code true} if the user ID is present in the list, {@code false} otherwise.
     */
    public boolean contains(String userId) {
        return applicantIds.contains(userId);
    }
}

package com.example.quacks_app;

import java.util.ArrayList;

public class ApplicantList extends RepoModel {
    private ArrayList<String> applicantIds;
    private Integer limit;

    public ApplicantList() {
        applicantIds = new ArrayList<>();
    }

    public ArrayList<String> getApplicantIds() {
        return applicantIds;
    }

    public ArrayList<User> getApplicants(ReadCallback readCallback) {
        return new ArrayList<>(); // placeholder
    }

    public void setApplicantIds(ArrayList<String> applicantIds) {
        this.applicantIds = applicantIds;
    }

    public void setApplicants(ArrayList<User> applicants) {
        applicantIds = new ArrayList<>();
        for (User user : applicants) {
            applicantIds.add(user.getId());
        }
    }

    public void addUser(User user) {
        this.applicantIds.add(user.getId());
    }

    public void addUser(String userId) {
        this.applicantIds.add(userId);
    }

    public void removeUser(User user) {
        this.applicantIds.remove(user.getId());
    }

    public void removeUser(String userId) {
        this.applicantIds.remove(userId);
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public ArrayList<String> drawApplicants(int number) {
        LotterySystem lotterySystem = new LotterySystem();
        return this.applicantIds; // placeholder
    }
}

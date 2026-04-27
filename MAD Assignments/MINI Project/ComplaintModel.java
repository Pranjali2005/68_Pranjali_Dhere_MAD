package com.example.final_project.model;

public class ComplaintModel {

    int id;
    String email;
    String description;
    String image;
    String status;
    String date;
    String proofImage;

    public ComplaintModel(int id, String email, String description,
                          String image, String status, String date, String proofImage) {
        this.id = id;
        this.email = email;
        this.description = description;
        this.image = image;
        this.status = status;
        this.date = date;
        this.proofImage = proofImage;
    }

    public int getId() { return id; }

    public String getEmail() { return email; }

    public String getDescription() { return description; }

    public String getImage() { return image; }

    public String getStatus() { return status; }

    public String getDate() { return date; }

    public String getProofImage() { return proofImage; }

    public void setStatus(String status) {
        this.status = status;
    }
}
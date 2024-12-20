package com.superwallet.models;

import jakarta.persistence.*;

@Entity
@Table(name = "statuses")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private int statusId;

    @Column(name = "status_name")
    private String statusName;

    public Status() {
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}

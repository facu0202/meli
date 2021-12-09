package com.facuferro.meetup.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"meetup_id", "user_id"})
})
public class UserMeetup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Meetup meetup;
    @ManyToOne
    private User user;
    private String token;
    private UserMeetupState status;
    private Boolean notified;

    public boolean isActive(){return this.getStatus().equals(UserMeetupState.ACTIVE);}

    public boolean isPending(){return this.getStatus().equals(UserMeetupState.PENDING);}

    public void toActive(){setStatus(UserMeetupState.ACTIVE);}

    public UserMeetup() {
    }

    public UserMeetup(Long id, Meetup meetup, User user, String token, UserMeetupState status, Boolean notified) {
        this();
        this.id = id;
        this.meetup = meetup;
        this.user = user;
        this.token = token;
        this.status = status;
        this.notified = notified;
    }
}

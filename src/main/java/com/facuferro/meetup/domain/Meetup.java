package com.facuferro.meetup.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Meetup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Location location;
    private MeetupState status;
    private Date date;
    private Date limitDate;
    private Integer duration;
    private Boolean notified;

    @OneToMany(mappedBy = "meetup")
    private List<UserMeetup> usersMeetup;

    public void toClosed(){setStatus(MeetupState.CLOSED);}

    @Override
    public String toString() {
        return "Meetup{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location=" + location +
                ", status=" + status +
                ", date=" + date +
                ", limitDate=" + limitDate +
                ", duration=" + duration +
                ", notified=" + notified +
                '}';
    }
}

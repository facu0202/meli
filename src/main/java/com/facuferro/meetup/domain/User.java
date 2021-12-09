package com.facuferro.meetup.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String email;
    private String name;
    private String surname;
    private String pwd;
    private String token;
    private Role role;
    private UserState status;
    @OneToMany(mappedBy = "user")
    private List<UserMeetup> usersMeetup;

    public String getPassword() {
        return getPwd();
    }
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public boolean isActive(){return this.getStatus().equals(UserState.ACTIVE);}

    public boolean isPending(){return this.getStatus().equals(UserState.PENDING);}

    public void toActive(){setStatus(UserState.ACTIVE);}

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", pwd='" + pwd + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public User() {

    }

    public User(Long id, String email, String name, String surname, String pwd, String token, Role role, UserState status, List<UserMeetup> usersMeetup) {
        this();
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.pwd = pwd;
        this.token = token;
        this.role = role;
        this.status = status;
        this.usersMeetup = usersMeetup;
    }
}

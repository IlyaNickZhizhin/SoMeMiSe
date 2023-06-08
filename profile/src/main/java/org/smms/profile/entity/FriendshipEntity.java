package org.smms.profile.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;

@Entity
@Table(name = "friendship", schema = "profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipEntity {
    
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private ProfileEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "friend_id")
    private ProfileEntity friend;

    @Column(name = "status", nullable = false)
    private String status;
}

package com.example.market.entities;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<Item> items;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="favorites",
        joinColumns = @JoinColumn(name="user_id", referencedColumnName="id"),
        inverseJoinColumns = @JoinColumn(name="item_id", referencedColumnName="id"))
    private Set<Item> favoriteItems = new HashSet<Item>();

    @OneToMany(mappedBy = "user", fetch=FetchType.EAGER)
    private List<Item> orderItems;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "roles", length = 120)
    private String roles;

    @Column(name = "enable", nullable = false)
    private Boolean enable;

    @Column(name = "profile", length = 1000, nullable = true)
    private String profile;

    @Column(name = "image", length = 100, nullable = true)
	private String image;

	@Column(name = "createdAt", nullable = false, updatable = false, insertable = false,
    columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
private ZonedDateTime createdAt;  

    @Column(name = "updatedAt", nullable = false, updatable = false, insertable = false,
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private ZonedDateTime updatedAt;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        return Objects.equals(id, other.id) && Objects.equals(email, other.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}



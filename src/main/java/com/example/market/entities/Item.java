package com.example.market.entities;

import java.time.ZonedDateTime;
import java.util.HashSet;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item")
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
	private User user;

    @ManyToOne(fetch = FetchType.EAGER)
	private Category category;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="favorites",
        joinColumns = @JoinColumn(name="item_id", referencedColumnName="id"),
        inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName="id"))
    private Set<User> favoritedUsers = new HashSet<User>();

    @ManyToOne(fetch=FetchType.EAGER)
    private User orderedUser;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 1000, nullable = false)
    private String description;

    @Column(name = "price", scale = 1000000)
	private int price;

    @Column(name = "image", length = 100, nullable = false)
	private String image;

    @Column(name = "isOnSale", nullable = false)
	private boolean isOnSale;

	@Column(name = "createdAt", nullable = false, updatable = false, insertable = false,
    columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
private ZonedDateTime createdAt;  

    @Column(name = "updatedAt", nullable = false, updatable = false, insertable = false,
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private ZonedDateTime updatedAt;

}



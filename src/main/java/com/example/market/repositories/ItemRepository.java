package com.example.market.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.market.entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByUserId(Long id);

    List<Item> findByUserIdOrderByCreatedAtDesc(Long id);

    List<Item> findByUserIdInOrderByCreatedAtDesc(List<Long> ids);

    List<Item> findByfavoritedUsersId(Long id);

    List<Item> findByfavoritedUsersIdOrderByCreatedAtDesc(Long id);

    List<Item> findByOrderedUserIdOrderByCreatedAtDesc(Long id);
    
}

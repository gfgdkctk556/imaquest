package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.HealerCharacterEntity;


public interface HealerCharacterRepository extends JpaRepository<HealerCharacterEntity, Long> {
    // 任意のカスタムのデータアクセスメソッドを追加できます
}

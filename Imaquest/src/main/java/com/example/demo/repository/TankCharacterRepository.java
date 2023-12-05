package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.TankCharacterEntity;


public interface TankCharacterRepository extends JpaRepository<TankCharacterEntity, Long> {
    // 任意のカスタムのデータアクセスメソッドを追加することができます
}

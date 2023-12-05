package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.WizardCharacterEntity;


public interface WizardCharacterRepository extends JpaRepository<WizardCharacterEntity, Long> {
    // 任意のカスタムのデータアクセスメソッドを追加できます
}

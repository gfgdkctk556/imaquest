package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "tanks") // テーブル名を指定
@Entity
public class TankCharacterEntity {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    
    private int player_id; // カラム名に合わせてフィールド名を変更
    private String character_Name; // カラム名に合わせてフィールド名を変更
    private int character_Level; // カラム名に合わせてフィールド名を変更
    private int character_Experience; // カラム名に合わせてフィールド名を変更
    private int character_HP; // カラム名に合わせてフィールド名を変更
    private int character_MP; // カラム名に合わせてフィールド名を変更
    private int character_Attack; // カラム名に合わせてフィールド名を変更
    private int character_Defense; // カラム名に合わせてフィールド名を変更
    private String character_Image; // カラム名に合わせてフィールド名を変更

 // ゲッターとセッター（getterとsetter）を追加
    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public String getCharacter_name() {
        return character_Name;
    }

    public void setCharacter_Name(String character_Name) {
        this.character_Name = character_Name;
    }

    public int getCharacter_Level() {
        return character_Level;
    }

    public void setCharacter_Level(int character_Level) {
        this.character_Level = character_Level;
    }

    public int getCharacter_Experience() {
        return character_Experience;
    }

    public void setCharacter_Experience(int character_Experience) {
        this.character_Experience = character_Experience;
    }

    public int getCharacter_HP() {
        return character_HP;
    }

    public void setCharacter_HP(int character_HP) {
        this.character_HP = character_HP;
    }

    public int getCharacter_MP() {
        return character_MP;
    }

    public void setCharacter_MP(int character_MP) {
        this.character_MP = character_MP;
    }

    public int getCharacter_Attack() {
        return character_Attack;
    }

    public void setCharacter_Attack(int character_Attack) {
        this.character_Attack = character_Attack;
    }

    public int getCharacter_Defense() {
        return character_Defense;
    }

    public void setCharacter_Defense(int character_Defense) {
        this.character_Defense = character_Defense;
    }

    public String getCharacter_Image() {
        return character_Image;
    }

    public void setCharacter_Image(String character_Image) {
        this.character_Image = character_Image;
    }
}

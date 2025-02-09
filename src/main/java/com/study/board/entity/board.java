package com.study.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String content;

    private String filename;

    private String filepath;
    private Integer boardNumber;
    public Integer getBoardNumber() {
        return boardNumber;
    }
    public void setBoardNumber(Integer boardNumber) {
        this.boardNumber = boardNumber;
    }

}

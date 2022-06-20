package com.bb8qq.tgbotproject.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tg_logger")
@Data
public class TgLogger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time")
    private Long time;

    @Column(name = "msg")
    private String msg;

    @Column(name = "tag")
    private String tag;

}

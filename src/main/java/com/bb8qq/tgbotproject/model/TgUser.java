package com.bb8qq.tgbotproject.model;

import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "tg_user")
@Data
public class TgUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String username;

    @Column(name = "chat_id")
    private Long chatId;

}

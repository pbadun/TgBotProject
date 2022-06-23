package com.bb8qq.tgbotproject.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tg_user")
@Data
public class TgUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uname")
    private String uname;

    @Column(name = "chat_id")
    private Long chatId;

    // Отправлять сообщения?
    @Column(name = "active")
    private Boolean act;

    // Пользователь удалил и заблокировал бота.
    @Column(name = "del")
    private Boolean del;

}

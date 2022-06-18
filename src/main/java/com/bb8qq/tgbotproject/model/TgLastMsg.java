package com.bb8qq.tgbotproject.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "tg_last_msg")
@Data
@ToString
public class TgLastMsg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "msg")
    private String msg;
}

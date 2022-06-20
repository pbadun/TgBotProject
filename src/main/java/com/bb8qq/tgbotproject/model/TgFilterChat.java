package com.bb8qq.tgbotproject.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "tg_filter_chat")
@Data
@ToString
public class TgFilterChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TgUser tgUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private TgTrackingChat tgTrackingChat;

    @Column(name = "word")
    private String word;

}

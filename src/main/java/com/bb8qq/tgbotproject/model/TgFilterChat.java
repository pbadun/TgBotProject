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

    // Канал
    @ManyToOne(fetch = FetchType.LAZY)
    private TgTrackingChannel trackingChannel;

    // Объект ыильтра
    @ManyToOne(fetch = FetchType.LAZY)
    private TgMyChannels myChannels;

    // Искомое слово
    @Column(name = "word")
    private String word;

}

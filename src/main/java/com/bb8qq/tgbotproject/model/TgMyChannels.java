package com.bb8qq.tgbotproject.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tg_my_channels")
@ToString
public class TgMyChannels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TgUser tgUser;

    @ManyToOne(fetch = FetchType.EAGER)
    private TgTrackingChannel channel;

    @Column(name = "channel_name")
    private String channelName;

}

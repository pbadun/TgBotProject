package com.bb8qq.tgbotproject.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "tg_tracking_channel")
@Data
@ToString
public class TgTrackingChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "channel_id")
    private Long channelId;

    @Column(name = "channel_name")
    private String channelName;

}

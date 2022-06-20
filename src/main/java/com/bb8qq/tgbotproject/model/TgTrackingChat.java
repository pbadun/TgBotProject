package com.bb8qq.tgbotproject.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tg_tracking_chat")
@Data
@ToString
public class TgTrackingChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TgUser tgUser;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tgTrackingChat")
    private List<TgFilterChat> tgFilterChats;

}

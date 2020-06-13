package com.lazybot.microservices.commons.model.mission;

import com.lazybot.microservices.commons.model.Bot;
import com.lazybot.microservices.commons.model.Item;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ExchangeMission extends Mission {
    private Bot bot1;
    private Bot bot2;
    private String bot1Username;
    private String bot2Username;
    private List<Item> itemsGiveByBot1;
    private List<Item> itemsGiveByBot2;

    public ExchangeMission() {
    }



    public ExchangeMission(String bot1Username, String bot2Username, List<Item> itemsGiveByBot1) {
        this.bot1Username = bot1Username;
        this.bot2Username = bot2Username;
        this.itemsGiveByBot1 = itemsGiveByBot1;
    }

    public ExchangeMission(Bot bot1, Bot bot2, List<Item> itemsGiveByBot1, List<Item> itemsGiveByBot2) {
        this.bot1 = bot1;
        this.bot2 = bot2;
        this.itemsGiveByBot1 = itemsGiveByBot1;
        this.itemsGiveByBot2 = itemsGiveByBot2;
    }

    public ExchangeMission(int step, Bot bot1, Bot bot2, List<Item> itemsGiveByBot1, List<Item> itemsGiveByBot2) {
        super(step);
        this.bot1 = bot1;
        this.bot2 = bot2;
        this.itemsGiveByBot1 = itemsGiveByBot1;
        this.itemsGiveByBot2 = itemsGiveByBot2;
    }

    public ExchangeMission(int missionId, int step, Bot bot1, Bot bot2, List<Item> itemsGiveByBot1, List<Item> itemsGiveByBot2) {
        super(missionId, step);
        this.bot1 = bot1;
        this.bot2 = bot2;
        this.itemsGiveByBot1 = itemsGiveByBot1;
        this.itemsGiveByBot2 = itemsGiveByBot2;
    }
}

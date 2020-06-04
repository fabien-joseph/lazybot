package com.lazybot.microservices.commons.model.mission;

import com.lazybot.microservices.commons.model.Bot;
import com.lazybot.microservices.commons.model.Item;
import lombok.Data;
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
    private List<Item> itemsGiveByBot1;
    private List<Item> itemsGiveByBot2;

    public ExchangeMission() {
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
}

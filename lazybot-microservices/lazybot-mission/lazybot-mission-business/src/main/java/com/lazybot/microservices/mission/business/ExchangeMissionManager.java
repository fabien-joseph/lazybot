package com.lazybot.microservices.mission.business;

import com.google.gson.Gson;
import com.lazybot.microservices.commons.model.*;
import com.lazybot.microservices.commons.model.mission.ExchangeMission;
import com.lazybot.microservices.commons.model.mission.Mission;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Mission exchange items between two bots
 */
@Getter
@Setter
@ToString
public class ExchangeMissionManager extends MissionAbstractManager {

    public ExchangeMissionManager() throws NoSuchMethodException {
        initializeSteps();
    }

    @Override
    public void initializeSteps() throws NoSuchMethodException {
        Class<ExchangeMissionManager> thisClass = ExchangeMissionManager.class;
        List<Method> list = new ArrayList<>();
        list.add(thisClass.getDeclaredMethod("botsJoinEachOthers", ExchangeMission.class));
        list.add(thisClass.getDeclaredMethod("botsLookEachOther", Mission.class));
        list.add(thisClass.getDeclaredMethod("botsDropItems", Mission.class));

        for (Method m : list) {
            m.setAccessible(true);
        }

        super.setSteps(list);
    }

    private void botsJoinEachOthers(ExchangeMission missionObject) {
        OrderBot<Integer> orderSetStatus = new OrderBot<>(missionObject.getId(), missionObject.getBot1().getUsername(), missionObject.getId(),
                "exchange", 0);
        super.getMasterSocket().emit("missionStatus", new Gson().toJson(orderSetStatus));

        OrderBot<Position> orderGoToPos = new OrderBot<>(missionObject.getId(), missionObject.getBot1().getUsername(), missionObject.getBot2().getPosition(),
                "exchange", 0);

        System.out.println("évent envoyé");
        super.getMasterSocket().emit("goToPos", new Gson().toJson(orderGoToPos));
    }

    private void botsLookEachOther(Mission missionObject) {
        System.out.println("Etape " + missionObject.getStep());

    }

    private void botsDropItems(Mission missionObject) {
        System.out.println("Etape " + missionObject.getStep());
    }

    // === Check values ===
    private boolean checkPossessions(List<Item> botInventory, List<Item> itemsGaveByBot1) {
        for (Item i : botInventory) {
            System.out.println("lol");
        }
        return true;
    }

    private boolean isBotsHasItems(Bot bot1, Bot bot2, List<Item> itemsGiveByBot1, List<Item> itemsGiveByBot2) {
        boolean bot1HasItems = checkPossessions(bot1.getInventory().getSlots(), itemsGiveByBot1);
        boolean bot2HasItems = checkPossessions(bot2.getInventory().getSlots(), itemsGiveByBot2);

        // Bots has items ?
        return bot1HasItems && bot2HasItems;
    }
}

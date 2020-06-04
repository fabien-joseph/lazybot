package com.lazybot.microservices.mission.business;

import com.corundumstudio.socketio.SocketIOClient;
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
        list.add(thisClass.getDeclaredMethod("botsGoToPos", ExchangeMission.class));
        list.add(thisClass.getDeclaredMethod("botsLookEachOther", Mission.class));
        list.add(thisClass.getDeclaredMethod("botsDropItems", Mission.class));

        for (Method m : list) {
            m.setAccessible(true);
        }

        super.setSteps(list);
    }

    private void botsGoToPos(ExchangeMission missionObject) throws Exception {
        if (isBotsHasItems(missionObject.getBot1(), missionObject.getBot2(),
                missionObject.getItemsGiveByBot1(), missionObject.getItemsGiveByBot2())) {
            super.getMasterSocket().sendEvent("error", "Exchange mission impossible !");
            return;
        }

        OrderBot<Position> order = new OrderBot<>(missionObject.getBot1().getUsername(), missionObject.getBot2().getPosition(),
                "exchange", 0);
        super.getMasterSocket().sendEvent("goToPos", order);
    }

    private void botsLookEachOther(Mission missionObject) {
        System.out.println("Etape " + missionObject.getStep());

    }

    private void botsDropItems(Mission missionObject) {
        System.out.println("Etape " + missionObject.getStep());


    }

    // === Check values ===
    private boolean checkPossessions(List<Item> botInventory, List<Item> itemsGaveByBot1) {
        for (Item item : itemsGaveByBot1) {
            if (!botInventory.contains(item))
                return false;
        }
        return true;
    }

    private boolean isBotsHasItems(Bot bot1, Bot bot2, List<Item> itemsGiveByBot1, List<Item> itemsGiveByBot2) throws Exception {
        boolean bot1HasItems = checkPossessions(bot1.getInventory().getSlots(), itemsGiveByBot1);
        boolean bot2HasItems = checkPossessions(bot2.getInventory().getSlots(), itemsGiveByBot2);

        // Bots has items ?
        return bot1HasItems && bot2HasItems;
    }
}

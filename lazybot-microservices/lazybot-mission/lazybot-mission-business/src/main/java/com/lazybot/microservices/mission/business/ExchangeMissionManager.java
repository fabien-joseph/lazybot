package com.lazybot.microservices.mission.business;

import com.google.gson.Gson;
import com.lazybot.microservices.commons.model.*;
import com.lazybot.microservices.commons.model.mission.ExchangeMission;
import com.lazybot.microservices.commons.model.mission.Mission;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.security.auth.callback.ConfirmationCallback;
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
        list.add(thisClass.getDeclaredMethod("botsLookEachOther", ExchangeMission.class));
        list.add(thisClass.getDeclaredMethod("botsDropItems", ExchangeMission.class));
        list.add(thisClass.getDeclaredMethod("botsGoFarAway", ExchangeMission.class));

        for (Method m : list) {
            m.setAccessible(true);
        }

        super.setSteps(list);
    }

    /**
     * The bot who give the item must join the bot who receive.
     * @param missionObject {@link ExchangeMission} contains all the information to do execute the step
     */
    private void botsJoinEachOthers(ExchangeMission missionObject) {
        if (isBotsHasItems(missionObject.getBot1(), missionObject.getItemsGiveByBot1())) {
            OrderBot<Integer> orderSetStatus = new OrderBot<>(missionObject.getId(), missionObject.getBot1().getUsername(), missionObject.getId(), missionObject.getStep());
            super.getMasterSocket().emit("missionStatus", new Gson().toJson(orderSetStatus));
            OrderBot<Position> orderGoToPos = new OrderBot<>(missionObject.getId(), missionObject.getBot1().getUsername(),
                    missionObject.getBot2().getPosition(), missionObject.getStep());
            System.out.println("Bot 1 rejoint Bot 2");
            super.getMasterSocket().emit("goToPos", new Gson().toJson(orderGoToPos));
        } else {
            super.getMasterSocket().emit("missionFail", new Gson().toJson(missionObject.getId()));
        }
    }

    /**
     * The bot who give the item must look the floor (to avoid that he doesn't drop the item too far away of the bot who receive).
     * @param missionObject {@link ExchangeMission} contains all the information to do execute the step
     */
    private void botsLookEachOther(ExchangeMission missionObject) {
        OrderBot<Look> orderLook = new OrderBot<>(missionObject.getId(), missionObject.getBot1().getUsername(),
                new Look(0, -90), missionObject.getStep());
        System.out.println("Bot 1 regarde au sol");
        super.getMasterSocket().emit("look", new Gson().toJson(orderLook));
    }

    /**
     * The bot who give the item must drop them.
     * @param missionObject {@link ExchangeMission} contains all the information to do execute the step
     */
    private void botsDropItems(ExchangeMission missionObject) throws InterruptedException {
        System.out.println("Bot 1 drop les items" + missionObject.getItemsGiveByBot1());

        OrderBot<List<Item>> orderDrop = new OrderBot<>(missionObject.getId(), missionObject.getBot1().getUsername(),
                missionObject.getItemsGiveByBot1(), missionObject.getStep());
        Thread.sleep(200);

        super.getMasterSocket().emit("drop", new Gson().toJson(orderDrop));
    }

    /**
     * The bot who give the item must go away.
     * @param missionObject {@link ExchangeMission} contains all the information to do execute the step
     */
    private void botsGoFarAway(ExchangeMission missionObject) {
        Position posFarAway = missionObject.getBot2().getPosition();
        posFarAway.setX(posFarAway.getX() + 3);
        OrderBot<Position> orderGoToPos = new OrderBot<>(missionObject.getId(), missionObject.getBot1().getUsername(),
                posFarAway, missionObject.getStep());
        super.getMasterSocket().emit("goToPos", new Gson().toJson(orderGoToPos));
    }

    /**
     * Check if the bot who give the item has it
     * @param bot {@link Bot}
     * @param itemsGiveByBot the list of items to give (actually just 1 item max)
     * @return
     */
    private boolean isBotsHasItems(Bot bot, List<Item> itemsGiveByBot) {
        int itemsItHas = 0;

        for (Item item : itemsGiveByBot) {
            for (int j = 0; j < bot.getInventory().getSlots().size(); j++) {
                if (bot.getInventory().getSlots().get(j) != null)
                    if (item.getType() == bot.getInventory().getSlots().get(j).getType())
                        itemsItHas++;
            }
        }
        return itemsGiveByBot.size() <= itemsItHas;
    }
}

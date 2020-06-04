package com.lazybot.microservices.mission.business;

import com.corundumstudio.socketio.SocketIOClient;
import com.lazybot.microservices.commons.model.*;
import com.lazybot.microservices.mission.model.MissionAbstract;
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
public class Exchange<T> extends MissionAbstract<T> {
    SocketIOClient socketIOClient;

    public Exchange() throws NoSuchMethodException {
        initializeSteps();
    }

    public Exchange(SocketIOClient socketIOClient, int step) throws NoSuchMethodException {
        super.setStepActual(step);
        this.socketIOClient = socketIOClient;
        initializeSteps();
    }

    @Override
    public void initializeSteps() throws NoSuchMethodException {
        Class<Exchange> thisClass = Exchange.class;
        List<Method> list = new ArrayList<>();
        list.add(thisClass.getDeclaredMethod("botsGoToPos", List.class));
        list.add(thisClass.getDeclaredMethod("botsLookEachOther"));
        list.add(thisClass.getDeclaredMethod("botsDropItems"));
        super.setSteps(list);
    }

    @Override
    public void initializeMissions() throws NoSuchMethodException {

    }

    @Override
    public void test() {
        System.out.println("TEST RECU");
    }

    public void botsGoToPos(List<Object> datas) throws Exception {
        System.out.println("Coucou");
/*        Bot bot1 = (Bot) datas.get(0);
        Bot bot2 = (Bot) datas.get(1);

        isBotsHasItems(datas);

        Order<Position> orderBot1 = new Order<>(bot1.getUsername(), bot1.getPosition(), "exchange", super.getStepActual());
        socketIOClient.sendEvent("goToPos", orderBot1);*/
    }

    private void botsLookEachOther() {
    }

    private void botsDropItems() {
    }

    // === Check values ===
    private boolean checkPossessions(List<Item> botInventory, List<Item> itemsGaveByBot1) {
        for (Item item : itemsGaveByBot1) {
            if (!botInventory.contains(item))
                return false;
        }
        return true;
    }

    private void isBotsHasItems(List<Object> datas) throws Exception {
        Bot bot1 = (Bot) datas.get(0);
        Bot bot2 = (Bot) datas.get(1);
        boolean bot1HasItems = checkPossessions(bot1.getInventory().getSlots(), (List<Item>) datas.get(2));
        boolean bot2HasItems = checkPossessions(bot2.getInventory().getSlots(), (List<Item>) datas.get(3));

        // Bots has items
        if (!(bot1HasItems && bot2HasItems))
            throw new Exception("The bots don't have items to exchange");
    }
}

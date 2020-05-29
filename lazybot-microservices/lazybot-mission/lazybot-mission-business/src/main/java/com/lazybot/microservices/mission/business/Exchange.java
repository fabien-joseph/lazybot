package com.lazybot.microservices.mission.business;

import com.corundumstudio.socketio.SocketIOClient;
import com.lazybot.microservices.commons.model.Bot;
import com.lazybot.microservices.commons.model.Item;
import com.lazybot.microservices.commons.model.Mission;
import com.lazybot.microservices.mission.model.MissionTools;
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
public class Exchange<T> extends Mission<T> {
    private List<Method> steps;
    SocketIOClient socketIOClient;

    public Exchange() throws NoSuchMethodException {
        initializeSteps();
    }

    public Exchange(SocketIOClient socketIOClient,int step) throws NoSuchMethodException {
        super.setStep(step);
        this.socketIOClient = socketIOClient;
        initializeSteps();
    }

    public void initializeSteps() throws NoSuchMethodException {
        Class<Exchange> thisClass = Exchange.class;
        this.steps = new ArrayList<>();
        this.steps.add(thisClass.getDeclaredMethod("isBotsHasItems"));
        this.steps.add(thisClass.getDeclaredMethod("getPosOfBots"));
        this.steps.add(thisClass.getDeclaredMethod("botsGoToPos"));
        this.steps.add(thisClass.getDeclaredMethod("botsLookEachOther"));
        this.steps.add(thisClass.getDeclaredMethod("botsDropItems"));
        super.setStepMax(steps.size());
    }

    public final Method getStep(int step) {
        return steps.get(step);
    }

    private void isBotsHasItems(Bot bot1, Bot bot2, List<Item> itemsGaveByBot1, List<Item> itemsGaveByBot2) {

    }

    private void getPosOfBots() {
    }

    private void botsGoToPos() {
    }

    private void botsLookEachOther() {
    }

    private void botsDropItems() {
    }
}

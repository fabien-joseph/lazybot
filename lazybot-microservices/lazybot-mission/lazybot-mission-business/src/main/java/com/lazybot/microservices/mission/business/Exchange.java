package com.lazybot.microservices.mission.business;

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

    public Exchange() throws NoSuchMethodException {
        initializeSteps();
    }

    public Exchange(int step) throws NoSuchMethodException {
        super.setStep(step);
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

    private void isBotsHasItems() {
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

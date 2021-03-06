package com.cazsius.xpplus.emblem;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class EmblemTime extends ItemEmblem {

    private final int cost;
    private final TimeStage time;

    public EmblemTime(Properties properties, int cost, TimeStage time) {
        super(properties);
        this.cost = cost;
        this.time = time;
    }

    @Override
    public EmblemType getEmblemType() {
        return EmblemType.MANUAL;
    }

    @Override
    public int getLevelCost() {
        return cost;
    }

    @Override
    public boolean onUseEmblem(World world, PlayerEntity player) {
        long timeOfDay = world.getDayTime() % 24000L;
        boolean isDay = timeOfDay >= 1000 && timeOfDay < 13000;

        if (time.equals(TimeStage.DAY) && !isDay) {
            if (!world.isRemote) {
                long increment = 24000 - ((timeOfDay - 1000) % 24000);
                world.setDayTime(world.getDayTime() + increment);
            }
            player.getCooldownTracker().setCooldown(this, 600);
            return true;
        }
        if (time.equals(TimeStage.NIGHT) && isDay) {
            if (!world.isRemote) {
                long increment = 24000 - ((timeOfDay - 13000) % 24000);
                world.setDayTime(world.getDayTime() + increment);
            }
            player.getCooldownTracker().setCooldown(this, 600);
            return true;
        }
        return false;
    }

    public enum TimeStage {
        DAY, NIGHT
    }

}
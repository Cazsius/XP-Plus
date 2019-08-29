package com.cazsius.xpplus.emblem;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class EmblemWeather extends ItemEmblem {

    private final int cost;
    private final WeatherType weather;

    public EmblemWeather(Properties properties, int cost, WeatherType weather) {
        super(properties);
        this.cost = cost;
        this.weather = weather;
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
        WorldInfo worldState = world.getWorldInfo();
            int clear = 0, rain = 0, thunder = 0;
            switch (weather) {
                case CLEAR:
                    clear = 24000;
                    break;
                case RAIN:
                    rain = 24000;
                    break;
                case THUNDER:
                    rain = 24000;
                    thunder = 24000;
                    break;
            }
        if (!world.isRemote) {
            worldState.setClearWeatherTime(clear);
            worldState.setRainTime(rain);
            worldState.setThunderTime(thunder);
            worldState.setRaining(rain > 0);
            worldState.setThundering(thunder > 0);
        }
        player.getCooldownTracker().setCooldown(this, 600);
        return true;
    }

    public enum WeatherType { CLEAR, RAIN, THUNDER }

}
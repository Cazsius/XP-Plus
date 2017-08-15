package me.poke.experienceplus.items.emblem;

import me.poke.experienceplus.items.ItemEmblem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class EmblemWeather extends ItemEmblem implements IEmblem {

    private final int cost;
    private final WeatherType weather;

    public EmblemWeather(String name, int cost, WeatherType weather) {
        super(name);
        this.cost = cost;
        this.weather = weather;
        setMaxDamage(7);
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
    public boolean onUseEmblem(World world, EntityPlayer player) {
        WorldInfo worldState = world.getWorldInfo();
            int clear = 0, rain = 0, thunder = 0;
            switch (weather) {
                case CLEAR:
                    clear = 24000;
                    rain = 0;
                    thunder = 0;
                    break;
                case RAIN:
                    clear = 0;
                    rain = 24000;
                    thunder = 0;
                    break;
                case THUNDER:
                    clear = 0;
                    rain = 24000;
                    thunder = 24000;
                    break;
            }
        if (!world.isRemote) {
            worldState.setCleanWeatherTime(clear);
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

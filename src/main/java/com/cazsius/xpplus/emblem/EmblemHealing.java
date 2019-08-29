package com.cazsius.xpplus.emblem;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class EmblemHealing extends ItemEmblem {

    private final int cost;
    private final float health;

    public EmblemHealing(Properties properties, int cost, float health) {
        super(properties);
        this.cost = cost;
        this.health = health;
    }

    @Override
    public boolean onUseEmblem(World world, PlayerEntity player) {
        if (!player.shouldHeal()) {
            return false;
        }
        player.heal(health);
        player.getCooldownTracker().setCooldown(this, 600);
        return true;
    }

    @Override
    public EmblemType getEmblemType() {
        return EmblemType.MANUAL;
    }

    @Override
    public int getLevelCost() {
        return cost;
    }

}
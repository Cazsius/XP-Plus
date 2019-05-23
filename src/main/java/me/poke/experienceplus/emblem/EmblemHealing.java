package me.poke.experienceplus.emblem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EmblemHealing extends ItemEmblem {

    private final int cost;
    private final float health;

    public EmblemHealing(String name, int cost, float health) {
        super(name);
        this.cost = cost;
        this.health = health;
        setMaxDamage(15);
        requires_sneak = false;
    }

    @Override
    public boolean onUseEmblem(World world, EntityPlayer player) {
        if (!player.shouldHeal()) {
            return false;
        }
        player.heal(health);
        player.getCooldownTracker().setCooldown(this, 200);
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

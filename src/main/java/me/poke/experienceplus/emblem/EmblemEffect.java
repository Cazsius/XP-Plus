package me.poke.experienceplus.emblem;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EmblemEffect extends ItemEmblem {

    private final int cost;
    private final Potion potion;
    private final int amplifier;

    public EmblemEffect(String name, int cost, Potion potion, int amplifier) {
        super(name);
        this.cost = cost;
        this.potion = potion;
        this.amplifier = amplifier;
        setMaxDamage(1799);
    }

    @Override
    public EmblemType getEmblemType() {
        return EmblemType.TOGGLEABLE;
    }

    @Override
    public int getLevelCost() {
        return cost;
    }

    @Override
    public boolean onUseEmblem(World world, EntityPlayer player) {
        return true;
    }

    @Override @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        int uses = (stack.getMaxDamage() + 1) - stack.getMetadata();
        tooltip.add(I18n.format("tooltip.experienceplus.remaining_minutes", uses / 60));
        super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public void onEmblemTick(ItemStack stack, EntityPlayer player) {
        if (isEmblemEnabled(stack) && !player.world.isRemote && player.ticksExisted % 20 == 0) {
            player.addPotionEffect(new PotionEffect(potion, 80, amplifier, true, false));
            stack.damageItem(1, player);
        }
    }

}

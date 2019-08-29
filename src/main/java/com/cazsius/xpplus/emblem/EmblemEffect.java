package com.cazsius.xpplus.emblem;

import com.cazsius.xpplus.XpPlus;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class EmblemEffect extends ItemEmblem {

    private final int cost;
    private final Effect effect;
    private final int amplifier;

    public EmblemEffect(Properties properties, int cost, Effect effect, int amplifier) {
        super(properties);
        this.cost = cost;
        this.effect = effect;
        this.amplifier = amplifier;
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
    public boolean onUseEmblem(World world, PlayerEntity player) {
        return true;
    }

    @Override @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        int uses = (stack.getMaxDamage() + 1) - stack.getDamage();
        tooltip.add(new StringTextComponent(I18n.format("tooltip.experienceplus.remaining_minutes", uses)));
        super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public void onEmblemTick(ItemStack stack, PlayerEntity player, Entity entity) {
        if (isEmblemEnabled(stack) && !player.world.isRemote && player.ticksExisted % 20 == 0) {
            XpPlus.LOGGER.info("this works");
            player.addPotionEffect(new EffectInstance(effect, 80, amplifier, true, false));
            stack.damageItem(1,player, l ->l.sendBreakAnimation(EquipmentSlotType.MAINHAND));
        }
    }

}
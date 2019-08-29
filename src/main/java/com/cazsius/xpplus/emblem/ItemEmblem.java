package com.cazsius.xpplus.emblem;

import com.cazsius.xpplus.XpPlus;
import com.cazsius.xpplus.util.ExpLevelHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.minecraft.util.math.BlockPos.getAllInBox;

public abstract class ItemEmblem extends Item {

    protected ItemEmblem(Properties properties) {

        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (!stack.hasTag() && getEmblemType().equals(EmblemType.TOGGLEABLE)) {
            CompoundNBT tag = new CompoundNBT();
            tag.putBoolean("activated", false);
            tag.putBoolean("enabled", false);
            stack.setTag(tag);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (player.isSneaking()) {
            ActionResult<ItemStack> success = new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
            if (!toggleEmblemState(world, player, hand)) {
                if (tryActivateEmblem(world, player, hand)) {
                    return success;
                }
            } else {
                return success;
            }
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        String key = stack.getTranslationKey() + ".tooltip";
        if (I18n.hasKey(key)) {
            // Description tooltips
            tooltip.add(new StringTextComponent(I18n.format(key)));
        }

        tooltip.addAll(getEmblemTooltip(stack));
        if (getEmblemType().equals(EmblemType.MANUAL)) {
            int uses = (stack.getMaxDamage() + 1) - stack.getDamage();
            tooltip.add(new StringTextComponent(I18n.format("tooltip.experienceplus.remaining_uses", uses)));
        }

    }

    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return isEmblemEnabled(stack);
    }

    public boolean isEmblemEnabled(ItemStack stack) {
        XpPlus.LOGGER.info("this is toggleable");
        return  !getEmblemType().equals(EmblemType.TOGGLEABLE)
                || ((stack.hasTag()
                && stack.getTag() != null
                && stack.getTag().getBoolean("enabled")));

    }

    @OnlyIn(Dist.CLIENT)
    private List<ITextComponent> getEmblemTooltip(ItemStack stack) {
        List<ITextComponent> tooltip = new ArrayList<>();
        if (stack.hasTag() && stack.getTag() != null) {
            if (stack.getTag().getBoolean("activated")) {
                boolean enabled = stack.getTag().getBoolean("enabled");
                tooltip.add(new StringTextComponent(I18n.format("tooltip.experienceplus." + (enabled ? "disable" : "enable"))));
            } else {
                tooltip.add(new StringTextComponent(I18n.format("tooltip.experienceplus.activate", getLevelCost())));
            }
        }
        return tooltip;
    }

    public abstract EmblemType getEmblemType();

    public abstract int getLevelCost();

    public boolean onUseEmblem(World world, PlayerEntity player) {
        return false;
    }

    public void onEmblemTick(ItemStack stack, PlayerEntity player, Entity entity) {
        // no-op
    }


    private boolean toggleEmblemState(World world, PlayerEntity player, Hand hand) {
        if (getEmblemType().equals(EmblemType.TOGGLEABLE)) {
            ItemStack stack = player.getHeldItem(hand);
            if (stack.hasTag() && stack.getTag() != null) {
                CompoundNBT nbt = stack.getTag();
                if (nbt.getBoolean("activated")) {
                    boolean state = nbt.getBoolean("enabled");
                    world.playSound(null, player.posX, player.posY, player.posZ,
                            SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.PLAYERS,
                            0.5F, (state ? 0.1F : 0.8F));
                    nbt.putBoolean("enabled", !state);
                } else {
                    if (tryActivateEmblem(world, player, hand)) {
                        nbt.putBoolean("activated", true);
                    } else {
                        player.sendStatusMessage(new TranslationTextComponent(
                                "message.experienceplus.no_experience"), true);
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean tryActivateEmblem(World world, PlayerEntity player, Hand hand) {
        int expCost = ExpLevelHelper.getExperienceForLevel(getLevelCost());
        if (ExpLevelHelper.getPlayerExp(player) >= expCost) {
            if (onUseEmblem(world, player)) {
                ExpLevelHelper.removePlayerExp(player, expCost);
                playActivationSound(world, player);
                createActivationAura(world, player);
                if (getEmblemType().equals(EmblemType.MANUAL)) {
                    player.getHeldItem(hand).damageItem(1, player, l ->l.sendBreakAnimation(EquipmentSlotType.MAINHAND));
                }
                return true;
            }
        }
        return false;
    }

    private static void playActivationSound(World world, PlayerEntity player) {
        SoundEvent sound = SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE;
        world.playSound(null, player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, 2.0F, 0.2F);
    }

    private static void createActivationAura(World world, PlayerEntity player) {
        BlockPos min = player.getPosition().subtract(new Vec3i(1, 0, 1));
        BlockPos max = player.getPosition().add(new Vec3i(1, 0, 1));
        Iterable<BlockPos> area = BlockPos.getAllInBoxMutable(min, max);
        for (BlockPos pos : area) {
            double x = pos.getX(), y = pos.getY(), z = pos.getZ();
            spawnAreaParticle(world, ParticleTypes.PORTAL, new Random(), x, y + 1.5, z);
            spawnAreaParticle(world, ParticleTypes.PORTAL, new Random(), x, y + 0.5, z);
        }
    }

    private static void spawnAreaParticle(World world, BasicParticleType particle, Random random, double x, double y, double z) {
        world.addParticle(ParticleTypes.PORTAL, x - 0.5, y, z, -(random.nextFloat() % 2), 0, 0);
        world.addParticle(ParticleTypes.PORTAL, x, y, z - 0.5, 0, 0, -(random.nextFloat() % 2));
        world.addParticle(ParticleTypes.PORTAL, x - 0.5, y, z - 0.5, -(random.nextFloat() % 2), 0, -(random.nextFloat() % 2));
        world.addParticle(ParticleTypes.PORTAL, x, y, z, 0, 0, 0);
        world.addParticle(ParticleTypes.PORTAL, x + 0.5, y, z + 0.5, (random.nextFloat() % 2), 0, (random.nextFloat() % 2));
        world.addParticle(ParticleTypes.PORTAL, x, y, z + 0.5, 0, 0, (random.nextFloat() % 2));
        world.addParticle(ParticleTypes.PORTAL, x + 0.5, y, z, (random.nextFloat() % 2), 0, 0);
    }

    public enum EmblemType {
        MANUAL, TOGGLEABLE
    }

}
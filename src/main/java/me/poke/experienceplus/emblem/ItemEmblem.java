package me.poke.experienceplus.emblem;

import me.poke.experienceplus.ExperiencePlus;
import me.poke.experienceplus.util.ExpLevelHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class ItemEmblem extends Item {

    protected ItemEmblem(String name) {
        setRegistryName(name);
        setTranslationKey(name);
        //setRegistryName(ExperiencePlus.MOD_ID + "." + name);
        setCreativeTab(ExperiencePlus.CTAB);
        setMaxStackSize(1);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (!stack.hasTagCompound() && getEmblemType().equals(EmblemType.TOGGLEABLE)) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("activated", false);
            tag.setBoolean("enabled", false);
            stack.setTagCompound(tag);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (player.isSneaking()) {
            ActionResult<ItemStack> success = new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
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
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        String key = stack.getItem().getRegistryName() + ".tooltip";
        if (I18n.hasKey(key)) {
            // Description tooltips
            tooltip.add(I18n.format(key));
        }

        tooltip.addAll(getEmblemTooltip(stack));
        if (getEmblemType().equals(EmblemType.MANUAL)) {
            int uses = (stack.getMaxDamage() + 1) - stack.getMetadata();
            tooltip.add(I18n.format("tooltip.experienceplus.remaining_uses", uses));
        }

    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return isEmblemEnabled(stack);
    }

    public boolean isEmblemEnabled(ItemStack stack) {
        return  !getEmblemType().equals(EmblemType.TOGGLEABLE)
                || ((stack.hasTagCompound()
                && stack.getTagCompound() != null
                && stack.getTagCompound().getBoolean("enabled")));
    }

    @SideOnly(Side.CLIENT)
    private List<String> getEmblemTooltip(ItemStack stack) {
        List<String> tooltip = new ArrayList<>();
        if (stack.hasTagCompound() && stack.getTagCompound() != null) {
            if (stack.getTagCompound().getBoolean("activated")) {
                boolean enabled = stack.getTagCompound().getBoolean("enabled");
                tooltip.add(I18n.format("tooltip.experienceplus." + (enabled ? "disable" : "enable")));
            } else {
                tooltip.add(I18n.format("tooltip.experienceplus.activate", getLevelCost()));
            }
        }
        return tooltip;
    }

    public abstract EmblemType getEmblemType();

    public abstract int getLevelCost();

    public boolean onUseEmblem(World world, EntityPlayer player) {
        return false;
    }

    public void onEmblemTick(ItemStack stack, EntityPlayer player) {
        // no-op
    }

    private boolean toggleEmblemState(World world, EntityPlayer player, EnumHand hand) {
        if (getEmblemType().equals(EmblemType.TOGGLEABLE)) {
            ItemStack stack = player.getHeldItem(hand);
            if (stack.hasTagCompound() && stack.getTagCompound() != null) {
                NBTTagCompound nbt = stack.getTagCompound();
                if (nbt.getBoolean("activated")) {
                    boolean state = nbt.getBoolean("enabled");
                    world.playSound(null, player.posX, player.posY, player.posZ,
                            SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.PLAYERS,
                            0.5F, (state ? 0.1F : 0.8F));
                    nbt.setBoolean("enabled", !state);
                } else {
                    if (tryActivateEmblem(world, player, hand)) {
                        nbt.setBoolean("activated", true);
                    } else {
                        player.sendStatusMessage(new TextComponentTranslation(
                                "message.experienceplus.no_experience"), true);
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean tryActivateEmblem(World world, EntityPlayer player, EnumHand hand) {
        int expCost = ExpLevelHelper.getExperienceForLevel(getLevelCost());
        if (ExpLevelHelper.getPlayerExp(player) >= expCost) {
            if (onUseEmblem(world, player)) {
                ExpLevelHelper.removePlayerExp(player, expCost);
                playActivationSound(world, player);
                createActivationAura(world, player);
                if (getEmblemType().equals(EmblemType.MANUAL)) {
                    player.getHeldItem(hand).damageItem(1, player);
                }
                return true;
            }
        }
        return false;
    }

    private static void playActivationSound(World world, EntityPlayer player) {
        SoundEvent sound = SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE;
        world.playSound(null, player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, 2.0F, 0.2F);
    }

    private static void createActivationAura(World world, EntityPlayer player) {
        BlockPos min = player.getPosition().subtract(new Vec3i(1, 0, 1));
        BlockPos max = player.getPosition().add(new Vec3i(1, 0, 1));
        Iterable<BlockPos> area = BlockPos.getAllInBox(min, max);
        for (BlockPos pos : area) {
            double x = pos.getX(), y = pos.getY(), z = pos.getZ();
            spawnAreaParticle(world, EnumParticleTypes.PORTAL, new Random(), x, y + 1.5, z);
            spawnAreaParticle(world, EnumParticleTypes.PORTAL, new Random(), x, y + 0.5, z);
        }
    }

    private static void spawnAreaParticle(World world, EnumParticleTypes particle, Random random, double x, double y, double z) {
        world.spawnParticle(particle, x - 0.5, y, z, -(random.nextFloat() % 2), 0, 0);
        world.spawnParticle(particle, x, y, z - 0.5, 0, 0, -(random.nextFloat() % 2));
        world.spawnParticle(particle, x - 0.5, y, z - 0.5, -(random.nextFloat() % 2), 0, -(random.nextFloat() % 2));
        world.spawnParticle(particle, x, y, z, 0, 0, 0);
        world.spawnParticle(particle, x + 0.5, y, z + 0.5, (random.nextFloat() % 2), 0, (random.nextFloat() % 2));
        world.spawnParticle(particle, x, y, z + 0.5, 0, 0, (random.nextFloat() % 2));
        world.spawnParticle(particle, x + 0.5, y, z, (random.nextFloat() % 2), 0, 0);
    }

    public enum EmblemType {
        MANUAL, TOGGLEABLE
    }

}

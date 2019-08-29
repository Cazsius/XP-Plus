package com.cazsius.xpplus.emblem;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class EmblemBridging extends ItemEmblem {

    private static final BlockPos.MutableBlockPos POS = new BlockPos.MutableBlockPos();
    private static final BlockState MATERIAL = Blocks.COBBLESTONE.getDefaultState();

    private final int cost;

    public EmblemBridging(Properties properties, int cost) {
        super(properties);
        this.cost = cost;
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
        tooltip.add(new StringTextComponent(I18n.format("tooltip.experienceplus.remaining_uses", uses)));
        super.addInformation(stack, world, tooltip, flag);
    }

    @Deprecated
    public void onEmblemTick(ItemStack stack, PlayerEntity player, Entity entity, BlockItemUseContext context) {
        if (!isEmblemEnabled(stack)) return;

        POS.setPos(player.posX, player.posY - 1, player.posZ);
        BlockPos min = POS.subtract(new Vec3i(1, 0, 1));
        BlockPos max = POS.add(new Vec3i(1, 0, 1));
        boolean hasPlacedBlocks = false;

        for (BlockPos target : BlockPos.getAllInBoxMutable(min, max)) {
            BlockState stateAt = player.world.getBlockState(target);
            if (stateAt.getBlock().isReplaceable(stateAt, context)) {
                hasPlacedBlocks = true;
                if (!player.world.isRemote) {
                    player.world.setBlockState(target, MATERIAL);
                    stack.damageItem(1, player,l ->l.sendBreakAnimation(EquipmentSlotType.MAINHAND));
                }
            }
        }

        if (hasPlacedBlocks) {
            SoundType soundtype = MATERIAL.getBlock().getSoundType(MATERIAL, player.world, POS, player);
            player.world.playSound(null, POS, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
                    (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        }

    }

}
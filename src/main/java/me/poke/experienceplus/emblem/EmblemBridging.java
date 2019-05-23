package me.poke.experienceplus.emblem;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EmblemBridging extends ItemEmblem {

    private static final BlockPos.MutableBlockPos POS = new BlockPos.MutableBlockPos();
    private static final IBlockState MATERIAL = Blocks.COBBLESTONE.getDefaultState();

    private final int cost;

    public EmblemBridging(String name, int cost) {
        super(name);
        this.cost = cost;
        setMaxDamage(1023);
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
        int uses = (stack.getMaxDamage() + 1) - stack.getItemDamage();
        tooltip.add(I18n.format("tooltip.experienceplus.remaining_uses", uses));
        super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public void onEmblemTick(ItemStack stack, EntityPlayer player) {
        if (!isEmblemEnabled(stack) || player.isRiding()) return;

        POS.setPos(player.posX, player.posY - 1, player.posZ);
        BlockPos min = POS.subtract(new Vec3i(1, 0, 1));
        BlockPos max = POS.add(new Vec3i(1, 0, 1));
        boolean hasPlacedBlocks = false;

        for (BlockPos.MutableBlockPos target : BlockPos.getAllInBoxMutable(min, max)) {
            IBlockState stateAt = player.world.getBlockState(target);
            if (stateAt.getBlock().isReplaceable(player.world, target)) {
                hasPlacedBlocks = true;
                if (!player.world.isRemote) {
                    player.world.setBlockState(target, MATERIAL);
                    stack.damageItem(1, player);
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

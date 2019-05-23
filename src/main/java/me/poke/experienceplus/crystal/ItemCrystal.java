package me.poke.experienceplus.crystal;

import me.poke.experienceplus.ExperiencePlus;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemCrystal extends Item {

    public ItemCrystal(String name) {
        setRegistryName("crystal_" + name);
        setTranslationKey("crystal_" + name);
        //setRegistryName(ExperiencePlus.MOD_ID + ".crystal_" + name);
        setCreativeTab(ExperiencePlus.CTAB);
    }

    @Override @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        String key = stack.getItem().getRegistryName() + ".tooltip";
        if (I18n.hasKey(key)) {
            tooltip.add(I18n.format(key));
        }
    }

}

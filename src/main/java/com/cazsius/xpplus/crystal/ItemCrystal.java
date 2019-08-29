package com.cazsius.xpplus.crystal;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCrystal extends Item {

    public ItemCrystal(Properties properties) {
    super(properties);
    }

    @Override @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        String key = stack.getTranslationKey() + ".tooltip";
        if (I18n.hasKey(key)) {
            tooltip.add(new StringTextComponent(I18n.format(key)));
        }
    }

}
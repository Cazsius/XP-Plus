package com.cazsius.xpplus.util;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class XPGroup extends ItemGroup {

    public static final XPGroup instance = new XPGroup(ItemGroup.GROUPS.length, "xpplus");

    private XPGroup(int index, String label)
    {
        super(index, label);
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(XPItems.XPICON);
    }


}

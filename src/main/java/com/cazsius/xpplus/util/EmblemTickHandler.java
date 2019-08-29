package com.cazsius.xpplus.util;

import com.cazsius.xpplus.emblem.ItemEmblem;
import com.google.common.base.Equivalence;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EmblemTickHandler {

    private static final Equivalence<ItemStack> STACK_EQUIVALENCE = new Equivalence<ItemStack>() {

        @Override
        protected boolean doEquivalent(ItemStack a, ItemStack b) {
            return ItemStack.areItemsEqualIgnoreDurability(a, b);
        }

        @Override
        protected int doHash(ItemStack stack) {
            int result = stack.getItem().getRegistryName().hashCode();
            result = 31 * result + stack.getMaxDamage();
            result = 31 * result + (stack.hasTag() ?
                    stack.getTag().hashCode() : 0);
            return result;
        }

    };

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END ||event.side != LogicalSide.SERVER) {
            return;
        }
        NonNullList<Equivalence.Wrapper<ItemStack>> cache = NonNullList.create();
        for (ItemStack stack : event.player.inventory.mainInventory) {
            if (stack.getItem() instanceof ItemEmblem) {
                Equivalence.Wrapper<ItemStack> target = STACK_EQUIVALENCE.wrap(stack);
                if (!cache.contains(target)) {
                    cache.add(target);

                }
            }
        }

        for (Equivalence.Wrapper<ItemStack> stack : cache) {

            ItemEmblem emblem = (ItemEmblem) stack.get().getItem();
            emblem.onEmblemTick(stack.get(), event.player, null);
        }

    }

}
package me.poke.experienceplus.util;

import com.google.common.base.Equivalence;
import com.google.common.base.Equivalence.Wrapper;
import me.poke.experienceplus.ExperiencePlus;
import me.poke.experienceplus.emblem.ItemEmblem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = ExperiencePlus.MOD_ID)
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
            result = 31 * result + (stack.hasTagCompound() ?
                    stack.getTagCompound().hashCode() : 0);
            return result;
        }

    };

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.side != Side.SERVER) {
            return;
        }

        NonNullList<Wrapper<ItemStack>> cache = NonNullList.create();

        for (ItemStack stack : event.player.inventory.mainInventory) {
            if (stack.getItem() instanceof ItemEmblem) {
                Wrapper<ItemStack> target = STACK_EQUIVALENCE.wrap(stack);
                if (!cache.contains(target)) {
                    cache.add(target);
                }
            }
        }

        for (Wrapper<ItemStack> stack : cache) {
            ItemEmblem emblem = (ItemEmblem) stack.get().getItem();
            emblem.onEmblemTick(stack.get(), event.player);
        }

    }

}

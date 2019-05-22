package me.poke.experienceplus;

import me.poke.experienceplus.crystal.ItemCrystal;
import me.poke.experienceplus.emblem.*;
import me.poke.experienceplus.emblem.EmblemTime.TimeStage;
import me.poke.experienceplus.emblem.EmblemWeather.WeatherType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RegistryManager {

    public enum ModItems {
        // Crystals
        CRYSTAL_BASIC(new ItemCrystal("basic")),
        CRYSTAL_ADVANCED(new ItemCrystal("advanced")),
        // Healing Emblem
        EMBLEM_HEALING(new EmblemHealing("emblem_healing", 15, 5.0f)),
        // Bridging Emblem
        //EMBLEM_BRIDGING(new EmblemBridging("emblem_bridging", 30)),
        // Time Emblems
        EMBLEM_DAY(new EmblemTime("emblem_day", 15, TimeStage.DAY)),
        EMBLEM_NIGHT(new EmblemTime("emblem_night", 15, TimeStage.NIGHT)),
        // Weather Emblems
        EMBLEM_CLEAR(new EmblemWeather("emblem_clear", 10, WeatherType.CLEAR)),
        EMBLEM_RAIN(new EmblemWeather("emblem_rain", 10, WeatherType.RAIN)),
        EMBLEM_THUNDER(new EmblemWeather("emblem_thunder", 10, WeatherType.THUNDER)),
        // Effect Emblems
        //EMBLEM_GLOWING(new EmblemEffect("emblem_glowing", 30, MobEffects.GLOWING, 0)),
        EMBLEM_HASTE(new EmblemEffect("emblem_haste", 30, MobEffects.HASTE, 0)),
        EMBLEM_JUMP(new EmblemEffect("emblem_jump", 30, MobEffects.JUMP_BOOST, 0)),
        EMBLEM_RESISTANCE(new EmblemEffect("emblem_resistance", 30, MobEffects.RESISTANCE, 0)),
        EMBLEM_SPEED(new EmblemEffect("emblem_speed", 30, MobEffects.SPEED, 0)),
        EMBLEM_STRENGTH(new EmblemEffect("emblem_strength", 30, MobEffects.STRENGTH, 0)),;

        private final Item item;

        ModItems(Item item) {
            this.item = item;
        }

        public Item get() {
            return item;
        }
    }

    @Mod.EventBusSubscriber(modid = ExperiencePlus.MOD_ID)
    public static class ItemRegistry {

        @SubscribeEvent
        public static void onItemRegistry(RegistryEvent.Register<Item> event) {
            for (ModItems item : ModItems.values()) {
                event.getRegistry().register(item.get());
            }
        }

    }

    @SideOnly(Side.CLIENT)
    @Mod.EventBusSubscriber(modid = ExperiencePlus.MOD_ID, value = Side.CLIENT)
    public static class ModelRegistry {

        @SubscribeEvent
        public static void onModelRegistry(ModelRegistryEvent event) {
            for (ModItems item : ModItems.values()) {
                registerItemModel(item.get());
            }
        }

        private static void registerItemModel(Item item) {
            ModelResourceLocation mrl = new ModelResourceLocation(item.getRegistryName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(item, 0, mrl);
        }

    }

    @Mod.EventBusSubscriber(modid = ExperiencePlus.MOD_ID)
    public static class RecipeRegistry {

        @SubscribeEvent
        public static void onRecipeRegistry(RegistryEvent.Register<IRecipe> event) {
            /*// Crystals
            registerCrystalRecipe(ModItems.CRYSTAL_ADVANCED.get(), Items.NETHER_STAR);
            registerCrystalRecipe(ModItems.CRYSTAL_BASIC.get(), Items.EMERALD);
            // Time Emblems
            registerAdvancedRecipe(ModItems.EMBLEM_DAY.get(), Items.BLAZE_POWDER, Blocks.GLOWSTONE);
            registerAdvancedRecipe(ModItems.EMBLEM_NIGHT.get(), Items.ENDER_PEARL, Blocks.END_STONE);
            // Weather Emblems
            registerBasicRecipe(ModItems.EMBLEM_CLEAR.get(), new ItemStack(Blocks.DOUBLE_PLANT, 1, 0), Blocks.GRASS);
            registerBasicRecipe(ModItems.EMBLEM_RAIN.get(), new ItemStack(Blocks.SPONGE, 1, 1), Items.WATER_BUCKET);
            registerBasicRecipe(ModItems.EMBLEM_THUNDER.get(), Blocks.SOUL_SAND, Blocks.END_ROD);
            // Effect Emblems
            registerBasicRecipe(ModItems.EMBLEM_GLOWING.get(), Blocks.REDSTONE_LAMP, Blocks.REDSTONE_BLOCK);
            registerBasicRecipe(ModItems.EMBLEM_HASTE.get(), Items.SUGAR, withEnchantment(new ItemStack(Items.DIAMOND_PICKAXE), Enchantments.EFFICIENCY, 4));
            registerAdvancedRecipe(ModItems.EMBLEM_HEALING.get(), Items.GOLDEN_APPLE, Items.GOLDEN_APPLE);
            registerBasicRecipe(ModItems.EMBLEM_JUMP.get(), Items.RABBIT, Blocks.SLIME_BLOCK);
            registerAdvancedRecipe(ModItems.EMBLEM_RESISTANCE.get(), Blocks.IRON_BLOCK, Blocks.IRON_BLOCK);
            registerBasicRecipe(ModItems.EMBLEM_SPEED.get(), Items.SUGAR, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.SWIFTNESS));
            registerAdvancedRecipe(ModItems.EMBLEM_STRENGTH.get(), Items.BLAZE_POWDER, Items.BLAZE_ROD);*/

        }

        private static void registerCrystalRecipe(Item crystal, Object core) {
            GameRegistry.addShapedRecipe(
                    crystal.getRegistryName(), null, new ItemStack(crystal),
                    " O ", "OCO", " O ",
                    'C', core, 'O', Blocks.GLASS_PANE
            );
        }

        private static void registerBasicRecipe(Item emblem, Object top, Object bottom) {
            GameRegistry.addShapedRecipe(
                    emblem.getRegistryName(), null, new ItemStack(emblem),
                    "IAI", "ICI", "IBI",
                    'I', Items.GOLD_INGOT,
                    'A', top, 'B', bottom,
                    'C', ModItems.CRYSTAL_BASIC.get()
            );
        }

        private static void registerAdvancedRecipe(Item emblem, Object top, Object bottom) {
            GameRegistry.addShapedRecipe(
                    emblem.getRegistryName(), null, new ItemStack(emblem),
                    "IAI", "ICI", "IBI",
                    'I', Items.DIAMOND,
                    'A', top, 'B', bottom,
                    'C', ModItems.CRYSTAL_ADVANCED.get()
            );
        }

        private static ItemStack withEnchantment(ItemStack stack, Enchantment enchantment, int level) {
            stack.addEnchantment(enchantment, level);
            return stack;
        }

    }

}

package com.cazsius.xpplus.util;


import com.cazsius.xpplus.crystal.ItemCrystal;
import com.cazsius.xpplus.emblem.*;
import net.minecraft.item.Item;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class XPItems {


    public static Item XPICON, CRYSTAL_BASIC,CRYSTAL_ADVANCED,EMBLEM_HEALING,BRIDGING_EMBLEM,EMBLEM_DAY,EMBLEM_CLEAR,EMBLEM_RAIN,EMBLEM_THUNDER,EMBLEM_GLOWING,EMBLEM_HASTE,EMBLEM_JUMP,EMBLEM_RESISTANCE,EMBLEM_NIGHT,EMBLEM_SPEED,EMBLEM_STRENGTH;


    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event)
    {
                CRYSTAL_BASIC = registerItem(new ItemCrystal(new Item.Properties().maxStackSize(16).group(XPGroup.instance)),"basic");
                CRYSTAL_ADVANCED = registerItem(new ItemCrystal(new Item.Properties().maxStackSize(16).group(XPGroup.instance)),"advanced");
                // Healing Emblem
                EMBLEM_HEALING = registerItem(new EmblemHealing(new Item.Properties().maxStackSize(1).group(XPGroup.instance).maxDamage(15), 15, 5.0f), "emblem_healing");
                // Bridging Emblem
                BRIDGING_EMBLEM = registerItem(new EmblemBridging(new Item.Properties().maxStackSize(1).group(XPGroup.instance).maxDamage(1023),30), "emblem_bridging");
                // Time Emblems
                EMBLEM_DAY = registerItem(new EmblemTime(new Item.Properties().maxStackSize(1).group(XPGroup.instance).maxDamage(7), 15, EmblemTime.TimeStage.DAY), "emblem_day");
                EMBLEM_NIGHT = registerItem(new EmblemTime(new Item.Properties().maxStackSize(1).group(XPGroup.instance).maxDamage(7), 15, EmblemTime.TimeStage.NIGHT), "emblem_night");
                // Weather Emblems
                EMBLEM_CLEAR = registerItem(new EmblemWeather(new Item.Properties().maxStackSize(1).group(XPGroup.instance).maxDamage(7), 10, EmblemWeather.WeatherType.CLEAR), "emblem_clear");
                EMBLEM_RAIN = registerItem(new EmblemWeather(new Item.Properties().maxStackSize(1).group(XPGroup.instance).maxDamage(7), 10, EmblemWeather.WeatherType.RAIN), "emblem_rain");
                EMBLEM_THUNDER = registerItem(new EmblemWeather(new Item.Properties().maxStackSize(1).group(XPGroup.instance).maxDamage(7), 10, EmblemWeather.WeatherType.THUNDER), "emblem_thunder");
                // Effect Emblems
                EMBLEM_GLOWING = registerItem(new EmblemEffect(new Item.Properties().maxStackSize(1).group(XPGroup.instance).maxDamage(1799), 30, Effects.GLOWING.getEffect(), 10),"emblem_glowing");
                EMBLEM_HASTE = registerItem(new EmblemEffect(new Item.Properties().maxStackSize(1).group(XPGroup.instance).maxDamage(1799), 30, Effects.HASTE.getEffect(), 10),"emblem_haste");
                EMBLEM_JUMP = registerItem(new EmblemEffect(new Item.Properties().maxStackSize(1).group(XPGroup.instance).maxDamage(1799), 30, Effects.JUMP_BOOST.getEffect(), 10),"emblem_jump");
                EMBLEM_RESISTANCE = registerItem(new EmblemEffect(new Item.Properties().maxStackSize(1).group(XPGroup.instance).maxDamage(1799), 30, Effects.RESISTANCE.getEffect(), 10),"emblem_resistance");
                EMBLEM_SPEED = registerItem(new EmblemEffect(new Item.Properties().maxStackSize(1).group(XPGroup.instance).maxDamage(1799), 30, Effects.SPEED.getEffect(), 10),"emblem_speed");
                EMBLEM_STRENGTH = registerItem(new EmblemEffect(new Item.Properties().maxStackSize(1).group(XPGroup.instance).maxDamage(1799), 30, Effects.STRENGTH.getEffect(), 10),"emblem_strength");



        XPICON = registerItem(new Item(new Item.Properties()), "xpicon");

    }

    public static Item registerItem(Item item, String name)
    {
        item.setRegistryName(name);
        ForgeRegistries.ITEMS.register(item);
        return item;
    }
}
package me.poke.experienceplus;

import me.poke.experienceplus.RegistryManager.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

@Mod(   modid = ExperiencePlus.MOD_ID,
        name = ExperiencePlus.MOD_NAME,
        version = ExperiencePlus.MOD_VERSION,
        acceptedMinecraftVersions = ExperiencePlus.MC_VERSION)

public class ExperiencePlus {

    @Mod.Instance
    public static ExperiencePlus instance;

    public static final String MOD_ID = "experienceplus";
    public static final String MOD_NAME = "Experience Plus";
    public static final String MOD_VERSION = "1.02";
    public static final String MC_VERSION = "[1.12,1.13)";

    public static final CreativeTabs CTAB = new CreativeTabs(ExperiencePlus.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.CRYSTAL_ADVANCED.get());
        }
    };

}

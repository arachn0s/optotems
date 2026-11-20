package com.arachnos.optotems.item;

import com.arachnos.optotems.OpTotems;
import com.arachnos.optotems.item.custom.totem_of_the_ghost;
import com.arachnos.optotems.item.custom.totem_of_thunder;
import com.arachnos.optotems.item.custom.totem_of_wolfgang;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DeathProtection;

import java.util.function.Function;

public class ModItems {
    public static final Item TOTEM_OF_WOLFGANG = registerItem("totem_of_wolfgang",
            properties -> new totem_of_wolfgang(properties.component(
                    DataComponents.DEATH_PROTECTION, DeathProtection.TOTEM_OF_UNDYING)));
    public static final Item TOTEM_OF_THUNDER = registerItem("totem_of_thunder",
            properties -> new totem_of_thunder(properties.component(
                    DataComponents.DEATH_PROTECTION, DeathProtection.TOTEM_OF_UNDYING)));
    public static final Item TOTEM_OF_THE_GHOST = registerItem("totem_of_the_ghost",
            properties -> new totem_of_the_ghost(properties.component(
                    DataComponents.DEATH_PROTECTION, DeathProtection.TOTEM_OF_UNDYING)));
    private static Item registerItem(String name, Function<Item.Properties, Item> function){
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(OpTotems.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(OpTotems.MOD_ID, name)))));
    }
    public static void registerModItems(){
        OpTotems.LOGGER.info("Registering Mod Items " + OpTotems.MOD_ID);
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(output ->{
                output.accept(TOTEM_OF_WOLFGANG);
                output.accept(TOTEM_OF_THUNDER);
                output.accept(TOTEM_OF_THE_GHOST);
        });
    }

}

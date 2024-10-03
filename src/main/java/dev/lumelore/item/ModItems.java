package dev.lumelore.item;

import dev.lumelore.Enderbook;
import dev.lumelore.item.special.EnderbookItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public abstract class ModItems {

    public static final Item ENDERBOOK = registerItem("enderbook", new EnderbookItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.UNCOMMON)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Enderbook.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Enderbook.LOGGER.info("Registering items for the " + Enderbook.MOD_ID + " mod...");
    }

}

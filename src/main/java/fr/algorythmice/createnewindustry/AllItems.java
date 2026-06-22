package fr.algorythmice.createnewindustry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class AllItems {
    private static final CreateRegistrate REGISTRATE =
            CreateNewIndustry.REGISTRATE;

    public static final ItemEntry<Item> WHISK =
            REGISTRATE.item("centrifuge_blades", Item::new)
                    .register();

    public static void register() {}
}
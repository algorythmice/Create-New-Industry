package fr.algorythmice.createnewindustry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class AllCreativeModeTabs {

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateNewIndustry.ID);

    public static DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB;

    static {
        MAIN_TAB = TABS.register("main", () ->
                CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup.createnewindustry.base"))
                        .icon(AllBlocks.MECHANICAL_CENTRIFUGE::asStack)
                        .displayItems(new RegistrateDisplayItemsGenerator(true, MAIN_TAB))
                        .build()
        );
    }

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }

    private static class RegistrateDisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {

        private final boolean addItems;
        private final DeferredHolder<CreativeModeTab, CreativeModeTab> tabFilter;

        public RegistrateDisplayItemsGenerator(boolean addItems, DeferredHolder<CreativeModeTab, CreativeModeTab> tabFilter) {
            this.addItems = addItems;
            this.tabFilter = tabFilter;
        }

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
            List<Item> items = new ArrayList<>();

            if (addItems) {
                items.addAll(collectItems());
            }

            items.addAll(collectBlocks());

            for (Item item : new LinkedHashSet<>(items)) {
                output.accept(new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }

        private List<Item> collectBlocks() {
            List<Item> items = new ArrayList<>();

            for (RegistryEntry<Block, Block> entry : CreateNewIndustry.registrate().getAll(Registries.BLOCK)) {
                if (!CreateRegistrate.isInCreativeTab(entry, tabFilter))
                    continue;

                Item item = entry.get().asItem();
                if (item == Items.AIR)
                    continue;

                items.add(item);
            }

            return items;
        }

        private List<Item> collectItems() {
            List<Item> items = new ArrayList<>();

            for (RegistryEntry<Item, Item> entry : CreateNewIndustry.registrate().getAll(Registries.ITEM)) {
                if (!CreateRegistrate.isInCreativeTab(entry, tabFilter))
                    continue;

                Item item = entry.get();
                if (item instanceof BlockItem)
                    continue;

                items.add(item);
            }

            return items;
        }
    }
}
package fr.algorythmice.createnewindustry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(CreateNewIndustry.ID)
public class CreateNewIndustry {

    public static final String ID = "createnewindustry";
    public static final String NAME = "Create New Industry";

    private static final StackWalker STACK_WALKER =
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public static final CreateRegistrate REGISTRATE =
            CreateRegistrate.create(ID)
                    .defaultCreativeTab((ResourceKey<CreativeModeTab>) null);

    public CreateNewIndustry(IEventBus modEventBus, ModContainer modContainer) {
        onCtor(modEventBus, modContainer);
    }

    public static void onCtor(IEventBus modEventBus, ModContainer modContainer) {

        AllCreativeModeTabs.register(modEventBus);
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.MAIN_TAB);
        REGISTRATE.registerEventListeners(modEventBus);

        AllBlocks.register();
        AllItems.register();
        AllBlockEntityTypes.register();
        AllRecipeTypes.register(modEventBus);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    public static CreateRegistrate registrate() {
        if (!STACK_WALKER.getCallerClass().getPackageName()
                .startsWith("fr.algorythmice.createnewindustry")) {
            throw new UnsupportedOperationException(
                    "Other mods are not permitted to use registrate instance.");
        }
        return REGISTRATE;
    }
}
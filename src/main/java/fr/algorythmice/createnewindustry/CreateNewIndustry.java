package fr.algorythmice.createnewindustry;

import com.simibubi.create.CreateBuildInfo;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.level.LevelEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(CreateNewIndustry.ID)
public class CreateNewIndustry {

    public static final String ID = "createnewindustry";
    public static final String NAME = "Create New Industry";

    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static final CreateRegistrate REGISTRATE =
            CreateRegistrate.create(ID);

    public CreateNewIndustry(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("{} initializing! {}", NAME, CreateBuildInfo.VERSION);
        NeoForge.EVENT_BUS.register(CreateNewIndustry.class);
        NeoForge.EVENT_BUS.addListener((LevelEvent.Load event) -> {
            if (!(event.getLevel() instanceof ServerLevel level)) return;

            var recipes = level.getRecipeManager().getAllRecipesFor(AllRecipeTypes.CENTRIFUGE.getType());

            LOGGER.info("[CENTRIFUGE] recipes loaded = {}", recipes.size());

            for (var r : recipes) {
                LOGGER.info("[CENTRIFUGE] {}", r.id());
            }
        });
        onCtor(modEventBus, modContainer);
    }

    public static void onCtor(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modEventBus);

        AllBlocks.register();
        AllBlockEntityTypes.register();
        AllRecipeTypes.register(modEventBus);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {

        Level level = (Level) event.getLevel();
        if (level.isClientSide()) return;

        var manager = level.getRecipeManager();
        var list = manager.getAllRecipesFor(AllRecipeTypes.CENTRIFUGE.getType());

        System.out.println("\n[CENTRIFUGE DEBUG] Loaded recipes: " + list.size());

        for (var r : list) {
            System.out.println("[CENTRIFUGE DEBUG] " + r.id());
        }
    }
}
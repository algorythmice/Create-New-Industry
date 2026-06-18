package fr.algorythmice.createnewindustry;

import fr.algorythmice.createnewindustry.foundation.ponder.CreateNewIndustryPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = CreateNewIndustry.ID, dist = Dist.CLIENT)
public class CreateNewIndustryClient {
    public CreateNewIndustryClient(IEventBus modEventBus) {
        onCtorClient(modEventBus);
    }

    public static void onCtorClient(IEventBus modEventBus) {
        modEventBus.addListener(CreateNewIndustryClient::clientInit);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            PonderIndex.addPlugin(new CreateNewIndustryPonderPlugin());
        });
    }

}

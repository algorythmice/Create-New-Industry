package fr.algorythmice.createnewindustry.infrastructure.ponder;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import fr.algorythmice.createnewindustry.AllBlocks;

import fr.algorythmice.createnewindustry.infrastructure.ponder.scenes.ProcessingScenes;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class AllCreateNewIndustryPonderScenes {


    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.addStoryBoard(AllBlocks.MECHANICAL_CENTRIFUGE,"mechanical_centrifuge/centrifuge", ProcessingScenes::mixing);

    }
}

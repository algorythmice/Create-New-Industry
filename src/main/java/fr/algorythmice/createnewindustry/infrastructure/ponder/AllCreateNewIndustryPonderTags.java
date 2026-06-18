package fr.algorythmice.createnewindustry.infrastructure.ponder;

import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.tterrag.registrate.util.entry.RegistryEntry;
import fr.algorythmice.createnewindustry.AllBlocks;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class AllCreateNewIndustryPonderTags {

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {

        PonderTagRegistrationHelper<RegistryEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.addToTag(AllCreatePonderTags.KINETIC_APPLIANCES)
                .add(AllBlocks.MECHANICAL_CENTRIFUGE);

    }
}

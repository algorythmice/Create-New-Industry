package fr.algorythmice.createnewindustry.foundation.ponder;

import fr.algorythmice.createnewindustry.CreateNewIndustry;
import fr.algorythmice.createnewindustry.infrastructure.ponder.AllCreateNewIndustryPonderTags;
import fr.algorythmice.createnewindustry.infrastructure.ponder.AllCreateNewIndustryPonderScenes;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class CreateNewIndustryPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return CreateNewIndustry.ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        AllCreateNewIndustryPonderScenes.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        AllCreateNewIndustryPonderTags.register(helper);
    }
}

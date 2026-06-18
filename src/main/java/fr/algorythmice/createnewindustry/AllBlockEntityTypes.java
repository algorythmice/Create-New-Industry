package fr.algorythmice.createnewindustry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import fr.algorythmice.createnewindustry.content.kinetics.centrifuge.CentrifugeVisual;
import fr.algorythmice.createnewindustry.content.kinetics.centrifuge.MechanicalCentrifugeBlockEntity;
import fr.algorythmice.createnewindustry.content.kinetics.centrifuge.MechanicalCentrifugeRenderer;

public class AllBlockEntityTypes {
    private static final CreateRegistrate REGISTRATE =
            CreateNewIndustry.REGISTRATE;


    public static final BlockEntityEntry<MechanicalCentrifugeBlockEntity> MECHANICAL_CENTRIFUGE = REGISTRATE
            .blockEntity("mechanical_centrifuge", MechanicalCentrifugeBlockEntity::new)
            .visual(() -> CentrifugeVisual::new)
            .validBlocks(AllBlocks.MECHANICAL_CENTRIFUGE)
            .renderer(() -> MechanicalCentrifugeRenderer::new)
            .register();

    public static void register() {
    }
}

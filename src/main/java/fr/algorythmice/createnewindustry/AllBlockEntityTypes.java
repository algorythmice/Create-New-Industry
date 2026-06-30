package fr.algorythmice.createnewindustry;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import fr.algorythmice.createnewindustry.content.kinetics.centrifuge.CentrifugeVisual;
import fr.algorythmice.createnewindustry.content.kinetics.centrifuge.MechanicalCentrifugeBlockEntity;
import fr.algorythmice.createnewindustry.content.kinetics.centrifuge.MechanicalCentrifugeRenderer;
import fr.algorythmice.createnewindustry.content.kinetics.flywheel.FlywheelBlockEntity;
import fr.algorythmice.createnewindustry.content.kinetics.flywheel.FlywheelPartBlockEntity;

public class AllBlockEntityTypes {
    private static final CreateRegistrate REGISTRATE =
            CreateNewIndustry.REGISTRATE;


    public static final BlockEntityEntry<MechanicalCentrifugeBlockEntity> MECHANICAL_CENTRIFUGE = REGISTRATE
            .blockEntity("mechanical_centrifuge", MechanicalCentrifugeBlockEntity::new)
            .visual(() -> CentrifugeVisual::new)
            .validBlocks(AllBlocks.MECHANICAL_CENTRIFUGE)
            .renderer(() -> MechanicalCentrifugeRenderer::new)
            .register();
    public static final BlockEntityEntry<FlywheelBlockEntity> FLYWHEEL = REGISTRATE
            .blockEntity("flywheel", FlywheelBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual.of(AllPartialModels.FLYWHEEL_WHEEL), false)
            .renderer(() -> KineticBlockEntityRenderer::new)
            .validBlocks(AllBlocks.FLYWHEEL)
            .register();

    public static final BlockEntityEntry<FlywheelPartBlockEntity> FLYWHEEL_PART =
            REGISTRATE
                    .blockEntity("flywheel_part", FlywheelPartBlockEntity::new)
                    .validBlocks(AllBlocks.FLYWHEEL_PART)
                    .register();

    public static void register() {
    }
}

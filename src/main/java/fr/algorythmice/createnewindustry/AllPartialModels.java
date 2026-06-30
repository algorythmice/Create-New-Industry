package fr.algorythmice.createnewindustry;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;


public class AllPartialModels {

    public static final PartialModel
            MECHANICAL_CENTRIFUGE_POLE = block("mechanical_centrifuge/pole"),
            MECHANICAL_CENTRIFUGE_HEAD = block("mechanical_centrifuge/head"),
            FLYWHEEL_WHEEL = block("flywheel/block");

    private static PartialModel block(String path) {
        return PartialModel.of(CreateNewIndustry.asResource("block/" + path));
    }

    public static void init() {
        // init static fields
    }
}

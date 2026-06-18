package fr.algorythmice.createnewindustry;

import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import fr.algorythmice.createnewindustry.content.kinetics.centrifuge.MechanicalCentrifugeBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.MapColor;


import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

@SuppressWarnings("removal")
public class AllBlocks {
    private static final CreateRegistrate REGISTRATE =
            CreateNewIndustry.REGISTRATE;

    public static final BlockEntry<MechanicalCentrifugeBlock> MECHANICAL_CENTRIFUGE =
            REGISTRATE.block("mechanical_centrifuge", MechanicalCentrifugeBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.STONE))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(AssemblyOperatorBlockItem::new)
                    .transform(customItemModel())
                    .register();

    public static void register() {
    }
}

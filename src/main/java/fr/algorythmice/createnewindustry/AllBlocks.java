package fr.algorythmice.createnewindustry;

import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import fr.algorythmice.createnewindustry.content.kinetics.centrifuge.MechanicalCentrifugeBlock;
import fr.algorythmice.createnewindustry.content.kinetics.flywheel.FlywheelBlock;
import fr.algorythmice.createnewindustry.content.kinetics.flywheel.FlywheelPartBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

@SuppressWarnings("removal")
public class AllBlocks {
    private static final CreateRegistrate REGISTRATE =
            CreateNewIndustry.REGISTRATE;

    public static final BlockEntry<MechanicalCentrifugeBlock> MECHANICAL_CENTRIFUGE =

            REGISTRATE
                    .setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item))))
                    .block("mechanical_centrifuge", MechanicalCentrifugeBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .transform(axeOrPickaxe())
                    .onRegister((block) -> BlockStressValues.IMPACTS.register(block, () ->5f))
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.STONE))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(AssemblyOperatorBlockItem::new)
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<FlywheelBlock> FLYWHEEL =
            REGISTRATE
                    .setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item))))
                    .block("flywheel", FlywheelBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .transform(axeOrPickaxe())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .onRegister((block) -> {
                        BlockStressValues.IMPACTS.register(block, () -> 5f);
                        BlockStressValues.CAPACITIES.register(block, () -> 32f);
                    })
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.METAL))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> BlockStateGen.axisBlock(c, p, s -> AssetLookup.partialBaseModel(c, p)))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(AssemblyOperatorBlockItem::new)
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<FlywheelPartBlock> FLYWHEEL_PART =
            REGISTRATE
                    .block("flywheel_part", FlywheelPartBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .transform(axeOrPickaxe())
                    .properties(p -> p.noOcclusion()
                            .noLootTable())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate((c, p) -> {})
                    .register();

    public static void register() {
    }
}

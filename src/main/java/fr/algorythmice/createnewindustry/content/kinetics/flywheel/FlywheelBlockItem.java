package fr.algorythmice.createnewindustry.content.kinetics.flywheel;

import net.createmod.catnip.data.Pair;
import net.createmod.catnip.outliner.Outliner;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class FlywheelBlockItem extends BlockItem {

    public FlywheelBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);
        if (result != InteractionResult.FAIL)
            return result;

        Direction clickedFace = context.getClickedFace();
        if (clickedFace.getAxis() != ((FlywheelBlock) getBlock()).getAxisForPlacement(context))
            result = super.place(BlockPlaceContext.at(context, context.getClickedPos()
                    .relative(clickedFace), clickedFace));

        if (result == InteractionResult.FAIL && context.getLevel().isClientSide())
            CatnipServices.PLATFORM.executeOnClientOnly(() -> () -> showBounds(context));

        return result;
    }

    @OnlyIn(Dist.CLIENT)
    public void showBounds(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Axis axis = ((FlywheelBlock) getBlock()).getAxisForPlacement(context);
        Vec3 contract = Vec3.atLowerCornerOf(Direction.get(AxisDirection.POSITIVE, axis).getNormal());

        if (!(context.getPlayer() instanceof LocalPlayer localPlayer))
            return;

        Outliner.getInstance().showAABB(Pair.of("flywheel", pos), new AABB(pos).inflate(1)
                        .deflate(contract.x, contract.y, contract.z))
                .colored(0xFF_ff5d6c);
        localPlayer.displayClientMessage(Component.literal("Not enough space")
                .withStyle(ChatFormatting.RED), true);
    }
}

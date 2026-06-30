package fr.algorythmice.createnewindustry.content.kinetics.flywheel;

import com.simibubi.create.foundation.block.IBE;
import fr.algorythmice.createnewindustry.AllBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class FlywheelPartBlock extends Block
        implements IBE<FlywheelPartBlockEntity> {

    public FlywheelPartBlock(Properties properties) {
        super(properties);
    }


    @Override
    public BlockState playerWillDestroy(Level level,
                                        BlockPos pos,
                                        BlockState state,
                                        Player player) {

        if (!level.isClientSide) {

            BlockPos center = findCenter(level, pos);

            if (center != null) {
                level.destroyBlock(center, !player.isCreative(), player);
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }



    @Override
    public float getDestroyProgress(BlockState state,
                                    Player player,
                                    BlockGetter level,
                                    BlockPos pos) {

        BlockPos center = findCenter(level, pos);

        if (center != null) {
            BlockState centerState = level.getBlockState(center);

            return centerState.getDestroyProgress(
                    player,
                    level,
                    center);
        }

        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    public BlockEntityType<? extends FlywheelPartBlockEntity> getBlockEntityType() {
        return AllBlockEntityTypes.FLYWHEEL_PART.get();
    }

    @Override
    public Class<FlywheelPartBlockEntity> getBlockEntityClass() {
        return FlywheelPartBlockEntity.class;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    private BlockPos findCenter(BlockGetter level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof FlywheelPartBlockEntity part && isValidCenter(level, pos, part.getCenter())) {
            return part.getCenter();
        }

        for (Direction.Axis axis : Direction.Axis.values()) {
            for (int a = -1; a <= 1; a++) {
                for (int b = -1; b <= 1; b++) {
                    if (a == 0 && b == 0)
                        continue;

                    BlockPos center = switch (axis) {
                        case X -> pos.offset(0, -a, -b);
                        case Y -> pos.offset(-a, 0, -b);
                        case Z -> pos.offset(-a, -b, 0);
                    };

                    if (isValidCenter(level, pos, center)) {
                        return center;
                    }
                }
            }
        }

        return null;
    }

    private boolean isValidCenter(BlockGetter level, BlockPos pos, BlockPos center) {
        BlockState centerState = level.getBlockState(center);

        if (!(centerState.getBlock() instanceof FlywheelBlock)) {
            return false;
        }

        Direction.Axis axis = centerState.getValue(FlywheelBlock.AXIS);

        return switch (axis) {
            case X -> pos.getX() == center.getX()
                    && Math.abs(pos.getY() - center.getY()) <= 1
                    && Math.abs(pos.getZ() - center.getZ()) <= 1
                    && !pos.equals(center);
            case Y -> pos.getY() == center.getY()
                    && Math.abs(pos.getX() - center.getX()) <= 1
                    && Math.abs(pos.getZ() - center.getZ()) <= 1
                    && !pos.equals(center);
            case Z -> pos.getZ() == center.getZ()
                    && Math.abs(pos.getX() - center.getX()) <= 1
                    && Math.abs(pos.getY() - center.getY()) <= 1
                    && !pos.equals(center);
        };
    }

}


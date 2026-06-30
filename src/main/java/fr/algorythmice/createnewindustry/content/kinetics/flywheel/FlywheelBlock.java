package fr.algorythmice.createnewindustry.content.kinetics.flywheel;

import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import fr.algorythmice.createnewindustry.AllBlockEntityTypes;
import fr.algorythmice.createnewindustry.AllBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


import com.simibubi.create.AllShapes;

public class FlywheelBlock extends RotatedPillarKineticBlock implements IBE<FlywheelBlockEntity> {

    public static final VoxelShape FLYWHEEL_SHAPE = AllShapes.CASING_14PX.get(Direction.DOWN);

    public FlywheelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
        super.onPlace(state, level, pos, oldState, moved);

        if (level.isClientSide)
            return;

        Direction.Axis axis = state.getValue(AXIS);

        createStructure(level, pos, axis);
    }

    @Override
    public void onRemove(BlockState state,
                         Level level,
                         BlockPos pos,
                         BlockState newState,
                         boolean moved) {

        if (state.getBlock() != newState.getBlock()) {
            removeStructure(level, pos, state.getValue(AXIS));
        }

        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canCreateStructure((Level) level, pos, state.getValue(AXIS));
    }


    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.CRUSHING_WHEEL_COLLISION_SHAPE;
    }

    @Override
    public BlockState playerWillDestroy(Level level,
                                  BlockPos pos,
                                  BlockState state,
                                  Player player) {

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(AXIS);
    }

    @Override
    public BlockEntityType<? extends FlywheelBlockEntity> getBlockEntityType() {
        return AllBlockEntityTypes.FLYWHEEL.get();
    }

    @Override
    public Class<FlywheelBlockEntity> getBlockEntityClass() {
        return FlywheelBlockEntity.class;
    }

    @Override
    public void tick(BlockState state,
                     net.minecraft.server.level.ServerLevel level,
                     BlockPos pos,
                     net.minecraft.util.RandomSource random) {

        if (!canCreateStructure(level, pos, state.getValue(AXIS))) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public void neighborChanged(BlockState state,
                                Level level,
                                BlockPos pos,
                                net.minecraft.world.level.block.Block block,
                                BlockPos fromPos,
                                boolean moving) {

        super.neighborChanged(state, level, pos, block, fromPos, moving);

        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 1);
        }
    }

    private void removeStructure(Level level, BlockPos center, Direction.Axis axis) {

        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {

                if (a == 0 && b == 0)
                    continue;

                BlockPos target;

                switch (axis) {
                    case X -> target = center.offset(0, a, b);
                    case Y -> target = center.offset(a, 0, b);
                    case Z -> target = center.offset(a, b, 0);
                    default -> throw new IllegalStateException();
                }

                if (level.getBlockState(target).is(AllBlocks.FLYWHEEL_PART.get()))
                    level.removeBlock(target, false);
            }
        }
    }

    private boolean canCreateStructure(LevelReader level, BlockPos center, Direction.Axis axis) {

        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {

                if (a == 0 && b == 0)
                    continue;

                BlockPos target;

                switch (axis) {
                    case X -> target = center.offset(0, a, b);
                    case Y -> target = center.offset(a, 0, b);
                    case Z -> target = center.offset(a, b, 0);
                    default -> throw new IllegalStateException();
                }

                BlockState state = level.getBlockState(target);

                if (state.is(AllBlocks.FLYWHEEL_PART.get())) {

                    if (level.getBlockEntity(target) instanceof FlywheelPartBlockEntity be) {
                        if (!be.getCenter().equals(center))
                            return false;
                    }

                    continue;
                }

                if (!state.canBeReplaced())
                    return false;
            }
        }

        return true;
    }

    private void createStructure(Level level, BlockPos center, Direction.Axis axis) {

        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {

                if (a == 0 && b == 0)
                    continue;

                BlockPos target;

                switch (axis) {

                    case X -> target = center.offset(0, a, b);

                    case Y -> target = center.offset(a, 0, b);

                    case Z -> target = center.offset(a, b, 0);

                    default -> throw new IllegalStateException();
                }

                level.setBlock(target, AllBlocks.FLYWHEEL_PART.getDefaultState(), 3);
                if (level.getBlockEntity(target) instanceof FlywheelPartBlockEntity be) {
                    be.setCenter(center);
                }

            }
        }
    }
}
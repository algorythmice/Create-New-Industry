package fr.algorythmice.createnewindustry.content.kinetics.flywheel;

import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import fr.algorythmice.createnewindustry.AllBlockEntityTypes;
import fr.algorythmice.createnewindustry.AllBlocks;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
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

    public Axis getAxisForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).getValue(AXIS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        BlockPos pos = context.getClickedPos();
        Axis axis = stateForPlacement.getValue(AXIS);

        for (BlockPos offset : getStructureOffsets(axis)) {
            BlockState occupiedState = context.getLevel()
                    .getBlockState(pos.offset(offset));
            if (!occupiedState.canBeReplaced())
                return null;
        }

        return stateForPlacement;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
        super.onPlace(state, level, pos, oldState, moved);

        if (level.isClientSide)
            return;

        if (!level.getBlockTicks().hasScheduledTick(pos, this))
            level.scheduleTick(pos, this, 1);
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
        return canCreateStructure(level, pos, state.getValue(AXIS));
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
                     ServerLevel level,
                     BlockPos pos,
                     RandomSource random) {

        if (!canCreateStructure(level, pos, state.getValue(AXIS))) {
            level.destroyBlock(pos, true);
            return;
        }

        createStructure(level, pos, state.getValue(AXIS));
    }

    @Override
    public BlockState updateShape(BlockState state,
                                  Direction facing,
                                  BlockState facingState,
                                  LevelAccessor level,
                                  BlockPos currentPos,
                                  BlockPos facingPos) {
        if (!level.isClientSide()
                && !level.getBlockTicks().hasScheduledTick(currentPos, this))
            level.scheduleTick(currentPos, this, 1);
        return state;
    }

    private void removeStructure(Level level, BlockPos center, Direction.Axis axis) {

        for (BlockPos offset : getStructureOffsets(axis)) {
            BlockPos target = center.offset(offset);
            if (level.getBlockState(target).is(AllBlocks.FLYWHEEL_PART.get()))
                level.removeBlock(target, false);
        }
    }

    private boolean canCreateStructure(LevelReader level, BlockPos center, Direction.Axis axis) {

        for (BlockPos offset : getStructureOffsets(axis)) {
            BlockPos target = center.offset(offset);
            BlockState state = level.getBlockState(target);

            if (state.is(AllBlocks.FLYWHEEL_PART.get()))
                continue;

            if (!state.canBeReplaced())
                return false;
        }

        return true;
    }

    private void createStructure(Level level, BlockPos center, Direction.Axis axis) {

        for (Direction side : Iterate.directions) {
            if (side.getAxis() == axis)
                continue;
            for (boolean secondary : Iterate.falseAndTrue) {
                Direction targetSide = secondary ? side.getClockWise(axis) : side;
                BlockPos structurePos = (secondary ? center.relative(side) : center).relative(targetSide);
                BlockState occupiedState = level.getBlockState(structurePos);
                BlockState requiredStructure = AllBlocks.FLYWHEEL_PART.getDefaultState()
                        .setValue(FlywheelPartBlock.FACING, targetSide.getOpposite());
                if (occupiedState == requiredStructure)
                    continue;
                if (!occupiedState.canBeReplaced()) {
                    level.destroyBlock(center, true);
                    return;
                }
                level.setBlockAndUpdate(structurePos, requiredStructure);
            }
        }
    }

    private Iterable<BlockPos> getStructureOffsets(Axis axis) {
        java.util.List<BlockPos> offsets = new java.util.ArrayList<>(8);
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (axis.choose(x, y, z) != 0)
                        continue;
                    BlockPos offset = new BlockPos(x, y, z);
                    if (offset.equals(BlockPos.ZERO))
                        continue;
                    offsets.add(offset);
                }
            }
        }
        return offsets;
    }
}

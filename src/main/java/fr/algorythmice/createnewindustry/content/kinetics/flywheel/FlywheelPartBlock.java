package fr.algorythmice.createnewindustry.content.kinetics.flywheel;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.render.MultiPosDestructionHandler;
import fr.algorythmice.createnewindustry.AllBlocks;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;

public class FlywheelPartBlock extends DirectionalBlock implements IWrenchable {

    public static final MapCodec<FlywheelPartBlock> CODEC = simpleCodec(FlywheelPartBlock::new);

    public FlywheelPartBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return AllBlocks.FLYWHEEL.asStack();
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (stillValid(level, pos, state, false)) {
            BlockPos masterPos = getMaster(level, pos, state);
            level.destroyBlockProgress(masterPos.hashCode(), masterPos, -1);
            if (!level.isClientSide() && player.isCreative())
                level.destroyBlock(masterPos, false);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (stillValid(level, pos, state, false))
            level.destroyBlock(getMaster(level, pos, state), true);
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        if (stillValid(level, pos, state, false)) {
            BlockPos masterPos = getMaster(level, pos, state);
            return level.getBlockState(masterPos).getDestroyProgress(player, level, masterPos);
        }

        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level,
                                  BlockPos currentPos, BlockPos facingPos) {
        if (stillValid(level, currentPos, state, false)) {
            BlockPos masterPos = getMaster(level, currentPos, state);
            if (!level.getBlockTicks().hasScheduledTick(masterPos, AllBlocks.FLYWHEEL.get()))
                level.scheduleTick(masterPos, AllBlocks.FLYWHEEL.get(), 1);
            return state;
        }
        if (!(level instanceof Level realLevel) || realLevel.isClientSide())
            return state;
        if (!realLevel.getBlockTicks().hasScheduledTick(currentPos, this))
            realLevel.scheduleTick(currentPos, this, 1);
        return state;
    }

    public static BlockPos getMaster(BlockGetter level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos targetedPos = pos.relative(direction);
        BlockState targetedState = level.getBlockState(targetedPos);
        if (targetedState.is(AllBlocks.FLYWHEEL_PART.get()))
            return getMaster(level, targetedPos, targetedState);
        return targetedPos;
    }

    public boolean stillValid(BlockGetter level, BlockPos pos, BlockState state, boolean directlyAdjacent) {
        if (!state.is(this))
            return false;

        Direction direction = state.getValue(FACING);
        BlockPos targetedPos = pos.relative(direction);
        BlockState targetedState = level.getBlockState(targetedPos);

        if (!directlyAdjacent && stillValid(level, targetedPos, targetedState, true))
            return true;

        return targetedState.getBlock() instanceof FlywheelBlock
                && targetedState.getValue(FlywheelBlock.AXIS) != direction.getAxis();
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!stillValid(level, pos, state, false))
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }

    @Override
    public boolean addLandingEffects(BlockState state, ServerLevel level, BlockPos pos, BlockState stateOn,
                                     LivingEntity entity, int numberOfParticles) {
        return true;
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    public static class RenderProperties implements IClientBlockExtensions, MultiPosDestructionHandler {

        @Override
        public boolean addDestroyEffects(BlockState state, Level level, BlockPos pos, ParticleEngine manager) {
            return true;
        }

        @Override
        public boolean addHitEffects(BlockState state, Level level, HitResult target, ParticleEngine manager) {
            if (target instanceof BlockHitResult blockHitResult) {
                BlockPos targetPos = blockHitResult.getBlockPos();
                FlywheelPartBlock partBlock = AllBlocks.FLYWHEEL_PART.get();
                if (partBlock.stillValid(level, targetPos, state, false))
                    manager.crack(FlywheelPartBlock.getMaster(level, targetPos, state), blockHitResult.getDirection());
                return true;
            }
            return IClientBlockExtensions.super.addHitEffects(state, level, target, manager);
        }

        @Override
        @Nullable
        public Set<BlockPos> getExtraPositions(ClientLevel level, BlockPos pos, BlockState blockState, int progress) {
            FlywheelPartBlock partBlock = AllBlocks.FLYWHEEL_PART.get();
            if (!partBlock.stillValid(level, pos, blockState, false))
                return null;

            HashSet<BlockPos> set = new HashSet<>();
            set.add(FlywheelPartBlock.getMaster(level, pos, blockState));
            return set;
        }
    }
}

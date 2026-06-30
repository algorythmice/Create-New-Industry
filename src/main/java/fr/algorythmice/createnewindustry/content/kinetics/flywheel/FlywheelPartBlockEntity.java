package fr.algorythmice.createnewindustry.content.kinetics.flywheel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FlywheelPartBlockEntity extends BlockEntity {

    private BlockPos center = BlockPos.ZERO;
    public FlywheelPartBlockEntity(BlockEntityType<?> type,
                                   BlockPos pos,
                                   BlockState state) {
        super(type, pos, state);
    }



    public void setCenter(BlockPos center) {
        this.center = center;
        setChanged();
    }

    public BlockPos getCenter() {
        return center;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("Center", center.asLong());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        center = BlockPos.of(tag.getLong("Center"));
    }

}

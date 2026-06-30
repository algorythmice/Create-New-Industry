package fr.algorythmice.createnewindustry.content.kinetics.flywheel;

import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FlywheelBlockEntity extends GeneratingKineticBlockEntity {

    private static final float INERTIA = 100f;
    private static final float MAX_OMEGA = 256f;
    private static final float VISCOUS_DRAG = 5e-5f;
    private static final float BEARING_DRAG = 0.008f;
    private static final float CHARGE_RATE = 0.05f;
    private static final float CHARGE_EFFICIENCY = 0.60f;
    private static final float BASE_LOAD_POWER = 3200f;
    private static final float COUPLING_LOSS_EACH = 0.10f;
    private static final float STOP_THRESHOLD = 0.4f;
    private static final float NETWORK_UPDATE_DELTA = 10.0f;
    private static final int UPDATE_COOLDOWN_TICKS = 4;
    public float storedOmega = 0f;
    public int storedDirection = 1;
    public boolean isDischarging = false;

    private float cachedOutputRPM = 0f;
    private int updateCooldown = 0;

    public FlywheelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide) return;

        if (updateCooldown > 0) updateCooldown--;

        boolean hasExternal = hasExternalPower();

        if (isDischarging && hasExternal) {
            isDischarging = false;
            storedDirection = (int) Math.signum(getSpeed());
            pushNetworkUpdate();
            return;
        }

        if (!isDischarging) {
            applyFriction();

            float networkRPM = Math.abs(getSpeed());
            if (hasExternal && networkRPM > 0f) {
                chargeFrom(networkRPM);
            } else if (!hasExternal && storedOmega > STOP_THRESHOLD) {
                isDischarging = true;
                pushNetworkUpdate();
            }
            return;
        }

        applyFriction();
        applyLoadDrain();

        if (storedOmega <= STOP_THRESHOLD) {
            storedOmega = 0f;
            isDischarging = false;
            pushNetworkUpdate();
            setChanged();
            return;
        }

        float newRPM = computeOutputRPM();
        if (Math.abs(newRPM - cachedOutputRPM) >= NETWORK_UPDATE_DELTA && updateCooldown <= 0) {
            cachedOutputRPM = newRPM;
            updateCooldown = UPDATE_COOLDOWN_TICKS;
            updateGeneratedRotation();
            setChanged();
        } else if (level.getGameTime() % 20 == 0) {
            setChanged();
        }
    }


    private void applyFriction() {
        if (storedOmega <= 0f) return;
        float dOmega = (VISCOUS_DRAG * storedOmega * storedOmega
                + BEARING_DRAG * storedOmega) / INERTIA;
        storedOmega = Math.max(0f, storedOmega - dOmega);
    }

    private void chargeFrom(float inputRPM) {
        float targetOmega = Math.min(inputRPM, MAX_OMEGA);
        if (storedOmega >= targetOmega) return;

        float delta = (targetOmega - storedOmega) * CHARGE_RATE * CHARGE_EFFICIENCY;
        storedOmega = Math.min(targetOmega, storedOmega + delta);
        storedDirection = (int) Math.signum(getSpeed());
        setChanged();
    }

    private void applyLoadDrain() {
        if (storedOmega <= 0.01f) return;

        int   n              = countDischargingFlywheels();
        float loadFraction   = estimateNetworkLoad();
        float usefulShare    = BASE_LOAD_POWER * loadFraction / n;
        float lossMultiplier = 1f + COUPLING_LOSS_EACH * (n - 1);

        float powerExtracted = usefulShare * lossMultiplier;

        float dOmega = powerExtracted / (INERTIA * storedOmega);
        storedOmega = Math.max(0f, storedOmega - dOmega);
    }

    private boolean hasExternalPower() {
        if (level == null || level.isClientSide) return false;
        KineticNetwork network = getOrCreateNetwork();
        if (network == null || network.sources == null) return false;
        for (KineticBlockEntity src : network.sources.keySet()) {
            if (!(src instanceof FlywheelBlockEntity)) return true;
        }
        return false;
    }

    private int countDischargingFlywheels() {
        KineticNetwork network = getOrCreateNetwork();
        if (network == null || network.sources == null) return 1;
        int n = 0;
        for (KineticBlockEntity src : network.sources.keySet()) {
            if (src instanceof FlywheelBlockEntity fw && fw.isDischarging) n++;
        }
        return Math.max(1, n);
    }

    private float estimateNetworkLoad() {
         KineticNetwork network = getOrCreateNetwork();
         if (network == null) return 0.5f;
         float cap = network.calculateCapacity();
         if (cap <= 0f) return 0f;
         return Math.min(1f, network.calculateStress() / cap);
    }

    private float computeOutputRPM() {
        return Math.min(MAX_OMEGA, storedOmega);
    }

    @Override
    public float getGeneratedSpeed() {
        if (!isDischarging || storedOmega <= STOP_THRESHOLD) return 0f;
        if (cachedOutputRPM == 0f) cachedOutputRPM = computeOutputRPM();
        return cachedOutputRPM * storedDirection;
    }

    private void pushNetworkUpdate() {
        cachedOutputRPM = isDischarging ? computeOutputRPM() : 0f;
        updateGeneratedRotation();
        updateCooldown = UPDATE_COOLDOWN_TICKS;
        setChanged();
    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putFloat("StoredOmega",     storedOmega);
        tag.putBoolean("IsDischarging", isDischarging);
        tag.putInt("StoredDirection",   storedDirection);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        storedOmega     = tag.getFloat("StoredOmega");
        isDischarging   = tag.getBoolean("IsDischarging");
        storedDirection = tag.getInt("StoredDirection");
        cachedOutputRPM = isDischarging ? computeOutputRPM() : 0f;
    }
}
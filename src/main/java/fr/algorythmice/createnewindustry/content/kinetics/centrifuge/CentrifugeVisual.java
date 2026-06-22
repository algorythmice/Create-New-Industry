package fr.algorythmice.createnewindustry.content.kinetics.centrifuge;

import java.util.function.Consumer;

import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.render.AllInstanceTypes;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import fr.algorythmice.createnewindustry.AllPartialModels;
import net.minecraft.core.Direction;

public class CentrifugeVisual extends SingleAxisRotatingVisual<MechanicalCentrifugeBlockEntity> implements SimpleDynamicVisual {

    private final RotatingInstance centrifugeHead;
    private final OrientedInstance centrifugePole;
    private final MechanicalCentrifugeBlockEntity centrifuge;

    public CentrifugeVisual(VisualizationContext context, MechanicalCentrifugeBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(com.simibubi.create.AllPartialModels.SHAFTLESS_COGWHEEL));
        this.centrifuge = blockEntity;

        centrifugeHead = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.MECHANICAL_CENTRIFUGE_HEAD))
                .createInstance();

        centrifugeHead.setRotationAxis(Direction.Axis.Y);

        centrifugePole = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(AllPartialModels.MECHANICAL_CENTRIFUGE_POLE))
                .createInstance();

        animate(partialTick);
    }
    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        animate(ctx.partialTick());
    }

    private void animate(float pt) {
        float renderedHeadOffset = centrifuge.getRenderedHeadOffset(pt);

        transformPole(renderedHeadOffset);
        transformHead(renderedHeadOffset, pt);
    }

    private void transformHead(float renderedHeadOffset, float pt) {
        float speed = centrifuge.getRenderedHeadRotationSpeed(pt);

        centrifugeHead.setPosition(getVisualPosition())
                .nudge(0, -renderedHeadOffset, 0)
                .setRotationalSpeed(speed * 2 * RotatingInstance.SPEED_MULTIPLIER)
                .setChanged();
    }

    private void transformPole(float renderedHeadOffset) {
        centrifugePole.position(getVisualPosition())
                .translatePosition(0, -renderedHeadOffset, 0)
                .setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        super.updateLight(partialTick);

        relight(pos.below(), centrifugeHead);
        relight(centrifugePole);
    }

    @Override
    protected void _delete() {
        super._delete();
        centrifugeHead.delete();
        centrifugePole.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        super.collectCrumblingInstances(consumer);
        consumer.accept(centrifugeHead);
        consumer.accept(centrifugePole);
    }
}
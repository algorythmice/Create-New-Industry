package fr.algorythmice.createnewindustry.infrastructure.ponder.scenes;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class FlywheelScenes {

    public static void flywheel(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.world().setKineticSpeed(util.select().everywhere(), 0);

        scene.title("flywheel", "Storing Kinetic Energy");
        scene.configureBasePlate(0, 0, 5);

        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(5);
        scene.world().showSection(util.select().position(1, 3, 5), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(1, 1, 0, 1, 3, 4), Direction.SOUTH);
        scene.idle(20);



        BlockPos flywheel = util.grid().at(0, 3, 5);
        Vec3 flywheelside = util.vector().blockSurface(flywheel, Direction.EAST);

        scene.overlay().showText(60)
                .pointAt(flywheelside)
                .placeNearTarget()
                .attachKeyFrame()
                .text("A Flywheel cannot generate power on its own. It must first be spun by an external kinetic source.");
        scene.idle(40);
        scene.idle(20);

        scene.world().setKineticSpeed(util.select().everywhere(), 60);
        scene.world().setKineticSpeed(
                util.select().position(0, 3, 3),
                60
        );
        scene.world().showSection(util.select().position(0, 3, 3), Direction.EAST);

        scene.idle(15);

        scene.overlay().showText(60)
                .pointAt(flywheelside)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Once spinning, the Flywheel stores kinetic energy and can keep your machines running after the external power source stops.");
        scene.idle(75);


        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(0, 4, 5), Direction.NORTH), Pointing.RIGHT, 50)
                .withItem(AllItems.GOGGLES.asStack());

        scene.overlay().showText(80)
                .pointAt(flywheelside)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Use Engineer's Goggles to check how much kinetic energy is stored.");
        scene.idle(90);

        scene.world().setKineticSpeed(util.select().everywhere(), 60);
        scene.world().setKineticSpeed(
                util.select().position(1, 3, 5),
                50
        );
        scene.world().setBlock(util.grid().at(0, 3, 3), Blocks.AIR.defaultBlockState(), true);
        scene.idle(20);

        scene.overlay().showText(80)
                .pointAt(flywheelside)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Without external power, the Flywheel gradually slows down as it releases its stored energy.");
        scene.idle(100);

        scene.rotateCameraY(30);
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(1, 3, 1, 4, 3, 3), Direction.UP);
        scene.idle(10);
        scene.rotateCameraY(-30);

        scene.idle(15);

        scene.overlay().showText(80)
                .pointAt(flywheelside)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Higher stress consumption drains the Flywheel faster.");
        scene.idle(100);

        scene.world().showSection(util.select().position(1, 3, 6), Direction.EAST);

        scene.overlay().showText(80)
                .pointAt(flywheelside)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Connecting additional Flywheels increases the total stored kinetic energy.");
        scene.idle(80);
    }
}

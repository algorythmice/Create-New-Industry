package fr.algorythmice.createnewindustry.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import fr.algorythmice.createnewindustry.AllBlocks;
import fr.algorythmice.createnewindustry.AllPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class AnimatedCentrifuge extends AnimatedKinetics {
    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 23;

        blockElement(cogwheel())
                .rotateBlock(0, -getCurrentAngle() * 2, 0)
                .atLocal(0, 0, 0)
                .scale(scale)
                .render(graphics);

        blockElement(AllBlocks.MECHANICAL_CENTRIFUGE.getDefaultState())
                .atLocal(0, 0, 0)
                .scale(scale)
                .render(graphics);

        float animation = ((Mth.sin(AnimationTickHolder.getRenderTime() / 32f) + 1) / 5) + .5f;

        blockElement(AllPartialModels.MECHANICAL_CENTRIFUGE_POLE)
                .atLocal(0, animation, 0)
                .scale(scale)
                .render(graphics);

        blockElement(AllPartialModels.MECHANICAL_CENTRIFUGE_HEAD)
                .rotateBlock(0, -getCurrentAngle() * 4, 0)
                .atLocal(0, animation, 0)
                .scale(scale)
                .render(graphics);

        blockElement(com.simibubi.create.AllBlocks.BASIN.getDefaultState())
                .atLocal(0, 1.65, 0)
                .scale(scale)
                .render(graphics);

        matrixStack.popPose();
    }
}

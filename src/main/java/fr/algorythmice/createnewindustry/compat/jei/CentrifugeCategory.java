package fr.algorythmice.createnewindustry.compat.jei;

import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphics;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CentrifugeCategory extends BasinCategory {

    private final AnimatedCentrifuge centrifuge = new AnimatedCentrifuge();
    private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();

    private final CentrifugeCategoryType type;

    enum CentrifugeCategoryType {
        MIXING,
        AUTO_SHAPELESS,
        AUTO_BREWING
    }

    public static CentrifugeCategory standard(Info<BasinRecipe> info) {
        return new CentrifugeCategory(info, CentrifugeCategoryType.MIXING);
    }

    public static CentrifugeCategory autoShapeless(Info<BasinRecipe> info) {
        return new CentrifugeCategory(info, CentrifugeCategoryType.AUTO_SHAPELESS);
    }

    public static CentrifugeCategory autoBrewing(Info<BasinRecipe> info) {
        return new CentrifugeCategory(info, CentrifugeCategoryType.AUTO_BREWING);
    }

    protected CentrifugeCategory(
            Info<BasinRecipe> info,
            CentrifugeCategoryType type
    ) {
        super(info, type != CentrifugeCategoryType.AUTO_SHAPELESS);
        this.type = type;
    }

    @Override
    public void draw(
            BasinRecipe recipe,
            IRecipeSlotsView slots,
            GuiGraphics graphics,
            double mouseX,
            double mouseY
    ) {
        super.draw(recipe, slots, graphics, mouseX, mouseY);

        HeatCondition requiredHeat = recipe.getRequiredHeat();

        if (requiredHeat != HeatCondition.NONE) {
            heater.withHeat(requiredHeat.visualizeAsBlazeBurner())
                    .draw(graphics, getBackground().getWidth() / 2 + 3, 55);
        }

        centrifuge.draw(graphics,
                getBackground().getWidth() / 2 + 3,
                34);
    }
}

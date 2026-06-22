package fr.algorythmice.createnewindustry.compat.jei;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import fr.algorythmice.createnewindustry.AllBlocks;
import fr.algorythmice.createnewindustry.AllRecipeTypes;
import fr.algorythmice.createnewindustry.CreateNewIndustry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
@JeiPlugin
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class CreateNewIndustryJEI implements IModPlugin {
    private static final ResourceLocation ID = CreateNewIndustry.asResource("jei_plugin");

    private final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();

    private void loadCategories() {
        allCategories.clear();

        CreateRecipeCategory<?>

                centrifuge = builder(BasinRecipe.class)
                        .addTypedRecipes(AllRecipeTypes.CENTRIFUGE)
                        .catalyst(AllBlocks.MECHANICAL_CENTRIFUGE::get)
                        .catalyst(com.simibubi.create.AllBlocks.BASIN::get)
                        .doubleItemIcon(AllBlocks.MECHANICAL_CENTRIFUGE.get(), com.simibubi.create.AllBlocks.BASIN.get())
                        .emptyBackground(177, 103)
                        .build("centrifuge", CentrifugeCategory::standard);

    }

    private <T extends Recipe<? extends RecipeInput>> CreateNewIndustryJEI.CategoryBuilder<T> builder(Class<T> recipeClass) {
        return new CreateNewIndustryJEI.CategoryBuilder<>(recipeClass);
    }

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        loadCategories();
        registration.addRecipeCategories(allCategories.toArray(IRecipeCategory[]::new));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        allCategories.forEach(c -> c.registerRecipes(registration));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        allCategories.forEach(c -> c.registerCatalysts(registration));
    }

    private class CategoryBuilder<T extends Recipe<?>> extends CreateRecipeCategory.Builder<T> {
        public CategoryBuilder(Class<? extends T> recipeClass) {
            super(recipeClass);
        }

        @Override
        public CreateRecipeCategory<T> build(ResourceLocation id, CreateRecipeCategory.Factory<T> factory) {
            CreateRecipeCategory<T> category = super.build(id, factory);
            allCategories.add(category);
            return category;
        }
    }
}

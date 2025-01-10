package ace.actually.dataplanets;

import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class DPRecipes {

    public static void doRecpies(Consumer<FinishedRecipe> provider)
    {
        DPRecipeTypes.OBSERVATORY_RECIPE_TYPE.recipeBuilder(ResourceLocation.tryBuild("dataplanets","observetory_recipes"))
                .EUt(256)
                .duration(10000)
                .inputItems(Items.WRITABLE_BOOK)
                .outputItems(DPItems.THEORY_VISIBLE)
                .save(provider);

        VanillaRecipeHelper.addShapelessRecipe(provider,ResourceLocation.tryBuild("dataplanets","theorycraft1"),
                new ItemStack(DPItems.THEORY_2.get()),new ItemStack(DPItems.THEORY_RADIO.get()),new ItemStack(DPItems.THEORY_VISIBLE.get()));


        DPRecipeTypes.RADIO_RECIPE_TYPE.recipeBuilder(ResourceLocation.tryBuild("dataplanets","radio_recipes"))
                .EUt(256)
                .duration(10000)
                .inputItems(Items.WRITABLE_BOOK)
                .outputItems(DPItems.THEORY_RADIO)
                .save(provider);


    }
}

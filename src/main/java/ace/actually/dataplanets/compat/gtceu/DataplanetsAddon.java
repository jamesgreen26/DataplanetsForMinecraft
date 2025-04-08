package ace.actually.dataplanets.compat.gtceu;

import ace.actually.dataplanets.Dataplanets;
import ace.actually.dataplanets.registry.DPRecipes;
import ace.actually.dataplanets.registry.Reg;
import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

@GTAddon
public class DataplanetsAddon implements IGTAddon {
    @Override
    public GTRegistrate getRegistrate() {
        return Reg.REGISTRATE;
    }

    @Override
    public void initializeAddon() {

    }

    @Override
    public String addonModId() {
        return Dataplanets.MODID;
    }

    @Override
    public void addRecipes(Consumer<FinishedRecipe> provider) {
        DPRecipes.doRecpies(provider);
    }
}

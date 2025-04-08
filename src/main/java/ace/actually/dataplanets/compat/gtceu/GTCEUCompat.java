package ace.actually.dataplanets.compat.gtceu;

import ace.actually.dataplanets.Dataplanets;
import ace.actually.dataplanets.registry.DPMachines;
import ace.actually.dataplanets.registry.DPRecipeTypes;
import ace.actually.dataplanets.space.StarSystemCreator;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.event.PostMaterialEvent;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class GTCEUCompat {

    @Mod.EventBusSubscriber(modid = Dataplanets.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents
    {
        @SubscribeEvent
        public static void registerMachines(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
            DPMachines.init();
        }
        @SubscribeEvent
        public static void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
            DPRecipeTypes.init();
        }

        @SubscribeEvent
        public static void postMaterial(PostMaterialEvent event)
        {
            //System.out.println("trying to make fluid blocks...");

            //TODO: check this is actually doing what it is suppose to
            GTCEuAPI.materialManager.getRegistry(GTCEu.MOD_ID).getAllMaterials().stream().filter(a->a.hasFluid()).forEach(a->a.getFluidBuilder().block().still());
        }

    }

    public static void doRegister(IEventBus bus)
    {
        bus.addGenericListener(GTRecipeType.class, ModEvents::registerRecipeTypes);
        bus.addGenericListener(MachineDefinition.class, ModEvents::registerMachines);
    }

    public static void doInits()
    {

    }
}

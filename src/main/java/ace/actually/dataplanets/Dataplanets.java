package ace.actually.dataplanets;

import ace.actually.dataplanets.registry.*;
import ace.actually.dataplanets.space.StarSystemCreator;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.event.PostMaterialEvent;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import java.util.UUID;

import static net.minecraft.commands.Commands.literal;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Dataplanets.MODID)
public class Dataplanets
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "dataplanets";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final UUID LOW_GRAVITY = UUID.fromString("662A6B8D-DA3E-4C1C-1112-96EA6097278D");


    public static final ResourceLocation DATA_STORAGE  = ResourceLocation.tryBuild("dataplanets","data_storage");

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Dataplanets()
    {
        // Register the commonSetup method for modloading

        DPTabs.init();
        DPItems.init();
        DPBlocks.init();
        Reg.REGISTRATE.registerRegistrate();

        MinecraftForge.EVENT_BUS.register(this);

        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addGenericListener(GTRecipeType.class, ModEvents::registerRecipeTypes);
        ;
        bus.addGenericListener(MachineDefinition.class, ModEvents::registerMachines);

    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        if(event.getServer().isDedicatedServer())
        {
            StarSystemCreator.DATALOC = event.getServer().getServerDirectory().getAbsolutePath() +"\\world\\datapacks\\";

        }
        else
        {
            StarSystemCreator.DATALOC = event.getServer().getServerDirectory().getAbsolutePath() +"\\saves\\"+event.getServer().getWorldData().getLevelName()+"\\datapacks\\";

        }
        StarSystemCreator.RECLOC = event.getServer().getServerDirectory().getAbsolutePath()+"\\resourcepacks\\";
    }

    @SubscribeEvent
    public void commands(RegisterCommandsEvent event)
    {
        event.getDispatcher().register(literal("dataplanets")
                .then(literal("getrps")
                        .executes(context -> {

                            //TODO: it would probably be better to not send the entire tag but just the resources necessary.
                            //^ its not really a problem, none of the data sent is particularly special ^
                            CompoundTag tag = context.getSource().getServer().getCommandStorage().get(StarSystemCreator.SYSTEM_DATA);

                            //PacketDistributor.PLAYER.with(()->context.getSource().getPlayer()).send();
                            return 1;
                        }))
                .then(literal("reload")
                        .executes(context -> {

                            return 1;
                        })));
    }



    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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

            StarSystemCreator.FLUIDABLE = GTCEuAPI.materialManager.getRegistry(GTCEu.MOD_ID).getAllMaterials().stream().filter(a->a.hasFluid()).toList();
            StarSystemCreator.FLUIDABLE.forEach(a->
            {
                //System.out.println("Making a fluid block for "+a.getName()+"!");
                a.getFluidBuilder().block().still();
            });
        }

    }
}

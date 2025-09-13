package ace.actually.dataplanets;

import ace.actually.dataplanets.compat.Compat;
import ace.actually.dataplanets.compat.gcyr.GCYRPacket;
import ace.actually.dataplanets.registry.*;
import ace.actually.dataplanets.space.S2PTranslationPacket;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.RegisterNamedRenderTypesEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import java.util.UUID;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Dataplanets.MODID)
public class Dataplanets
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "dataplanets";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final UUID LOW_GRAVITY = UUID.fromString("662A6B8D-DA3E-4C1C-1112-96EA6097278D");

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Dataplanets()
    {


        // Register the commonSetup method for modloading
        Compat.loadCompat("gcyr");
        DPPackets.INSTANCE.messageBuilder(GCYRPacket.class, 0)
                .encoder(GCYRPacket::encoder)
                .decoder(GCYRPacket::decoder)
                .consumerMainThread(GCYRPacket::messageConsumer)
                .add();
        DPPackets.INSTANCE.messageBuilder(S2PTranslationPacket.class, 1)
                .encoder(S2PTranslationPacket::encoder)
                .decoder(S2PTranslationPacket::decoder)
                .consumerMainThread(S2PTranslationPacket::messageConsumer)
                .add();

        DPTabs.init();
        DPItems.init();
        DPBlocks.init();
        Reg.REGISTRATE.registerRegistrate();

        MinecraftForge.EVENT_BUS.register(this);

        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        Compat.modEventBusLoad(bus);



    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void playerLogsIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        Compat.postLoadWorld();
    }



}

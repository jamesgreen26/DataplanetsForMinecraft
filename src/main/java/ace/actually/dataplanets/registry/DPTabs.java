package ace.actually.dataplanets.registry;

import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public class DPTabs {

      public static RegistryEntry<CreativeModeTab> TAB = Reg.REGISTRATE.defaultCreativeTab("dataplanets",
                  builder -> builder.displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator("dataplanets", Reg.REGISTRATE))
                          .icon(()->DPItems.THEORY_2.asStack())
                          .title(Component.literal("Dataplanets"))
                          .build())
                  .register();


    public static void init(){}
}

package ace.actually.dataplanets.radio;

import ace.actually.radios.RadioSpec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class StaticRadioActions {

    public static final TagKey<Structure> WEATHER_LOW_BAND = TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath("dataplanets","weather_low_band"));
    public static void init()
    {
        RadioSpec.ACTIONS.add((action)->
        {
            if(action.band()<10)
            {
                BlockPos nearest = action.level().findNearestMapStructure(WEATHER_LOW_BAND,action.pos(), (int) Math.pow(action.band(),8),true);
                if(nearest!=null)
                {
                    RadioSpec.transmit(action.level(),action.pos(),action.band(),"WEATHER","");
                }
            }
        });
    }
}

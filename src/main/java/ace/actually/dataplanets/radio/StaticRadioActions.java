package ace.actually.dataplanets.radio;

import ace.actually.dataplanets.MutableTags;
import ace.actually.radios.RadioSpec;
import net.minecraft.core.BlockPos;

public class StaticRadioActions {
    public static void init()
    {
        RadioSpec.ACTIONS.add((action)->
        {
            if(action.band()<10)
            {
                BlockPos nearest = action.level().findNearestMapStructure(MutableTags.WEATHER_LOW_BAND,action.pos(), (int) Math.pow(action.band(),8),true);
                if(nearest!=null)
                {
                    RadioSpec.transmit(action.level(),action.pos(),action.band(),"WEATHER","");
                }
            }
        });
    }

}

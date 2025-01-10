package ace.actually.dataplanets;

import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

public class Reg {

    public static final GTRegistrate REGISTRATE = GTRegistrate.create(Dataplanets.MODID);

    static {
        Reg.REGISTRATE.creativeModeTab(() -> DPTabs.TAB);
    }
}

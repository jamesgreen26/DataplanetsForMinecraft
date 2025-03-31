package ace.actually.dataplanets.registry;

import ace.actually.dataplanets.blocks.ResearchStationBE;
import ace.actually.dataplanets.blocks.ResearchStationBlock;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;

public class DPBlocks {

    public static BlockEntry<ResearchStationBlock> RESEARCH_STATION = Reg.REGISTRATE.block("research_station", ResearchStationBlock::new).simpleItem().register();
    public static BlockEntityEntry<ResearchStationBE> RESEARCH_STATION_BE = Reg.REGISTRATE.blockEntity("research_station",ResearchStationBE::new).validBlock(RESEARCH_STATION).register();

    public static void init()
    {

    }
}

package ace.actually.dataplanets.registry;

import ace.actually.dataplanets.items.PortableTelescopeItem;
import ace.actually.dataplanets.items.ResearchItem;
import ace.actually.dataplanets.items.TestItem;
import ace.actually.dataplanets.items.TheoryItem;
import com.tterrag.registrate.util.entry.ItemEntry;

public class DPItems {

    static {
        Reg.REGISTRATE.creativeModeTab(() -> DPTabs.TAB);
    }

    public static ItemEntry<TheoryItem> THEORY_VISIBLE = Reg.REGISTRATE.item("theory_visible", a->new TheoryItem(a,1)).properties((a)->a).lang("Visible Theory").register();
    public static ItemEntry<TheoryItem> THEORY_RADIO = Reg.REGISTRATE.item("theory_radio", a->new TheoryItem(a,1)).properties((a)->a).lang("Radio Theory").register();
    public static ItemEntry<TheoryItem> THEORY_2 = Reg.REGISTRATE.item("theory2", a->new TheoryItem(a,2)).properties((a)->a).lang("Theory 2").register();
    public static ItemEntry<PortableTelescopeItem> PORTABLE_TELESCOPE = Reg.REGISTRATE.item("portable_telescope", PortableTelescopeItem::new).properties((a)->a).register();
    public static ItemEntry<TestItem> TEST_ITEM = Reg.REGISTRATE.item("test_item",TestItem::new).properties((a)->a).lang("Test Item").register();
    public static ItemEntry<ResearchItem> RESEARCH = Reg.REGISTRATE.item("research", ResearchItem::new).properties((a)->a).register();

    public static void init()
    {

    }
}

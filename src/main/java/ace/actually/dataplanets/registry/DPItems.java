package ace.actually.dataplanets.registry;

import ace.actually.dataplanets.items.FieldReserchItem;
import ace.actually.dataplanets.items.TelescopeItem;
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
    public static ItemEntry<FieldReserchItem> FIELD_RESEARCH = Reg.REGISTRATE.item("field_research", FieldReserchItem::new).properties((a)->a).lang("Field Research").register();
    public static ItemEntry<TelescopeItem> TELESCOPE = Reg.REGISTRATE.item("telescope", TelescopeItem::new).properties((a)->a).lang("Telescope").register();
    public static ItemEntry<TestItem> TEST_ITEM = Reg.REGISTRATE.item("test_item",TestItem::new).properties((a)->a).lang("Test Item").register();

    public static void init()
    {

    }
}

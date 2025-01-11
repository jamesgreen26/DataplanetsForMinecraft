package ace.actually.dataplanets;

import ace.actually.dataplanets.items.TelescopeItem;
import ace.actually.dataplanets.items.TheoryItem;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class DPItems {

    static {
        Reg.REGISTRATE.creativeModeTab(() -> DPTabs.TAB);
    }

    public static ItemEntry<TheoryItem> THEORY_VISIBLE = Reg.REGISTRATE.item("theory_visible", a->new TheoryItem(a,1)).properties((a)->a).lang("visible theory").register();
    public static ItemEntry<TheoryItem> THEORY_RADIO = Reg.REGISTRATE.item("theory_radio", a->new TheoryItem(a,1)).properties((a)->a).lang("Radio Theory").register();
    public static ItemEntry<TheoryItem> THEORY_2 = Reg.REGISTRATE.item("theory2", a->new TheoryItem(a,2)).properties((a)->a).lang("Theory 2").register();
    public static ItemEntry<TelescopeItem> TELESCOPE = Reg.REGISTRATE.item("theory_visible", TelescopeItem::new).properties((a)->a).lang("Visible Theory").register();

    public static void init()
    {

    }
}

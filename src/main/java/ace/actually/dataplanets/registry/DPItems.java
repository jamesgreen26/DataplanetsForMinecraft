package ace.actually.dataplanets.registry;

import ace.actually.dataplanets.items.*;
import com.tterrag.registrate.util.entry.ItemEntry;

public class DPItems {

    static {
        Reg.REGISTRATE.defaultCreativeTab("dataplanets").register();
    }

    public static ItemEntry<TheoryItem> THEORY = Reg.REGISTRATE.item("theory", a->new TheoryItem(a,1)).properties((a)->a).lang("Visible Theory").register();
    public static ItemEntry<PortableTelescopeItem> PORTABLE_TELESCOPE = Reg.REGISTRATE.item("portable_telescope", PortableTelescopeItem::new).properties((a)->a).register();
    public static ItemEntry<ResearchItem> RESEARCH = Reg.REGISTRATE.item("research", ResearchItem::new).properties((a)->a).register();
    public static ItemEntry<TestItem> TEST_ITEM = Reg.REGISTRATE.item("test_item",TestItem::new).properties((a)->a).lang("Test Item").register();
    public static ItemEntry<TaskListItem> TASK_LIST = Reg.REGISTRATE.item("task_list",TaskListItem::new).properties((a)->a).lang("Task List").register();


    public static void init() {}
}

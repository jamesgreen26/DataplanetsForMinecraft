package ace.actually.dataplanets.registry;

import ace.actually.dataplanets.entities.NeumEntity;
import ace.actually.dataplanets.entities.NeumEntityRenderer;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;

public class DPEntities {

    public static EntityEntry<NeumEntity> NEUM = Reg.REGISTRATE.entity("neum",NeumEntity::new, MobCategory.CREATURE)
            .attributes(Mob::createMobAttributes)
            .renderer(()-> NeumEntityRenderer::new)
            .lang("Neum").register();

    public static void init() {}
}

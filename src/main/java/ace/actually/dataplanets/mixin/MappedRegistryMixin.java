package ace.actually.dataplanets.mixin;

import ace.actually.dataplanets.interfaces.IUnfreezableRegistry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MappedRegistry.class)
public class MappedRegistryMixin implements IUnfreezableRegistry {

    @Shadow private boolean frozen;

    @Override
    public boolean isRegFrozen() {
        return frozen;
    }

    @Override
    public void setRegFrozen(boolean v) {
        frozen=v;
    }
}

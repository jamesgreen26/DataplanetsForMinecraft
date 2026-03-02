package shipwrights.dataplanets.mixin;

import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import shipwrights.genesis.space.Celestial;
import shipwrights.genesis.space.registry.SpaceRegistry;

@Mixin(SpaceRegistry.class)
public interface SpaceRegistryInvoker {

    @Invoker("addCelestial")
    void addCelestial(ResourceLocation id, Celestial it);
}

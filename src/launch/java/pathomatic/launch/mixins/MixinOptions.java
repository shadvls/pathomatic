package pathomatic.launch.mixins;

import pathomatic.gui.PathomaticKeys;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(Options.class)
public class MixinOptions {

    @Shadow
    private KeyMapping[] keyMappings;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void registerPathomaticKeys(CallbackInfo ci) {
        KeyMapping[] old = this.keyMappings;
        KeyMapping[] extended = Arrays.copyOf(old, old.length + 1);
        extended[old.length] = PathomaticKeys.OPEN_GUI;
        this.keyMappings = extended;
    }
}

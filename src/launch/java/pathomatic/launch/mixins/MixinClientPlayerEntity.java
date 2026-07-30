/*
 * This file is part of Pathomatic.
 *
 * Pathomatic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Pathomatic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Pathomatic.  If not, see <https://www.gnu.org/licenses/>.
 */

package pathomatic.launch.mixins;

import pathomatic.api.PathomaticAPI;
import pathomatic.api.IPathomatic;
import pathomatic.api.event.events.PlayerUpdateEvent;
import pathomatic.api.event.events.SprintStateEvent;
import pathomatic.api.event.events.type.EventState;
import pathomatic.behavior.LookBehavior;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Abilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @since 8/1/2018
 */
@Mixin(LocalPlayer.class)
public class MixinClientPlayerEntity {
    @Unique
    private static final MethodHandle MAY_FLY = pathomatic$resolveMayFly();

    @Unique
    private static MethodHandle pathomatic$resolveMayFly() {
        try {
            var lookup = MethodHandles.publicLookup();
            return lookup.findVirtual(LocalPlayer.class, "mayFly", MethodType.methodType(boolean.class));
        } catch (NoSuchMethodException e) {
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/player/AbstractClientPlayer.tick()V",
                    shift = At.Shift.AFTER
            )
    )
    private void onPreUpdate(CallbackInfo ci) {
        IPathomatic pathomatic = PathomaticAPI.getProvider().getPathomaticForPlayer((LocalPlayer) (Object) this);
        if (pathomatic != null) {
            pathomatic.getGameEventHandler().onPlayerUpdate(new PlayerUpdateEvent(EventState.PRE));
        }
    }

    @Redirect(
            method = "aiStep",
            at = @At(
                    value = "FIELD",
                    target = "net/minecraft/world/entity/player/Abilities.mayfly:Z"
            )
    )
    @Group(name = "mayFly", min = 1, max = 1)
    private boolean isAllowFlying(Abilities capabilities) {
        IPathomatic pathomatic = PathomaticAPI.getProvider().getPathomaticForPlayer((LocalPlayer) (Object) this);
        if (pathomatic == null) {
            return capabilities.mayfly;
        }
        return !pathomatic.getPathingBehavior().isPathing() && capabilities.mayfly;
    }

    @Redirect(
        method = "aiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;mayFly()Z"
        )
    )
    @Group(name = "mayFly", min = 1, max = 1)
    private boolean onMayFlyNeoforge(LocalPlayer instance) throws Throwable {
        IPathomatic pathomatic = PathomaticAPI.getProvider().getPathomaticForPlayer((LocalPlayer) (Object) this);
        if (pathomatic == null) {
            return (boolean) MAY_FLY.invokeExact(instance);
        }
        return !pathomatic.getPathingBehavior().isPathing() && (boolean) MAY_FLY.invokeExact(instance);
    }

    @Redirect(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/KeyMapping.isDown()Z"
            )
    )
    private boolean isKeyDown(KeyMapping keyBinding) {
        IPathomatic pathomatic = PathomaticAPI.getProvider().getPathomaticForPlayer((LocalPlayer) (Object) this);
        if (pathomatic == null) {
            return keyBinding.isDown();
        }
        SprintStateEvent event = new SprintStateEvent();
        pathomatic.getGameEventHandler().onPlayerSprintState(event);
        if (event.getState() != null) {
            return event.getState();
        }
        if (pathomatic != PathomaticAPI.getProvider().getPrimaryPathomatic()) {
            // hitting control shouldn't make all bots sprint
            return false;
        }
        return keyBinding.isDown();
    }

    @Inject(
            method = "rideTick",
            at = @At(
                    value = "HEAD"
            )
    )
    private void updateRidden(CallbackInfo cb) {
        IPathomatic pathomatic = PathomaticAPI.getProvider().getPathomaticForPlayer((LocalPlayer) (Object) this);
        if (pathomatic != null) {
            ((LookBehavior) pathomatic.getLookBehavior()).pig();
        }
    }

    @Redirect(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;tryToStartFallFlying()Z"
            )
    )
    private boolean tryToStartFallFlying(final LocalPlayer instance) {
        IPathomatic pathomatic = PathomaticAPI.getProvider().getPathomaticForPlayer(instance);
        if (pathomatic != null && pathomatic.getPathingBehavior().isPathing()) {
            return false;
        }
        return instance.tryToStartFallFlying();
    }
}

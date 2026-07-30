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
import pathomatic.api.event.events.ChatEvent;
import pathomatic.utils.accessor.IGuiScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.URI;

import static pathomatic.api.command.IPathomaticChatControl.FORCE_COMMAND_PREFIX;

@Mixin(Screen.class)
public abstract class MixinScreen implements IGuiScreen {

    //TODO: switch to enum extention with mixin 9.0 or whenever Mumfrey gets around to it
    @Inject(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V", remap = false, ordinal = 1), method = "handleComponentClicked", cancellable = true)
    public void handleCustomClickEvent(Style style, CallbackInfoReturnable<Boolean> cir) {
        ClickEvent clickEvent = style.getClickEvent();
        if (clickEvent == null) {
            return;
        }
        String command = clickEvent.getValue();
        if (command == null || !command.startsWith(FORCE_COMMAND_PREFIX)) {
            return;
        }
        IPathomatic pathomatic = PathomaticAPI.getProvider().getPrimaryPathomatic();
        if (pathomatic != null) {
            pathomatic.getGameEventHandler().onSendChatMessage(new ChatEvent(command));
        }
        cir.setReturnValue(true);
        cir.cancel();
    }
}

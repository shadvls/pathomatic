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

package pathomatic.api.utils.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PathomaticToast implements Toast {
    private String title;
    private String subtitle;
    private long firstDrawTime;
    private boolean newDisplay;
    private long totalShowTime;

    public PathomaticToast(Component titleComponent, Component subtitleComponent, long totalShowTime) {
        this.title = titleComponent.getString();
        this.subtitle = subtitleComponent == null ? null : subtitleComponent.getString();
        this.totalShowTime = totalShowTime;
    }

    public Visibility render(GuiGraphics gui, ToastComponent toastGui, long delta) {
        if (this.newDisplay) {
            this.firstDrawTime = delta;
            this.newDisplay = false;
        }


        //TODO: check
        gui.blit(ResourceLocation.parse("textures/gui/toasts.png"), 0, 0, 0, 32, 160, 32);

        if (this.subtitle == null) {
            gui.drawString(toastGui.getMinecraft().font, this.title, 18, 12, -11534256);
        } else {
            gui.drawString(toastGui.getMinecraft().font, this.title, 18, 7, -11534256);
            gui.drawString(toastGui.getMinecraft().font, this.subtitle, 18, 18, -16777216);
        }

        return delta - this.firstDrawTime < totalShowTime ? Visibility.SHOW : Visibility.HIDE;
    }

    public void setDisplayedText(Component titleComponent, Component subtitleComponent) {
        this.title = titleComponent.getString();
        this.subtitle = subtitleComponent == null ? null : subtitleComponent.getString();
        this.newDisplay = true;
    }

    public static void addOrUpdate(ToastComponent toast, Component title, Component subtitle, long totalShowTime) {
        PathomaticToast pathomatoast = toast.getToast(PathomaticToast.class, new Object());

        if (pathomatoast == null) {
            toast.addToast(new PathomaticToast(title, subtitle, totalShowTime));
        } else {
            pathomatoast.setDisplayedText(title, subtitle);
        }
    }

    public static void addOrUpdate(Component title, Component subtitle) {
        addOrUpdate(Minecraft.getInstance().getToasts(), title, subtitle, pathomatic.api.PathomaticAPI.getSettings().toastTimer.value);
    }
}

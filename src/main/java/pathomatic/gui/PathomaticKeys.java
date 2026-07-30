package pathomatic.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class PathomaticKeys {

    public static final String CATEGORY = "key.categories.pathomatic";

    public static final KeyMapping OPEN_GUI = new KeyMapping(
        "key.pathomatic.gui",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_P,
        CATEGORY
    );
}
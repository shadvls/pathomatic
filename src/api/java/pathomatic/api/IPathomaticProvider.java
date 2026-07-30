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

package pathomatic.api;

import pathomatic.api.cache.IWorldScanner;
import pathomatic.api.command.ICommand;
import pathomatic.api.command.ICommandSystem;
import pathomatic.api.schematic.ISchematicSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;

import java.util.List;
import java.util.Objects;

/**
 * Provides the present {@link IPathomatic} instances, as well as non-pathomatic instance related APIs.
 *
 */
public interface IPathomaticProvider {

    /**
     * Returns the primary {@link IPathomatic} instance. This instance is persistent, and
     * is represented by the local player that is created by the game itself, not a "bot"
     * player through Pathomatic.
     *
     * @return The primary {@link IPathomatic} instance.
     */
    IPathomatic getPrimaryPathomatic();

    /**
     * Returns all of the active {@link IPathomatic} instances. This includes the local one
     * returned by {@link #getPrimaryPathomatic()}.
     *
     * @return All active {@link IPathomatic} instances.
     * @see #getPathomaticForPlayer(LocalPlayer)
     */
    List<IPathomatic> getAllPathomatics();

    /**
     * Provides the {@link IPathomatic} instance for a given {@link LocalPlayer}.
     *
     * @param player The player
     * @return The {@link IPathomatic} instance.
     */
    default IPathomatic getPathomaticForPlayer(LocalPlayer player) {
        for (IPathomatic pathomatic : this.getAllPathomatics()) {
            if (Objects.equals(player, pathomatic.getPlayerContext().player())) {
                return pathomatic;
            }
        }
        return null;
    }

    /**
     * Provides the {@link IPathomatic} instance for a given {@link Minecraft}.
     *
     * @param minecraft The minecraft
     * @return The {@link IPathomatic} instance.
     */
    default IPathomatic getPathomaticForMinecraft(Minecraft minecraft) {
        for (IPathomatic pathomatic : this.getAllPathomatics()) {
            if (Objects.equals(minecraft, pathomatic.getPlayerContext().minecraft())) {
                return pathomatic;
            }
        }
        return null;
    }

    /**
     * Provides the {@link IPathomatic} instance for the player with the specified connection.
     *
     * @param connection The connection
     * @return The {@link IPathomatic} instance.
     */
    default IPathomatic getPathomaticForConnection(ClientPacketListener connection) {
        for (IPathomatic pathomatic : this.getAllPathomatics()) {
            final LocalPlayer player = pathomatic.getPlayerContext().player();
            if (player != null && player.connection == connection) {
                return pathomatic;
            }
        }
        return null;
    }

    /**
     * Creates and registers a new {@link IPathomatic} instance using the specified {@link Minecraft}. The existing
     * instance is returned if already registered.
     *
     * @param minecraft The minecraft
     * @return The {@link IPathomatic} instance
     */
    IPathomatic createPathomatic(Minecraft minecraft);

    /**
     * Destroys and removes the specified {@link IPathomatic} instance. If the specified instance is the
     * {@link #getPrimaryPathomatic() primary pathomatic}, this operation has no effect and will return {@code false}.
     *
     * @param pathomatic The pathomatic instance to remove
     * @return Whether the pathomatic instance was removed
     */
    boolean destroyPathomatic(IPathomatic pathomatic);

    /**
     * Returns the {@link IWorldScanner} instance. This is not a type returned by
     * {@link IPathomatic} implementation, because it is not linked with {@link IPathomatic}.
     *
     * @return The {@link IWorldScanner} instance.
     */
    IWorldScanner getWorldScanner();

    /**
     * Returns the {@link ICommandSystem} instance. This is not bound to a specific {@link IPathomatic}
     * instance because {@link ICommandSystem} itself controls global behavior for {@link ICommand}s.
     *
     * @return The {@link ICommandSystem} instance.
     */
    ICommandSystem getCommandSystem();

    /**
     * @return The {@link ISchematicSystem} instance.
     */
    ISchematicSystem getSchematicSystem();
}

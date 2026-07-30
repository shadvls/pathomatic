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

package pathomatic.cache;

import pathomatic.Pathomatic;
import pathomatic.api.cache.ICachedWorld;
import pathomatic.api.cache.IWaypointCollection;
import pathomatic.api.cache.IWorldData;
import net.minecraft.world.level.dimension.DimensionType;

import java.nio.file.Path;

/**
 * Data about a world, from pathomatic's point of view. Includes cached chunks, waypoints, and map data.
 *
 */
public class WorldData implements IWorldData {

    public final CachedWorld cache;
    private final WaypointCollection waypoints;
    //public final MapData map;
    public final Path directory;
    public final DimensionType dimension;

    WorldData(Path directory, DimensionType dimension) {
        this.directory = directory;
        this.cache = new CachedWorld(directory.resolve("cache"), dimension);
        this.waypoints = new WaypointCollection(directory.resolve("waypoints"));
        this.dimension = dimension;
    }

    public void onClose() {
        Pathomatic.getExecutor().execute(() -> {
            System.out.println("Started saving the world in a new thread");
            cache.save();
        });
    }

    @Override
    public ICachedWorld getCachedWorld() {
        return this.cache;
    }

    @Override
    public IWaypointCollection getWaypoints() {
        return this.waypoints;
    }
}

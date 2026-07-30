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

package pathomatic;

import pathomatic.api.IPathomatic;
import pathomatic.api.IPathomaticProvider;
import pathomatic.api.cache.IWorldScanner;
import pathomatic.api.command.ICommandSystem;
import pathomatic.api.schematic.ISchematicSystem;
import pathomatic.cache.FasterWorldScanner;
import pathomatic.command.CommandSystem;
import pathomatic.command.ExamplePathomaticControl;
import pathomatic.utils.schematic.SchematicSystem;
import net.minecraft.client.Minecraft;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @since 9/29/2018
 */
public final class PathomaticProvider implements IPathomaticProvider {

    private final List<IPathomatic> all;
    private final List<IPathomatic> allView;

    public PathomaticProvider() {
        this.all = new CopyOnWriteArrayList<>();
        this.allView = Collections.unmodifiableList(this.all);

        // Setup chat control, just for the primary instance
        final Pathomatic primary = (Pathomatic) this.createPathomatic(Minecraft.getInstance());
        primary.registerBehavior(ExamplePathomaticControl::new);
    }

    @Override
    public IPathomatic getPrimaryPathomatic() {
        return this.all.get(0);
    }

    @Override
    public List<IPathomatic> getAllPathomatics() {
        return this.allView;
    }

    @Override
    public synchronized IPathomatic createPathomatic(Minecraft minecraft) {
        IPathomatic pathomatic = this.getPathomaticForMinecraft(minecraft);
        if (pathomatic == null) {
            this.all.add(pathomatic = new Pathomatic(minecraft));
        }
        return pathomatic;
    }

    @Override
    public synchronized boolean destroyPathomatic(IPathomatic pathomatic) {
        return pathomatic != this.getPrimaryPathomatic() && this.all.remove(pathomatic);
    }

    @Override
    public IWorldScanner getWorldScanner() {
        return FasterWorldScanner.INSTANCE;
    }

    @Override
    public ICommandSystem getCommandSystem() {
        return CommandSystem.INSTANCE;
    }

    @Override
    public ISchematicSystem getSchematicSystem() {
        return SchematicSystem.INSTANCE;
    }
}

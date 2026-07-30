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

package pathomatic.utils;

import pathomatic.Pathomatic;
import pathomatic.api.process.IPathomaticProcess;
import pathomatic.api.utils.Helper;
import pathomatic.api.utils.IPlayerContext;

public abstract class PathomaticProcessHelper implements IPathomaticProcess, Helper {

    protected final Pathomatic pathomatic;
    protected final IPlayerContext ctx;

    public PathomaticProcessHelper(Pathomatic pathomatic) {
        this.pathomatic = pathomatic;
        this.ctx = pathomatic.getPlayerContext();
    }

    @Override
    public boolean isTemporary() {
        return false;
    }
}

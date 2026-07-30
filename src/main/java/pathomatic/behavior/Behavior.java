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

package pathomatic.behavior;

import pathomatic.Pathomatic;
import pathomatic.api.behavior.IBehavior;
import pathomatic.api.utils.IPlayerContext;

/**
 * A type of game event listener that is given {@link Pathomatic} instance context.
 *
 * @since 8/1/2018
 */
public class Behavior implements IBehavior {

    public final Pathomatic pathomatic;
    public final IPlayerContext ctx;

    protected Behavior(Pathomatic pathomatic) {
        this.pathomatic = pathomatic;
        this.ctx = pathomatic.getPlayerContext();
    }
}

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

package pathomatic.api.pathing.calc;

import pathomatic.api.process.IPathomaticProcess;
import pathomatic.api.process.PathingCommand;

import java.util.Optional;

/**
 */
public interface IPathingControlManager {

    /**
     * Registers a process with this pathing control manager. See {@link IPathomaticProcess} for more details.
     *
     * @param process The process
     * @see IPathomaticProcess
     */
    void registerProcess(IPathomaticProcess process);

    /**
     * @return The most recent {@link IPathomaticProcess} that had control
     */
    Optional<IPathomaticProcess> mostRecentInControl();

    /**
     * @return The most recent pathing command executed
     */
    Optional<PathingCommand> mostRecentCommand();
}

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

package pathomatic.command;

import pathomatic.api.command.ICommandSystem;
import pathomatic.api.command.argparser.IArgParserManager;
import pathomatic.command.argparser.ArgParserManager;

/**
 * @since 10/4/2019
 */
public enum CommandSystem implements ICommandSystem {
    INSTANCE;

    @Override
    public IArgParserManager getParserManager() {
        return ArgParserManager.INSTANCE;
    }
}

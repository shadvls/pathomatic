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

package pathomatic.command.defaults;

import pathomatic.api.IPathomatic;
import pathomatic.api.command.Command;
import pathomatic.api.command.argument.IArgConsumer;
import pathomatic.api.command.exception.CommandException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class SaveAllCommand extends Command {

    public SaveAllCommand(IPathomatic pathomatic) {
        super(pathomatic, "saveall");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        args.requireMax(0);
        ctx.worldData().getCachedWorld().save();
        logDirect("Saved");
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) {
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "Saves Pathomatic's cache for this world";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList(
                "The saveall command saves Pathomatic's world cache.",
                "",
                "Usage:",
                "> saveall"
        );
    }
}

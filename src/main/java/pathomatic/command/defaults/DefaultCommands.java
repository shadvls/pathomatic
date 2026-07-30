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
import pathomatic.api.command.ICommand;

import java.util.*;

public final class DefaultCommands {

    private DefaultCommands() {
    }

    public static List<ICommand> createAll(IPathomatic pathomatic) {
        Objects.requireNonNull(pathomatic);
        List<ICommand> commands = new ArrayList<>(Arrays.asList(
                new HelpCommand(pathomatic),
                new SetCommand(pathomatic),
                new CommandAlias(pathomatic, Arrays.asList("modified", "mod", "pathomatic", "modifiedsettings"), "List modified settings", "set modified"),
                new CommandAlias(pathomatic, "reset", "Reset all settings or just one", "set reset"),
                new GoalCommand(pathomatic),
                new GotoCommand(pathomatic),
                new PathCommand(pathomatic),
                new ProcCommand(pathomatic),
                new ETACommand(pathomatic),
                new VersionCommand(pathomatic),
                new RepackCommand(pathomatic),
                new BuildCommand(pathomatic),
                //new SchematicaCommand(pathomatic),
                new LitematicaCommand(pathomatic),
                new ComeCommand(pathomatic),
                new AxisCommand(pathomatic),
                new ForceCancelCommand(pathomatic),
                new GcCommand(pathomatic),
                new InvertCommand(pathomatic),
                new TunnelCommand(pathomatic),
                new RenderCommand(pathomatic),
                new FarmCommand(pathomatic),
                new FollowCommand(pathomatic),
                new PickupCommand(pathomatic),
                new ExploreFilterCommand(pathomatic),
                new ReloadAllCommand(pathomatic),
                new SaveAllCommand(pathomatic),
                new ExploreCommand(pathomatic),
                new BlacklistCommand(pathomatic),
                new FindCommand(pathomatic),
                new MineCommand(pathomatic),
                new ClickCommand(pathomatic),
                new SurfaceCommand(pathomatic),
                new ThisWayCommand(pathomatic),
                new WaypointsCommand(pathomatic),
                new CommandAlias(pathomatic, "sethome", "Sets your home waypoint", "waypoints save home"),
                new CommandAlias(pathomatic, "home", "Path to your home waypoint", "waypoints goto home"),
                new SelCommand(pathomatic),
                new ElytraCommand(pathomatic)
        ));
        ExecutionControlCommands prc = new ExecutionControlCommands(pathomatic);
        commands.add(prc.pauseCommand);
        commands.add(prc.resumeCommand);
        commands.add(prc.pausedCommand);
        commands.add(prc.cancelCommand);
        return Collections.unmodifiableList(commands);
    }
}

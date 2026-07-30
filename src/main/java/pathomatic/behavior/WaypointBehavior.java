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
import pathomatic.api.cache.IWaypoint;
import pathomatic.api.cache.Waypoint;
import pathomatic.api.event.events.BlockInteractEvent;
import pathomatic.api.utils.BetterBlockPos;
import pathomatic.api.utils.Helper;
import pathomatic.utils.BlockStateInterface;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import java.util.Set;

import static pathomatic.api.command.IPathomaticChatControl.FORCE_COMMAND_PREFIX;

public class WaypointBehavior extends Behavior {


    public WaypointBehavior(Pathomatic pathomatic) {
        super(pathomatic);
    }

    @Override
    public void onBlockInteract(BlockInteractEvent event) {
        if (!Pathomatic.settings().doBedWaypoints.value)
            return;
        if (event.getType() == BlockInteractEvent.Type.USE) {
            BetterBlockPos pos = BetterBlockPos.from(event.getPos());
            BlockState state = BlockStateInterface.get(ctx, pos);
            if (state.getBlock() instanceof BedBlock) {
                if (state.getValue(BedBlock.PART) == BedPart.FOOT) {
                    pos = pos.relative(state.getValue(BedBlock.FACING));
                }
                Set<IWaypoint> waypoints = pathomatic.getWorldProvider().getCurrentWorld().getWaypoints().getByTag(IWaypoint.Tag.BED);
                boolean exists = waypoints.stream().map(IWaypoint::getLocation).filter(pos::equals).findFirst().isPresent();
                if (!exists) {
                    pathomatic.getWorldProvider().getCurrentWorld().getWaypoints().addWaypoint(new Waypoint("bed", Waypoint.Tag.BED, pos));
                }
            }
        }
    }

    @Override
    public void onPlayerDeath() {
        if (!Pathomatic.settings().doDeathWaypoints.value)
            return;
        Waypoint deathWaypoint = new Waypoint("death", Waypoint.Tag.DEATH, ctx.playerFeet());
        pathomatic.getWorldProvider().getCurrentWorld().getWaypoints().addWaypoint(deathWaypoint);
        MutableComponent component = Component.literal("Death position saved.");
        component.setStyle(component.getStyle()
                .withColor(ChatFormatting.WHITE)
                .withHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        Component.literal("Click to goto death")
                ))
                .withClickEvent(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        String.format(
                                "%s%s goto %s @ %d",
                                FORCE_COMMAND_PREFIX,
                                "wp",
                                deathWaypoint.getTag().getName(),
                                deathWaypoint.getCreationTimestamp()
                        )
                )));
        Helper.HELPER.logDirect(component);
    }

}

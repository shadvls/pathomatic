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

package pathomatic.api.command.datatypes;

import pathomatic.api.command.exception.CommandException;
import pathomatic.api.command.helpers.TabCompleteHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.stream.Stream;

public enum EntityClassById implements IDatatypeFor<EntityType> {
    INSTANCE;

    @Override
    public EntityType get(IDatatypeContext ctx) throws CommandException {
        ResourceLocation id = ResourceLocation.parse(ctx.getConsumer().getString());
        EntityType entity;
        if ((entity = BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(null)) == null) {
            throw new IllegalArgumentException("no entity found by that id");
        }
        return entity;
    }

    @Override
    public Stream<String> tabComplete(IDatatypeContext ctx) throws CommandException {
        return new TabCompleteHelper()
                .append(BuiltInRegistries.ENTITY_TYPE.stream().map(Object::toString))
                .filterPrefixNamespaced(ctx.getConsumer().getString())
                .sortAlphabetically()
                .stream();
    }
}

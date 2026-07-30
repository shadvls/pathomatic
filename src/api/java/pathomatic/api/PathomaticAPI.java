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

package pathomatic.api;

import pathomatic.api.utils.SettingsUtil;

/**
 * Exposes the {@link IPathomaticProvider} instance and the {@link Settings} instance for API usage.
 *
 * @since 9/23/2018
 */
public final class PathomaticAPI {

    private static final IPathomaticProvider provider;
    private static final Settings settings;

    static {
        settings = new Settings();
        SettingsUtil.readAndApply(settings, SettingsUtil.SETTINGS_DEFAULT_NAME);

        try {
            provider = (IPathomaticProvider) Class.forName("pathomatic.PathomaticProvider").newInstance();
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static IPathomaticProvider getProvider() {
        return PathomaticAPI.provider;
    }

    public static Settings getSettings() {
        return PathomaticAPI.settings;
    }
}

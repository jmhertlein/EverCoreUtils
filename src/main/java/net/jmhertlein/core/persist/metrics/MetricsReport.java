/*
 * Copyright (C) 2013 Joshua M Hertlein <jmhertlein@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.jmhertlein.core.persist.metrics;

import java.util.LinkedList;
import java.util.List;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 *
 * @author joshua
 */
public class MetricsReport {
    private final FileConfiguration f;
    
    public MetricsReport(Plugin p) {
        PluginDescriptionFile desc = p.getDescription();
        f = new YamlConfiguration();
        f.set("plugin.name", desc.getName());
        f.set("plugin.version", desc.getVersion());
        
        f.set("server.implementation.version", p.getServer().getVersion());
        f.set("server.implementation.name", p.getServer().getName());
        f.set("server.bukkit.version", p.getServer().getBukkitVersion());
        
        List<String> pluginNames = new LinkedList<>();
        for(Plugin plug : p.getServer().getPluginManager().getPlugins()) {
            pluginNames.add(plug.getName());
        }
        f.set("server.plugins", pluginNames);
        
        f.set("os.name", System.getProperty("os.name"));
        f.set("os.arch", System.getProperty("os.arch"));
        f.set("os.version", System.getProperty("os.version"));
        f.set("java.vendor", System.getProperty("java.vendor"));
        f.set("java.version", System.getProperty("java.version"));
        f.set("java.vendor.url", System.getProperty("java.vendor.url"));
    }
    
    public MetricsReport(String serializedString) throws InvalidConfigurationException {
        f = new YamlConfiguration();
        f.loadFromString(serializedString);
    }

    @Override
    public String toString() {
        return f.saveToString();
    } 
    
    //TODO: add getters for each KV pair stored in the YAML
}

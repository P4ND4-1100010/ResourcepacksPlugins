package de.themoep.resourcepacksplugin.velocity;

/*
 * ResourcepacksPlugins - velocity
 * Copyright (C) 2020 Max Lee aka Phoenix616 (mail@moep.tv)
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

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;
import com.typesafe.config.ConfigValueType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class PluginConfig {
    private final VelocityResourcepacks plugin;
    private final File configFile;
    private final String defaultFile;
    private Config config;

    public PluginConfig(VelocityResourcepacks plugin, File configFile) {
        this(plugin, configFile, configFile.getName());
    }

    public PluginConfig(VelocityResourcepacks plugin, File configFile, String defaultFile) {
        this.plugin = plugin;
        this.configFile = configFile;
        this.defaultFile = defaultFile;
    }

    public boolean load() {
        try {
            config = ConfigFactory.parseFile(configFile);
            if (defaultFile != null) {
                config = config.withFallback(ConfigFactory.load(defaultFile));;
            }
            plugin.getLogger().log(Level.INFO, "Loaded " + configFile.getName());
            return true;
        } catch (ConfigException e) {
            plugin.getLogger().log(Level.SEVERE, "Unable to load configuration file " + configFile.getName(), e);
            return false;
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(config.root().render());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object set(String path, Object value) {
        ConfigValue prev = config.root().put(path, ConfigValueFactory.fromAnyRef(value));
        return prev != null ? prev.unwrapped() : null;
    }

    public ConfigValue remove(String path) {
        return config.root().remove(path);
    }

    public Config getRawConfig() {
        return config;
    }

    public Config getRawConfig(String path) {
        return config.getConfig(path);
    }

    public boolean has(String path) {
        return config.hasPath(path);
    }

    public boolean isSection(String path) {
        try {
            return config.getValue(path).valueType() == ConfigValueType.OBJECT;
        } catch (ConfigException e) {
            return false;
        }
    }
    
    public int getInt(String path) {
        return getInt(path, 0);
    }

    public int getInt(String path, int def) {
        try {
            return config.getInt(path);
        } catch (ConfigException e) {
            return def;
        }
    }
    
    public double getDouble(String path) {
        return getDouble(path, 0);
    }

    public double getDouble(String path, double def) {
        try {
            return config.getDouble(path);
        } catch (ConfigException e) {
            return def;
        }
    }
    
    public String getString(String path) {
        return getString(path, null);
    }

    public String getString(String path, String def) {
        try {
            return config.getString(path);
        } catch (ConfigException e) {
            return def;
        }
    }
    
    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

    public boolean getBoolean(String path, boolean def) {
        try {
            return config.getBoolean(path);
        } catch (ConfigException e) {
            return def;
        }
    }
}

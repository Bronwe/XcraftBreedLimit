package de.xcraft.INemesisI.BreedLimit.Manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import de.xcraft.INemesisI.BreedLimit.XcraftBreedLimit;
import de.xcraft.INemesisI.Library.Manager.XcraftConfigManager;

public class ConfigManager extends XcraftConfigManager {

	private FileConfiguration data;
	private File dataFile;

	public int radius;
	public final Map<EntityType, Integer> limits = new HashMap<EntityType, Integer>();
	private final Map<EntityType, String> messages = new HashMap<EntityType, String>();

	public ConfigManager(XcraftBreedLimit plugin) {
		super(plugin);
	}

	@Override
	public void load() {
		dataFile = new File(plugin.getDataFolder(), "data.yml");
		data = YamlConfiguration.loadConfiguration(dataFile);
		ConfigurationSection cs = config.getConfigurationSection("Breeding.Limits");
		for (String key : cs.getKeys(false)) {
			if (EntityType.valueOf(key) != null) {
				limits.put(EntityType.valueOf(key), cs.getInt(key));
			}
		}
		cs = config.getConfigurationSection("Breeding.Message");
		for (String key : cs.getKeys(false)) {
			if (key.equals("default") || EntityType.valueOf(key) != null) {
				EntityType type = key.equals("default") ? EntityType.UNKNOWN : EntityType.valueOf(key);
				messages.put(type, cs.getString(key));
			}
		}
		((PluginManager) plugin.getPluginManager()).setLicences(licences);
		this.radius = config.getInt("Breeding.Radius", 2);
	}

	@Override
	public void save() {
		PluginManager pManager = (PluginManager) plugin.getPluginManager();
		try {
			data.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getMessage(EntityType type) {
		if (messages.containsKey(type))
			return messages.get(type);
		else
			return messages.get(EntityType.UNKNOWN);
	}
}

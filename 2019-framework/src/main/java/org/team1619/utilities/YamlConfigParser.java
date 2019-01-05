package org.team1619.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.team1619.models.exceptions.ConfigurationException;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

public class YamlConfigParser {

	private static final Logger sLogger = LoggerFactory.getLogger(YamlConfigParser.class);

	private final Yaml fYaml = new Yaml();

	private Map<String, Map<String, Object>> fData = new HashMap<>();
	private Map<String, String> fNameTypes = new HashMap<>();


	public void load(String path) {
		sLogger.info("Loading config file '{}'", path);

		try {
			fData = fYaml.load(getClassLoader().getResourceAsStream(path + ".yaml"));
		} catch (Throwable t) {
			sLogger.error(t.getMessage());
		}

		sLogger.info("Loaded config file '{}'", path);

		fNameTypes = new HashMap<>();
		for (Map.Entry<String, Map<String, Object>> entry : fData.entrySet()) {
			for (String name : entry.getValue().keySet()) {
				fNameTypes.put(name, entry.getKey());
			}
		}
	}


	public Config getConfig(Object object) {
		String name = object.toString().toLowerCase();

		sLogger.info("Getting config with name '{}'", name);

		if (!(fNameTypes.containsKey(name) && fData.containsKey(fNameTypes.get(name)))) {
			throw new ConfigurationException("No config exists for name '" + name + "'");
		}

		String type = fNameTypes.get(name);
		try {
			return new Config(type, (Map) fData.get(type).get(name));
		} catch (ClassCastException ex) {
			throw new ConfigurationException("Expected map but found " + fData.getClass().getSimpleName());
		}
	}

	protected ClassLoader getClassLoader() {
		return YamlConfigParser.class.getClassLoader();
	}
}

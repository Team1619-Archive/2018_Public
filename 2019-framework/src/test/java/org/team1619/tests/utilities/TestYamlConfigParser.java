package org.team1619.tests.utilities;

import org.team1619.NonNullByDefault;
import org.team1619.utilities.YamlConfigParser;

@NonNullByDefault
public class TestYamlConfigParser extends YamlConfigParser {

	@Override
	protected ClassLoader getClassLoader() {
		return TestYamlConfigParser.class.getClassLoader();
	}
}

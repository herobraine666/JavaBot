package com.javadiscord.javabot.properties.command;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.Arrays;

public class SubCommandConfig {
	private String name;
	private String description;
	private OptionConfig[] options;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OptionConfig[] getOptions() {
		return options;
	}

	public void setOptions(OptionConfig[] options) {
		this.options = options;
	}

	public SubcommandData toData() {
		SubcommandData data = new SubcommandData(this.name, this.description);
		if (this.options != null) {
			for (OptionConfig oc : this.options) {
				data.addOptions(oc.toData());
			}
		}
		return data;
	}

	@Override
	public String toString() {
		return "SubCommandConfig{" +
			"name='" + name + '\'' +
			", description='" + description + '\'' +
			", options=" + Arrays.toString(options) +
			'}';
	}

	public static SubCommandConfig fromData(SubcommandData data) {
		SubCommandConfig c = new SubCommandConfig();
		c.setName(data.getName());
		c.setDescription(data.getDescription());
		c.setOptions(data.getOptions().stream().map(OptionConfig::fromData).toArray(OptionConfig[]::new));
		return c;
	}
}

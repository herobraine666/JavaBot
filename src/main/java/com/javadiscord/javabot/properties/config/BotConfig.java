package com.javadiscord.javabot.properties.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * The base container class for all the JavaBot's configuration.
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class BotConfig {
	private ModerationConfig moderationConfig;
	private QOTWConfig qotwConfig;
	private WelcomeConfig welcomeConfig;
	private StarBoardConfig starBoardConfig;
	private JamConfig jamConfig;

	/**
	 * The path from which the config was loaded.
	 */
	private transient Path filePath;

	/**
	 * Constructs a new empty configuration.
	 * @param filePath The path to the configuration file.
	 */
	public BotConfig(Path filePath) {
		this.filePath = filePath;
		this.moderationConfig = new ModerationConfig();
		this.qotwConfig = new QOTWConfig();
		this.welcomeConfig = new WelcomeConfig();
		this.starBoardConfig = new StarBoardConfig();
		this.jamConfig = new JamConfig();
	}

	/**
	 * Loads an instance of the configuration from the given path, or creates a
	 * new empty configuration file there if none exists yet.
	 * @param filePath The path to the configuration JSON file.
	 * @return The config that was loaded.
	 * @throws JsonSyntaxException if the config file's JSON is invalid.
	 * @throws UncheckedIOException if an IO error occurs.
	 */
	public static BotConfig loadOrCreate(Path filePath) {
		Gson gson = new GsonBuilder().create();
		BotConfig config;
		try (var reader = Files.newBufferedReader(filePath)) {
			config = gson.fromJson(reader, BotConfig.class);
			config.setFilePath(filePath);
			log.info("Loaded config from {}", filePath);
		} catch (JsonSyntaxException e) {
			log.error("Invalid JSON found! Please fix or remove config file " + filePath + " and restart.", e);
			throw e;
		} catch (NoSuchFileException e) {
			log.info("No config file found. Creating an empty one at {}", filePath);
			config = new BotConfig(filePath);
			config.flush();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return config;
	}

	/**
	 * Saves this config to its file path.
	 */
	public synchronized void flush() {
		Gson gson = new GsonBuilder()
				.serializeNulls()
				.setPrettyPrinting()
				.create();
		try (var writer = Files.newBufferedWriter(this.filePath)) {
			gson.toJson(this, writer);
			writer.flush();
		} catch (IOException e) {
			log.error("Could not flush config.", e);
		}
	}
}

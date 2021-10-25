package com.javadiscord.javabot.service.jam;

import com.javadiscord.javabot.commands.DelegatingCommandHandler;
import com.javadiscord.javabot.service.jam.subcommands.admin.*;

import java.util.Map;

public class JamAdminCommandHandler extends DelegatingCommandHandler {
	public JamAdminCommandHandler() {
		super(Map.of(
				"plan-new-jam", new PlanNewJamSubcommand(),
				"edit-jam", new EditJamSubcommand(),
				"add-theme", new AddThemeSubcommand(),
				"list-themes", new ListThemesSubcommand(),
				"remove-theme", new RemoveThemeSubcommand(),
				"next-phase", new NextPhaseSubcommand(),
				"list-submissions", new ListSubmissionsSubcommand(),
				"remove-submissions", new RemoveSubmissionsSubcommand(),
				"cancel", new CancelSubcommand()
		));
	}
}
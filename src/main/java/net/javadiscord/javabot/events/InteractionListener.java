package net.javadiscord.javabot.events;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.javadiscord.javabot.systems.help.HelpChannelInteractionManager;
import net.javadiscord.javabot.systems.moderation.BanCommand;
import net.javadiscord.javabot.systems.moderation.KickCommand;
import net.javadiscord.javabot.systems.moderation.UnbanCommand;

@Slf4j
public class InteractionListener extends ListenerAdapter {

	// TODO: add Context-Menu Commands (once they're available in JDA)

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		if (event.getUser().isBot()) return;
		event.deferEdit().queue();

		String[] id = event.getComponentId().split(":");
		switch (id[0]) {
			case "dm-submission" -> this.handleDmSubmission(event);
			case "submission" -> this.handleSubmission(event);
			case "reaction-role" -> this.handleReactionRoles(event);
			case "help-channel" -> new HelpChannelInteractionManager().handleHelpChannel(event, id[1], id[2]);
			case "help-thank" -> new HelpChannelInteractionManager().handleHelpThank(event, id[1], id[2]);
			case "utils" -> this.handleUtils(event);
		}
	}

	/**
	 * Some utility methods for interactions
	 * + May be useful for Context Menu Interactions
	 */
	private void handleUtils(ButtonClickEvent event) {
		String[] id = event.getComponentId().split(":");
		switch (id[1]) {
			case "delete" -> event.getHook().deleteOriginal().queue();
			case "kick" -> new KickCommand().handleKickInteraction(event.getGuild().getMemberById(id[2]), event).queue();
			case "ban" -> new BanCommand().handleBanInteraction(event.getGuild().getMemberById(id[2]), event).queue();
			case "unban" -> new UnbanCommand().handleUnbanInteraction(event, id[2]).queue();
		}
	}

	private void handleDmSubmission(ButtonClickEvent event) {
		String[] id = event.getComponentId().split(":");
		switch (id[1]) {
			case "send" -> new SubmissionListener().dmSubmissionSend(event);
			case "cancel" -> new SubmissionListener().dmSubmissionCancel(event);
		}
	}

	private void handleSubmission(ButtonClickEvent event) {
		String[] id = event.getComponentId().split(":");
		switch (id[1]) {
			case "approve" -> new SubmissionListener().submissionApprove(event);
			case "decline" -> new SubmissionListener().submissionDecline(event);
			case "getraw" -> new SubmissionListener().submissionGetRaw(event);
		}
	}

	private void handleReactionRoles(ButtonClickEvent event) {
		String[] id = event.getComponentId().split(":");
		String roleID = id[1];
		boolean permanent = Boolean.parseBoolean(id[2]);

		event.getGuild().retrieveMemberById(event.getUser().getId()).queue(member->{
			Role role = event.getGuild().getRoleById(roleID);

			if (member.getRoles().contains(role)) {
				if (!permanent) {
					event.getGuild().removeRoleFromMember(member, role).queue();
					event.getHook().sendMessage("Removed Role: " + role.getAsMention()).setEphemeral(true).queue();
				} else {
					event.getHook().sendMessage("You already have Role: " + role.getAsMention()).setEphemeral(true).queue();
				}
			} else {
				event.getGuild().addRoleToMember(member, role).queue();
				event.getHook().sendMessage("Added Role: " + role.getAsMention()).setEphemeral(true).queue();
			}
		});
	}
}
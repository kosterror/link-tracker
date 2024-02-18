package edu.java.bot.util.i18n;

import lombok.Getter;

/**
 * Перечисление, оборачивающая в себя ключи от {@code Resource Bundle}.
 */
@Getter
public enum MessageKey {
    COMMANDS_START_DESCRIPTION("commands.start.description"),
    COMMANDS_START_ANSWER("commands.start.answer"),

    COMMANDS_TRACK_ANSWER("commands.track.answer"),
    COMMANDS_TRACK_DESCRIPTION("commands.track.description"),
    COMMANDS_TRACK_ILLEGAL_COMMAND_FORMAT("commands.track.illegal_command_format"),
    COMMANDS_TRACK_ILLEGAL_LINK_FORMAT("commands.track.illegal_link_format"),
    COMMANDS_TRACK_LINK_ALREADY_TRACKED("commands.track.link_already_tracked"),

    COMMANDS_LIST_DESCRIPTION("commands.list.description"),
    COMMANDS_LIST_ANSWER("commands.list.answer"),
    COMMANDS_LIST_LIST_IS_EMPTY("commands.list.list_is_empty"),

    COMMANDS_UNTRACK_DESCRIPTION("commands.untrack.description"),
    COMMANDS_UNTRACK_ANSWER("commands.untrack.answer"),
    COMMANDS_UNTRACK_LINK_NOT_FOUND("commands.untrack.link_not_found"),
    COMMANDS_UNTRACK_ILLEGAL_COMMAND_FORMAT("commands.untrack.illegal_command_format"),

    INTERNAL_ERROR("commands.internal_error"),
    INVALID_COMMAND("commands.invalid_command");

    /**
     * Ключ от {@code Resource Bundle}.
     */
    private final String code;

    MessageKey(String code) {
        this.code = code;
    }

}

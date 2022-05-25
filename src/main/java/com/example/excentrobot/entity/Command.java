package com.example.excentrobot.entity;

import com.example.excentrobot.util.StringUtil;

import java.util.Optional;
import java.util.stream.Stream;

public enum Command {
    EDIT_MESSAGE_DEMO("send_message", "Demo Send Different Messages"),
    CALLBACK_DEMO("callback", "Demo Callback Buttons"),
    CALLBACK_REAL_DEMO("callback_real", "Demo Callback Use Case"),
    REPLY_MARKUP_DEMO("reply_markup", "Demo Reply Keyboard Markup"),
    REPLY_MARKUP_RESIZED_DEMO("reply_markup_resized", "Demo Reply Keyboard Resized Markup"),
    REPLY_MARKUP_ONE_DEMO("reply_markup_one", "Demo Reply Keyboard One Time Markup"),
    REPLY_MARKUP_REAL_DEMO("reply_markup_real", "Demo Reply Keyboard Markup Use Case"),
    REPLY_MARKUP_REMOVE_DEMO("reply_markup_remove", "Remove Reply Keyboard Markup"),
    REPLY_MARKUP_FORCE_DEMO("reply_markup_force", "Force Reply Keyboard Markup"),
    PAYMENT_DEMO("payment", "Demo Payment"),
    ABOUT("about", "About Bot");

    Command(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    private final String name;
    private final String desc;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public static Optional<Command> parseCommand(String command) {
        if (StringUtil.isBlank(command)) {
            return Optional.empty();
        }
        String formatName = StringUtil.trim(command).toLowerCase();
        return Stream.of(values()).filter(c -> c.name.equalsIgnoreCase(formatName)).findFirst();
    }
}

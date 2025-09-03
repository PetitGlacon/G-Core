package fr.the__glacier.gcore.commands.utils.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fr.the__glacier.gcore.color.MiniMessages;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class EnumArgument<T extends Enum<T>> implements CustomArgumentType.Converted<T, String> {

    private final Class<T> enumClass;
    private String message;
    private final DynamicCommandExceptionType ERROR_INVALID_ENUM = new DynamicCommandExceptionType(o -> MessageComponentSerializer.message().serialize(new MiniMessages(this.message.replace("%value%", (String) o)).getComponent()));

    public EnumArgument(Class<T> enumClass, String message) {
        this.enumClass = enumClass;
        this.message = message;
    }

    @Override
    public @NotNull T convert(String input) throws CommandSyntaxException {
        try {
            return Enum.valueOf(enumClass, input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw ERROR_INVALID_ENUM.create(input);
        }
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for (T constant : enumClass.getEnumConstants()) {
            builder.suggest(constant.name().toLowerCase());
        }
        return builder.buildFuture();
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

}

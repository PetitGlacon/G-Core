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

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class StringListArgument  implements CustomArgumentType.Converted<String, String>{
    private String message;
    private Set<String> strings;
    private final DynamicCommandExceptionType ERROR_INVALID_MOB = new DynamicCommandExceptionType(value -> MessageComponentSerializer.message().serialize(new MiniMessages(this.message.replace("%value%", (String) value)).getComponent()));

    public StringListArgument(Set<String> strings, String message){
        this.strings = strings;
        this.message = message;
    }

    @Override
    public @NotNull String convert(@NotNull String nativeType) throws CommandSyntaxException {
        if (strings.contains(nativeType)) return nativeType;
        throw ERROR_INVALID_MOB.create(nativeType);
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for (String string : strings){
            if (string.startsWith(builder.getRemainingLowerCase())){
                builder.suggest(string);
            }
        }
        return builder.buildFuture();
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}

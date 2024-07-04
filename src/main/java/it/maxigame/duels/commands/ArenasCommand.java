package it.maxigame.duels.commands;

import it.maxigame.duels.game.arena.ArenaAgent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ArenasCommand implements CommandExecutor {
    public static final String[] HELP_MESSAGE = new String[]{
            "§fArena commands - §7(by Maxigame99)",
            "§6§l/arenas",
            "§6§l/delarena <name>",
            "§6§l/setarena <name>",
            "§fThen:",
            "§e/setarena reg",
            "§e/setarena spawn",
    };
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("duels.admin")) {
            sender.sendMessage("§cNon hai accesso a questo comando.");
            return false;
        }
        sender.sendMessage("§6§lLista delle arene:");
        ArenaAgent.getArenasName().forEach(sender::sendMessage);
        sender.sendMessage("§6§l---");
        return true;
    }
}

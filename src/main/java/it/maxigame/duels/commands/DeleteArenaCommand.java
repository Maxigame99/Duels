package it.maxigame.duels.commands;

import it.maxigame.duels.game.arena.Arena;
import it.maxigame.duels.game.arena.ArenaAgent;
import it.maxigame.duels.game.arena.ArenaStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static it.maxigame.duels.commands.ArenasCommand.HELP_MESSAGE;

public class DeleteArenaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("duels.admin")) {
            sender.sendMessage("§cNon hai accesso a questo comando.");
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(HELP_MESSAGE);
            return false;
        }

        String arenaName = args[0];
        Arena arena = ArenaAgent.getArena(arenaName);
        if (arena==null) {
            sender.sendMessage("§cArena non trovata!");
        }else if (arena.getStatus()!= ArenaStatus.READY)
            sender.sendMessage("§cL'arena è attualmente in utilizzo!");
        else {
            ArenaAgent.deleteArena(arenaName);
            sender.sendMessage("§aArena eliminata!");
            return true;
        }
        return false;
    }
}

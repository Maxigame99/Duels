package it.maxigame.duels.commands;

import it.maxigame.duels.game.arena.Arena;
import it.maxigame.duels.game.arena.ArenaAgent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static it.maxigame.duels.commands.ArenasCommand.HELP_MESSAGE;

public class CreateArenaCommand implements CommandExecutor {
    private final HashMap<Player, Arena> buildingArenas = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cComando eseguibile solamente da giocatore.");
            return false;
        }
        if (!sender.hasPermission("duels.admin")) {
            sender.sendMessage("§cNon hai accesso a questo comando.");
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(HELP_MESSAGE);
            return false;
        }

        Player player = (Player) sender;
        String subcommand = args[0];
        if (!buildingArenas.containsKey(player)) {
            if (ArenaAgent.getArena(subcommand)!=null) {
                sender.sendMessage("§cEsiste già un'arena con questo nome.");
                return true;
            }
            buildingArenas.put(player, new Arena(subcommand));
            sender.sendMessage("§eArena creata!");
            return true;
        }
        Arena arena = buildingArenas.get(player);
        Location loc = player.getLocation();
        switch (subcommand.toLowerCase()) {
            case "reg":
                if (arena.getRegion1()==null)
                    arena.setRegion1(loc);
                else arena.setRegion2(loc);
                sender.sendMessage("§eRegione impostata!");
                break;
            case "spawn":
                if (arena.getSpawn1()==null)
                    arena.setSpawn1(loc);
                else arena.setSpawn2(loc);
                sender.sendMessage("§eSpawn impostato!");
                break;
        }
        if (arena.getRegion1()!=null && arena.getRegion2()!=null && arena.getSpawn1()!=null && arena.getSpawn2()!=null) {
            ArenaAgent.addArena(arena);
            sender.sendMessage("§aArena configurata con successo!");
            return true;
        }
        return false;
    }
}

package it.maxigame.duels.commands;

import it.maxigame.duels.game.duel.Duel;
import it.maxigame.duels.game.duel.DuelManager;
import it.maxigame.duels.game.kit.KitHolder;
import it.maxigame.duels.gui.KitSelectorInventory;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class DuelCommands implements CommandExecutor {

    static final String[] HELP_MESSAGE = new String[]{
            "§fDuels commands - §7(by Maxigame99)",
            "§e§l/duel <player>",
            "§e/duel accept <player>",
            "§e/duel reject <player>",
    };


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cComando eseguibile solamente da giocatore.");
            return false;
        }
        if (args.length==0) {
            sender.sendMessage(HELP_MESSAGE);
            return false;
        }

        String subcommand = args[0];
        switch (subcommand.toLowerCase()) {
            case "accept":
                Player requester = Bukkit.getPlayer(args[1]);
                return true;
            case "refuse":
                return true;
        }
        Player requesterPlayer = (Player) sender;
        Consumer<KitHolder> onSelect = (kit) -> {
            Player receiverPlayer = Bukkit.getPlayer(subcommand);
            Duel duel = new Duel(requesterPlayer, receiverPlayer, kit);
            if (DuelManager.requestDuel(duel))
                sendRequestMessage(duel);
        };
        new KitSelectorInventory(onSelect).open(requesterPlayer);


        return true;
    }


    private static void sendRequestMessage(Duel duel) {
        String requesterName = duel.getRequester().getName();
        Player receiver = duel.getReceiver();
        String kitName = duel.getKit().getName();
        BaseComponent descriptionLine = new TextComponent("§e"+requesterName+" §avorrebbe duellare con te. Kit: §b"+kitName);
        BaseComponent buttonLine = new TextComponent();

        // Building button line
        BaseComponent refuseButton = new TextComponent("§c§l[RIFIUTA]");
        refuseButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duels refuse "+requesterName));

        BaseComponent spacer = new TextComponent("    ");

        BaseComponent acceptButton = new TextComponent("§a§l[ACCEPT]");
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duels accept "+requesterName));
        buttonLine.addExtra(refuseButton);
        buttonLine.addExtra(spacer);
        buttonLine.addExtra(acceptButton);
        //

        receiver.sendMessage(descriptionLine, buttonLine);
    }
}

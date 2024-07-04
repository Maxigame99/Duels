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
    static final String PLAYER_NOT_FOUND = "§cGiocatore non trovato!";

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
        Player cmdSender = (Player) sender;
        if (DuelManager.isDueling(cmdSender)) {
            cmdSender.sendMessage("§cSei attualmente in un duello!");
            return false;
        }

        String subcommand = args[0];
        switch (subcommand.toLowerCase()) {
            case "accept":
                return acceptDuel(Bukkit.getPlayer(args[1]), cmdSender);
            case "refuse":
                return refuseDuel(Bukkit.getPlayer(args[1]), cmdSender);
        }
        Player receiverPlayer = Bukkit.getPlayer(args[1]);
        if (DuelManager.isDueling(receiverPlayer)) {
            cmdSender.sendMessage("§c"+receiverPlayer.getName()+" è attualmente in un duello!");
            return false;
        }

        Consumer<KitHolder> onSelect = (kit) -> {
            Duel duel = new Duel(cmdSender, receiverPlayer, kit);
            if (DuelManager.requestDuel(duel))
                sendRequestMessage(duel);
        };
        new KitSelectorInventory(onSelect).open(cmdSender);


        return true;
    }



    private static boolean acceptDuel(Player requester, Player receiver) {
        if (requester==null) {
            receiver.sendMessage(PLAYER_NOT_FOUND);
            return false;
        }
        Duel duel = DuelManager.getDuel(requester, receiver);
        if (duel==null) {
            receiver.sendMessage("§cNon hai nessun duello in attesa con "+requester.getName());
            return false;
        }
        DuelManager.acceptDuel(duel);
        return true;
    }
    private static boolean refuseDuel(Player requester, Player receiver) {
        if (requester==null) {
            receiver.sendMessage(PLAYER_NOT_FOUND);
            return false;
        }
        Duel duel = DuelManager.getDuel(requester, receiver);
        if (duel==null) {
            receiver.sendMessage("§cNon hai nessun duello in attesa con "+requester.getName());
            return false;
        }
        DuelManager.refuseDuel(duel);
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

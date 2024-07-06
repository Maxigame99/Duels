package it.maxigame.duels.commands;

import it.maxigame.duels.api.DuelRefuseEvent;
import it.maxigame.duels.game.duel.model.Duel;
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

    private static final String[] HELP_MESSAGE = new String[]{
            "§fDuels commands - §7(by Maxigame99)",
            "§6§l/duel <player>",
            "§e/duel accept <player>",
            "§e/duel reject <player>",
    };
    private static final String PLAYER_NOT_FOUND = "§cGiocatore non trovato!";

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
        if (args.length>1 && !sender.hasPermission("duels.admin")) {
            sender.sendMessage("§cNon hai accesso a questo comando.");
            return false;
        }
        String subcommand = args[0];
        switch (subcommand.toLowerCase()) {
            case "accept":
                return acceptDuel(Bukkit.getPlayer(args[1]), cmdSender);
            case "refuse":
                return refuseDuel(Bukkit.getPlayer(args[1]), cmdSender);
        }
        Player receiverPlayer = Bukkit.getPlayer(subcommand);
        if (receiverPlayer == cmdSender) {
            cmdSender.sendMessage("§cNon puoi duellare con te stesso! xd");
            return false;
        }
        if (DuelManager.isDueling(receiverPlayer)) {
            cmdSender.sendMessage("§c"+receiverPlayer.getName()+" è attualmente in un duello!");
            return false;
        }

        Consumer<KitHolder> onSelect = (kit) -> {
            Duel duel = new Duel(cmdSender, receiverPlayer, kit);
            if (DuelManager.requestDuel(duel))
                sendRequestMessage(duel);
        };
        new KitSelectorInventory(cmdSender, onSelect).open();


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
        DuelManager.startDuel(duel);
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
        DuelManager.cancelDuel(duel);
        Bukkit.getPluginManager().callEvent(new DuelRefuseEvent(duel, DuelRefuseEvent.RefuseCause.REJECTED));
        return true;
    }

    private static void sendRequestMessage(Duel duel) {
        Player requester = duel.getRequester().getPlayer();
        String requesterName = requester.getName();
        Player receiver = duel.getReceiver().getPlayer();
        String kitName = duel.getKit().getName();
        BaseComponent descriptionLine = new TextComponent("§e"+requesterName+" §avorrebbe duellare con te. Kit: §b"+kitName);
        BaseComponent buttonLine = new TextComponent();

        // Building button line
        BaseComponent refuseButton = new TextComponent("§c§l[RIFIUTA]");
        refuseButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel refuse "+requesterName));

        BaseComponent spacer = new TextComponent("    ");

        BaseComponent acceptButton = new TextComponent("§a§l[ACCEPT]");
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel accept "+requesterName));
        buttonLine.addExtra(refuseButton);
        buttonLine.addExtra(spacer);
        buttonLine.addExtra(acceptButton);
        //

        receiver.sendMessage(descriptionLine);
        receiver.sendMessage(buttonLine);
        requester.sendMessage("§aHai inviato la richiesta di duello a §e"+receiver.getName()+"§a!");
    }
}

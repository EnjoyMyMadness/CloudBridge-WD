package com.bedrockcloud.commands;

import com.bedrockcloud.handler.JoinHandler;
import dev.waterdog.waterdogpe.command.Command;
import dev.waterdog.waterdogpe.command.CommandSender;
import dev.waterdog.waterdogpe.command.CommandSettings;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

public class HubCommand extends Command {

    private final JoinHandler joinHandler;

    public HubCommand(JoinHandler joinHandler){
        super("hub", CommandSettings.builder()
                .setDescription("Transfers you to the Hub.")
                .build());
        this.joinHandler = joinHandler;
    }

    @Override
    public boolean onExecute(CommandSender commandSender, String s, String[] strings) {

        if (!(commandSender instanceof ProxiedPlayer)) return false;

        if (((ProxiedPlayer) commandSender).getServerInfo().getServerName().equals("Lobby")){
            commandSender.sendMessage("§l§cYou are already on a Lobby server§r§7.");
            return false;
        }

        this.joinHandler.determineServer(((ProxiedPlayer) commandSender), ((ProxiedPlayer) commandSender).getServerInfo());
        return false;
    }
}

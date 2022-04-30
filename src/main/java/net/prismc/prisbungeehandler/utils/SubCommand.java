package net.prismc.prisbungeehandler.utils;

import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

import java.util.List;

public abstract class SubCommand {

    public abstract List<String> getNames();

    public abstract void perform(PrisPlayer player, String[] args);

}

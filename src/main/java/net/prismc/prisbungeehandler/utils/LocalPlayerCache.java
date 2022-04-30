package net.prismc.prisbungeehandler.utils;

import net.prismc.prisbungeehandler.communication.sql.SQL;
import net.prismc.prisbungeehandler.parties.PrisParty;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

// holy shit, you should've seen the last version of this class...
public class LocalPlayerCache extends SQL {

    public HashMap<UUID, PrisPlayer> onlinePlayers = new HashMap<>();
    public List<OfflinePrisPlayer> offlinePlayers = new ArrayList<>();
    public List<PrisParty> parties = new ArrayList<>();

}

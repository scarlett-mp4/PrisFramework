package net.prismc.prisbungeehandler.files.loader;

import net.prismc.prisbungeehandler.files.configs.Config;

public class EngLoader {
    private final Config config;

    public EngLoader(Config config) {
        this.config = config;
    }

    /**
     * Loads the values into the specified config file
     */
    public void loadMessages() {

        // Friends
        config.getConfig().set("Friends.General.UnknownCommand", "&5Friends: &cThis command does not exist.");
        config.getConfig().set("Friends.General.MissingPerms", "&5Friends: &cInsufficient permissions.");
        config.getConfig().set("Friends.General.NotOnline", "&5Friends: &cThat player is not online!");
        config.getConfig().set("Friends.General.MissingArgs", "&5Friends: &cYou must include a player.");
        config.getConfig().set("Friends.General.Spacer", "&9-----------------------------------------------------");

        config.getConfig().set("Friends.Help.Accept", "&7/&5friend &baccept <player> &7- &fAccept a friend request.");
        config.getConfig().set("Friends.Help.Add", "&7/&5friend &badd <player> &7- &fAdd a friend.");
        config.getConfig().set("Friends.Help.Deny", "&7/&5friend &bdeny <player> &7- &fDecline a friend request.");
        config.getConfig().set("Friends.Help.List", "&7/&5friend &blist <page> &7- &fSee all your friends you've added.");
        config.getConfig().set("Friends.Help.Notifications", "&7/&5friend &bnotifications &7- &fToggle friend notifications.");
        config.getConfig().set("Friends.Help.Remove", "&7/&5friend &bremove &7- &fRemove a friend.");
        config.getConfig().set("Friends.Help.Toggle", "&7/&5friend &btoggle &7- &fToggle friend requests.");
        config.getConfig().set("Friends.Help.Header", "&d&lFriends");

        config.getConfig().set("Friends.Add.Toggled", "&5Friends: &cThis player has friend requests disabled.");
        config.getConfig().set("Friends.Add.CantFriendYourself", "&5Friends: &cYou can't friend yourself!");
        config.getConfig().set("Friends.Add.TimedOutFrom", "&5Friends: &cYour friend request from &e[PLAYER] &chas timed out!");
        config.getConfig().set("Friends.Add.TimedOutTo", "&5Friends: &cYour friend request to &e[PLAYER] &chas timed out!");
        config.getConfig().set("Friends.Add.Sent", "&5Friends: &aYou have successfully sent a friend request!");
        config.getConfig().set("Friends.Add.AlreadySent", "&5Friends: &cYou've already sent that player a friend request.");
        config.getConfig().set("Friends.Add.AlreadyReceived", "&5Friends: &cYou have already received a friend request from this player!");
        config.getConfig().set("Friends.Add.AlreadyFriends", "&5Friends: &cYou are already friends with this player.");
        config.getConfig().set("Friends.Add.Request", "&6[PLAYER] &ehas sent you a friend request!");
        config.getConfig().set("Friends.Add.ADD", "&a&lADD");
        config.getConfig().set("Friends.Add.OR", "&7 or ");
        config.getConfig().set("Friends.Add.DENY", "&c&lDENY");
        config.getConfig().set("Friends.Add.ADDHover", "&7Click me to accept this friend request.");
        config.getConfig().set("Friends.Add.DENYHover", "&7Click me to deny this friend request.");

        config.getConfig().set("Friends.Accept.NoRequest", "&5Friends: &cYou haven't received a friend request from this player.");
        config.getConfig().set("Friends.Accept.NowFriends", "&5Friends: &aYou are now friends with &e[PLAYER]&a!");

        config.getConfig().set("Friends.List.Header", "&d&lYour Friends");
        config.getConfig().set("Friends.List.RightArrow", "&2&l>>");
        config.getConfig().set("Friends.List.LeftArrow", "&2&l<<");
        config.getConfig().set("Friends.List.NoRight", "&4&l>>");
        config.getConfig().set("Friends.List.NoLeft", "&4&l<<");
        config.getConfig().set("Friends.List.HoverPage", "&6Page [PAGE]");
        config.getConfig().set("Friends.List.OnlineMessage", "&7- &a[PLAYER] &e[STATUS]");
        config.getConfig().set("Friends.List.OfflineMessage", "&7- &4[PLAYER] &cis currently offline.");
        config.getConfig().set("Friends.List.Status.RPG", "is currently playing Prison.");
        config.getConfig().set("Friends.List.Status.Lobby", "is currently in The Lobby.");
        config.getConfig().set("Friends.List.NoFriends", "&c&lYou have no friends! ");
        config.getConfig().set("Friends.List.UwU", "&d(✿◠‿◠) &6&oDon't worry, I'll be your friend. &c<3");
        config.getConfig().set("Friends.List.NotAPage", "&5Friends: &cThat is not a valid page number.");
        config.getConfig().set("Friends.List.CurrentPage", "&6Page: [PAGE] of [MAXPAGES].");

        config.getConfig().set("Friends.Remove.CantRemoveYourself", "&5Friends: &cYou can't remove yourself!");
        config.getConfig().set("Friends.Remove.NotFriends", "&5Friends: &cYou are not friends with this player!");
        config.getConfig().set("Friends.Remove.BFFRemoved", "&5Friends: &e[PLAYER] &chas removed you from their friends list!");
        config.getConfig().set("Friends.Remove.FrenemyRemoved", "&5Friends: &e[PLAYER] &ahas been removed from your friends list!");

        config.getConfig().set("Friends.Deny.NoRequest", "&5Friends: &cYou haven't received a friend request from this player.");
        config.getConfig().set("Friends.Deny.YouDeclined", "&5Friends: &aYou have declined &e[PLAYER]&a's friend request.");
        config.getConfig().set("Friends.Deny.GotDeclined", "&5Friends: &e[PLAYER] &chas declined your friend request.");

        config.getConfig().set("Friends.Notifications.Online", "&5Friends: &a[PLAYER] &2has connected.");
        config.getConfig().set("Friends.Notifications.Offline", "&5Friends: &c[PLAYER] &4has disconnected.");
        config.getConfig().set("Friends.Notifications.ToggledOn", "&5Friends: &aYou have enabled friend notifications.");
        config.getConfig().set("Friends.Notifications.ToggledOff", "&5Friends: &cYou have disabled friend notifications.");

        config.getConfig().set("Friends.Requests.ToggledOn", "&5Friends: &aYou have enabled friend requests.");
        config.getConfig().set("Friends.Requests.ToggledOff", "&5Friends: &cYou have disabled friend requests.");

        // Language
        config.getConfig().set("Language.General.ImproperUsage", "&cImproper usage, try: /language change <lang>");
        config.getConfig().set("Language.General.Update", "&aLanguage updated!");
        config.getConfig().set("Language.General.Unknown", "&cUnknown language.");

        // Message
        config.getConfig().set("Message.NotOnline", "&cThat player is not online!");
        config.getConfig().set("Message.ImproperUsage", "&cImproper usage, try: /msg <player> <message>");
        config.getConfig().set("Message.From", "&d[PLAYER] &6messaged you: &e[MESSAGE]");
        config.getConfig().set("Message.To", "&6You messaged &d[PLAYER]: &e[MESSAGE]");
        config.getConfig().set("Message.CantMessagePlayer", "&cYou can't message this player.");
        config.getConfig().set("Message.ToggledOff", "&cYou will not receive messages.");
        config.getConfig().set("Message.ToggledFriends", "&6Only friends can send you messages.");
        config.getConfig().set("Message.ToggledOn", "&aAnyone can send you messages.");
        config.getConfig().set("Message.CantMessageYourself", "&cYou can't message yourself!");

        // Reply
        config.getConfig().set("Reply.ImproperUsage", "&cImproper usage, try: /reply <message>");
        config.getConfig().set("Reply.PlayerIsOffline", "&cThis player is offline!");
        config.getConfig().set("Reply.NoOneToReplyTo", "&cThere is no one to reply to!");

        // Party
        config.getConfig().set("Party.General.UnknownCommand", "&5Party: &cThis command does not exist.");
        config.getConfig().set("Party.General.MissingPerms", "&5Party: &cInsufficient permissions.");
        config.getConfig().set("Party.General.NotOnline", "&5Party: &cThat player is not online!");
        config.getConfig().set("Party.General.MissingArgs", "&5Party: &cYou must include a player.");
        config.getConfig().set("Party.General.Spacer", "&9-----------------------------------------------------");

        config.getConfig().set("Party.Events.CantSwitchServers", "&5Party: &cYou must be the party leader to join this server!");
        config.getConfig().set("Party.Events.LeaderDisconnected", "&c&lThe party leader has disconnected!");
        config.getConfig().set("Party.Events.TimeToReconnect", "&7They have &e5 MINUTES &7to reconnect.");
        config.getConfig().set("Party.Events.LeaderReconnected", "&e&lYour party leader has reconnected!");
        config.getConfig().set("Party.Events.GetBackInThere", "&c⚔ &6Now get back in there! &c⚔");
        config.getConfig().set("Party.Events.OfflinePlayers", "&5Party: &cYou can't join this server with offline party members!");

        config.getConfig().set("Party.Help.Join", "&7/&5party &bjoin <player> &7- &fJoin a player's party.");
        config.getConfig().set("Party.Help.Create", "&7/&5party &bcreate <open/closed> &7- &fCreate a party.");
        config.getConfig().set("Party.Help.Invite", "&7/&5party &binvite <player> &7- &fInvite a player to the party.");
        config.getConfig().set("Party.Help.Kick", "&7/&5party &bkick <player> &7- &fKick a player from the party.");
        config.getConfig().set("Party.Help.Leave", "&7/&5party &bleave <player> &7- &fLeave the party.");
        config.getConfig().set("Party.Help.Disband", "&7/&5party &bdisband <player> &7- &fDisband the party.");
        config.getConfig().set("Party.Help.Promote", "&7/&5party &bpromote <player> &7- &fPromote a user.");
        config.getConfig().set("Party.Help.Chat", "&7/&5party &bchat <message> &7- &fInteract with the party.");
        config.getConfig().set("Party.Help.Mute", "&7/&5party &bmute &7- &fMute the party chat.");
        config.getConfig().set("Party.Help.Status", "&7/&5party &bstatus <open/closed> &7- &fSet the party status.");
        config.getConfig().set("Party.Help.Warp", "&7/&5party &bwarp &7- &fTeleport the party to your server.");
        config.getConfig().set("Party.Help.List", "&7/&5party &blist &7- &fShows the current party members.");
        config.getConfig().set("Party.Help.Header", "&d&lParty");

        config.getConfig().set("Party.Invite.CantPartyPlayer", "&5Party: &cYou can't party this player");
        config.getConfig().set("Party.Invite.SentAnInvite", "&5Party: &aYou have successfully sent an invite.");
        config.getConfig().set("Party.Invite.AlreadySent", "&5Party: &cYou have already sent this player a party invite.");
        config.getConfig().set("Party.Invite.ReceivedInvite", "&6[PLAYER] &ehas invited you to a party!");
        config.getConfig().set("Party.Invite.JOIN", "&aClick me to &2&lJOIN");
        config.getConfig().set("Party.Invite.JOINHover", "&7Click me to join this party.");
        config.getConfig().set("Party.Invite.TimedOutFrom", "&5Party: &cYour party invite from &e[PLAYER] &chas timed out!");
        config.getConfig().set("Party.Invite.TimedOutTo", "&5Party: &cYour party invite to &e[PLAYER] &chas timed out!");
        config.getConfig().set("Party.Invite.CantInviteSelf", "&5Party: &cYou can't invite yourself!");
        config.getConfig().set("Party.Invite.AlreadyInParty", "&5Party: &cThat player is already in your party!");
        config.getConfig().set("Party.Invite.NotMoreOrLeader", "&5Party: &cYou must be a party moderator or leader to invite players!");
        config.getConfig().set("Party.Invite.Full", "&5Party: &cYour party is already full!");

        config.getConfig().set("Party.Join.AlreadyInAParty", "&5Party: &cYou are already in a party.");
        config.getConfig().set("Party.Join.NoRequest", "&5Party: &cYou haven't received a party invite from this player.");
        config.getConfig().set("Party.Join.HasJoined", "&5Party: &6[PLAYER] &ehas joined the party!");
        config.getConfig().set("Party.Join.LeaderMustBeOnline", "&5Party: &e[PLAYER] &chas disconnected! They must be online to join their party.");
        config.getConfig().set("Party.Join.Full", "&5Party: &cThis party is full!");

        config.getConfig().set("Party.List.NotInParty", "&5Party: &cYou are not in a party!");
        config.getConfig().set("Party.List.PartyHeader", "&e&l[PLAYER]'s &6&lParty:");
        config.getConfig().set("Party.List.Status.STATUS", "&7Status: [STATUS]");
        config.getConfig().set("Party.List.Status.OPEN", "&a&oOpen");
        config.getConfig().set("Party.List.Status.CLOSED", "&c&oInvite-only");
        config.getConfig().set("Party.List.Leader", "&eLeader: [PLAYER]");
        config.getConfig().set("Party.List.Mods", "&eMods: [MEMBERS]");
        config.getConfig().set("Party.List.Members", "&eMembers: [MEMBERS]");

        config.getConfig().set("Party.Leave.NotInParty", "&5Party: &cYou can't leave a party if you're not in one!");
        config.getConfig().set("Party.Leave.LeftTheParty", "&5Party: &6[PLAYER] &chas left the party!");
        config.getConfig().set("Party.Leave.YouLeft", "&5Party: &6You have left the party.");
        config.getConfig().set("Party.Leave.NotEnoughPlayers", "&5Party: &3Everyone have left the party!");

        config.getConfig().set("Party.Disband.NotInParty", "&5Party: &cYou can't disband a party if you're not in one!");
        config.getConfig().set("Party.Disband.MustBeLeader", "&5Party: &cYou must be the party leader to perform this command.");
        config.getConfig().set("Party.Disband.HasBeenDisbanded", "&5Party: &4The party has been disbanded!");

        config.getConfig().set("Party.Kick.NoPermission", "&5Party: &cYou don't have permission to do this!");
        config.getConfig().set("Party.Kick.CantKickPlayer", "&5Party: &cYou can't kick this player!");
        config.getConfig().set("Party.Kick.NotInParty", "&5Party: &cYou can't kick a player if you're not in a party!");
        config.getConfig().set("Party.Kick.PlayerNotInParty", "&5Party: &cThis player is not in your party.");
        config.getConfig().set("Party.Kick.KickedPlayer", "&5Party: &e[MOD] &chas removed &e[PLAYER] &cfrom the party!");
        config.getConfig().set("Party.Kick.NotEnoughPlayers", "&5Party: &3Everyone have left the party!");
        config.getConfig().set("Party.Kick.YouWereKicked", "&5Party: &cYou have been kicked from the party!");
        config.getConfig().set("Party.Kick.CantKickSelf", "&5Party: &cTYou can't kick yourself!");

        config.getConfig().set("Party.Promote.NotInParty", "&5Party: &cYou can't promote a player if you're not in a party!");
        config.getConfig().set("Party.Promote.PlayerNotInParty", "&5Party: &cThis player is not in your party.");
        config.getConfig().set("Party.Promote.NoPermission", "&5Party: &cYou don't have permission to do this!");
        config.getConfig().set("Party.Promote.AlreadyMod", "&5Party: &cThis player is already a mod!");
        config.getConfig().set("Party.Promote.AlreadyLeader", "&5Party: &cThis player is already the party leader!");
        config.getConfig().set("Party.Promote.ModPromoted", "&5Party: &e[PLAYER] &ahas been promoted to moderator!");
        config.getConfig().set("Party.Promote.LeaderPromoted", "&5Party: &e[PLAYER] &ais the new party leader!");

        config.getConfig().set("Party.Create.AlreadyInParty", "&5Party: &cYou are already in a party!");
        config.getConfig().set("Party.Create.Created", "&5Party: &aYou have created a party!");
        config.getConfig().set("Party.Create.CreatedOpen", "&5Party: &6You have created an &aOPEN &6party!");
        config.getConfig().set("Party.Create.InvalidType", "&5Party: &cInvalid party status! &7(open/closed)");

        config.getConfig().set("Party.Status.NotInParty", "&5Party: &cYou must be in a party to use this command.");
        config.getConfig().set("Party.Status.NoPermission", "&5Party: &cYou don't have permission to do this!");
        config.getConfig().set("Party.Status.Open", "&5Party: &6Your party is now &aOPEN&6!");
        config.getConfig().set("Party.Status.Closed", "&5Party: &6Your party is now &cCLOSED&6!");

        config.getConfig().set("Party.Warp.NotInParty", "&5Party: &cYou must be in a party to use this command.");
        config.getConfig().set("Party.Warp.NoPermission", "&5Party: &cYou must be the leader to use this command.");
        config.getConfig().set("Party.Warp.YouWarped", "&5Party: &dYou have been warped!");
        config.getConfig().set("Party.Warp.PlayersWarped", "&5Party: &dYou have warped your party to your current server!");

        config.saveConfig();
        config.reloadConfig();
    }
}

package net.stonyvin

class CommandHandler {
    private static final String COMMAND_JOIN = "join"
    private static final String COMMAND_LIST = "list"

    IRCLib irc

    CommandHandler(IRCLib irc) {
        this.irc = irc
    }

    private safeCall(List<String> splitCommand, Closure func) {
        try {
            func(splitCommand)
        } catch (Exception e) {
            println("ERROR: invalid parameters!")
        }
    }

    boolean handle(String rawCommand) {
        List<String> splitCommand = rawCommand.split()
        String command = splitCommand[0]

        if (command.startsWith("/")) {
            command = command.subSequence(1, command.size())
        } else {
            return false
        }

        switch (command.toLowerCase()) {
            case COMMAND_JOIN:
                safeCall(splitCommand){ irc.channel.joinCommand(it[1], it[2])}
                break
            case COMMAND_LIST:
                safeCall(splitCommand) { irc.channel.listCommand() }
                break
        }

        return true
    }
}

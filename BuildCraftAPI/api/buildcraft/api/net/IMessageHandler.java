package buildcraft.api.net;

import buildcraft.api.net.MessageContext;

public interface IMessageHandler<REQ extends IMessage, REPLY extends IMessage> {
    REPLY onMessage(REQ message, MessageContext ctx);
}

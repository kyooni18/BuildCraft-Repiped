package buildcraft.api.net;

import buildcraft.api.core.BCLog;
import net.minecraft.network.FriendlyByteBuf;

public interface IMessage {
    void fromBytes(FriendlyByteBuf buf);

    void toBytes(FriendlyByteBuf buf);

    public static <MSG extends IMessage> MSG staticFromBytes(Class<MSG> clazz, FriendlyByteBuf buf) {
        try {
            MSG message = clazz.newInstance();
            message.fromBytes(buf);
            return message;
        } catch (Exception e) {
            BCLog.logger.error(e);
            return null;
        }
    }
}

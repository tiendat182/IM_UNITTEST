package org.primefaces.component.socket;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class Vs2SocketRenderer
        extends SocketRenderer
{
    public void decode(FacesContext context, UIComponent component)
    {
        decodeBehaviors(context, component);
    }

    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException
    {
        Vs2Socket socket = (Vs2Socket)component;
        String pushServer = socket.getPushServer();
        String channel = socket.getChannel();
        socket.setChannel(channel);
        String channelUrl = "/primepush" + channel;
        String url = getResourceURL(context, channelUrl);
        String clientId = socket.getClientId(context);
        if (pushServer != null) {
            url = pushServer + channelUrl;
        }
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.initWithDomReady("Socket", socket.resolveWidgetVar(), clientId);

        wb.attr("url", url).attr("autoConnect", Boolean.valueOf(socket.isAutoConnect())).attr("transport", socket.getTransport()).attr("fallbackTransport", socket.getFallbackTransport()).callback("onMessage", socket.getOnMessage()).callback("onError", "function(response)", socket.getOnError()).callback("onClose", "function(response)", socket.getOnClose()).callback("onOpen", "function(response)", socket.getOnOpen()).callback("onReconnect", "function(response)", socket.getOnReconnect()).callback("onMessagePublished", "function(response)", socket.getOnMessagePublished()).callback("onTransportFailure", "function(response, request)", socket.getOnTransportFailure()).callback("onLocalMessage", "function(response)", socket.getOnLocalMessage());

        encodeClientBehaviors(context, socket);

        wb.finish();
    }
}

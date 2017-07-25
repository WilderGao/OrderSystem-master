import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 小吉哥哥 on 2017/7/21.
 */
public class Server {
    private ServerSocketChannel mChannel;
    private Selector mSelector;
    public Map<Long,SocketChannel> onlineCustomerMap = new HashMap<>();
    public Map<Long,SocketChannel> onlineSupplierMap = new HashMap<>();

    public interface OnEventListener {
        void onAcceptable(SelectionKey key);

        void onReadable(SelectionKey key);
    }

    private OnEventListener mOnEventListener = null;

    public OnEventListener getmOnEventListener() {
        return mOnEventListener;
    }

    public void setmOnEventListener(OnEventListener mOnEventListener) {
        this.mOnEventListener = mOnEventListener;
    }

    public void init() {
        try {
            mSelector = Selector.open();
            mChannel = ServerSocketChannel.open();
            mChannel.socket().bind(new InetSocketAddress(10086));
            mChannel.configureBlocking(false);
            mChannel.register(mSelector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selector() {
        try {
            while (true) {

                if (mSelector.select(3000) == 0) continue;
                Iterator<SelectionKey> iterator = mSelector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    //链接时间
                    if (key.isAcceptable()) {
                        if (mOnEventListener != null) {
                            mOnEventListener.onAcceptable(key);
                        }
                    }
                    //可读时间
                    if (key.isReadable()) {
                        if (mOnEventListener != null) {
                            mOnEventListener.onReadable(key);
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

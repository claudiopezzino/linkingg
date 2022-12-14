package view.bean.observers;

import java.io.Serializable;
import java.util.Map;

// Serializable needed to send Observer instance over socket stream
public interface Observer extends Serializable {
    <V> void update(Map<String, V> map);
}

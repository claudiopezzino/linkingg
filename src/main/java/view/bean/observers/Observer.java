package view.bean.observers;

import java.util.Map;

public interface Observer {
    <V> void update(Map<String, V> map);
}

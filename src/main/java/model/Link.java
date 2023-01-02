package model;

import java.io.Serializable;
import java.util.Map;

public interface Link extends Serializable {
    String destination();
    String source();
    Map<String, String> sourceDetails();
}

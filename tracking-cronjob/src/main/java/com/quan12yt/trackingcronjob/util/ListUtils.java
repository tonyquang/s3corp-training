package com.quan12yt.trackingcronjob.util;

import javax.lang.model.element.Element;
import java.util.List;
import java.util.function.Consumer;

public class ListUtils {

    public void ifEmpty(List<?> list, Consumer<Element> ifEmpty){
        if (list.isEmpty()){
            ifEmpty.accept((Element) this);
        }
    }
}

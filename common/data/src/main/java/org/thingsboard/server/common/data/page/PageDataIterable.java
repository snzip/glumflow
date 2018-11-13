package org.thingsboard.server.common.data.page;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.thingsboard.server.common.data.SearchTextBased;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.UUIDBased;

public class PageDataIterable<T extends SearchTextBased<? extends UUIDBased>> implements Iterable<T>, Iterator<T> {

    private final FetchFunction<T> function;
    private final int fetchSize;

    private List<T> currentItems;
    private int currentIdx;
    private boolean hasNextPack;
    private TextPageLink nextPackLink;
    private boolean initialized;

    public PageDataIterable(FetchFunction<T> function, int fetchSize) {
        super();
        this.function = function;
        this.fetchSize = fetchSize;
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if(!initialized){
            fetch(new TextPageLink(fetchSize));
            initialized = true;
        }
        if(currentIdx == currentItems.size()){
            if(hasNextPack){
                fetch(nextPackLink);
            }
        }
        return currentIdx < currentItems.size();
    }

    private void fetch(TextPageLink link) {
        TextPageData<T> pageData = function.fetch(link);
        currentIdx = 0;
        currentItems = pageData.getData();
        hasNextPack = pageData.hasNext();
        nextPackLink = pageData.getNextPageLink();
    }

    @Override
    public T next() {
        if(!hasNext()){
            throw new NoSuchElementException();
        }
        return currentItems.get(currentIdx++);
    }

    public static interface FetchFunction<T extends SearchTextBased<? extends UUIDBased>> {

        TextPageData<T> fetch(TextPageLink link);

    }
}

package me.hawkutils.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RowCreator {

	private List<Object> objects;
    private int max;
    private int paginaAtual = 1;
    private Map<Integer, List<Object>> paginasCache = new HashMap<>();

    public RowCreator(List<Object> objects, int max) {
        this.objects = objects;
        this.max = max;
    }

    public List<Object> getPage(int pagina) {
        if (containsPage(pagina) && !objects.isEmpty() && getTotalPages() > 0) {
            if (paginasCache.containsKey(pagina)) {
                return paginasCache.get(pagina);
            } else {
                List<Object> paginaAtual = new ArrayList<>();
                int startIndex = (pagina - 1) * max;
                int endIndex = Math.min(startIndex + max, objects.size());
                for (int i = startIndex; i < endIndex; i++) {
                    paginaAtual.add(objects.get(i));
                }
                paginasCache.put(pagina, paginaAtual);
                return paginaAtual;
            }
        }
        return new ArrayList<>();
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) objects.size() / max);
    }

    public Boolean containsPage(int pagina) {
        return pagina > 0 && pagina <= getTotalPages();
    }
	
}

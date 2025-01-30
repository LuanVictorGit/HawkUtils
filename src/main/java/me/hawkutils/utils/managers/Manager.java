package me.hawkutils.utils.managers;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import me.hawkutils.Core;
import me.hawkutils.utils.menus.Menu;

@Getter
@Setter
public class Manager {

	private List<Menu> menus = new ArrayList<>();
	
	public static Manager get() {
		return Core.getInstance().getManager();
	}
	
}

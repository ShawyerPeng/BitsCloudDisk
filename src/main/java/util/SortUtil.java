package util;

import org.springframework.stereotype.Component;
import service.dto.Sortable;

import java.text.Collator;
import java.util.Arrays;
import java.util.Date;

/**
 * 对 UserFileDTO 和 UserFolderDTO 数组进行按字母排序或者按最后修改时间排序
 */
@Component
public class SortUtil {
    public Sortable[] sort(Sortable[] array, int sortType) {
        switch (sortType) {
            case 0:
                sortByAlpha(array);
                break;
            case 1:
                sortByTime(array);
                break;
            default:
                sortByAlpha(array);
        }
        return array;
    }

    private void sortByAlpha(Sortable[] array) {
        Arrays.sort(array, (s1, s2) -> {
            String name1 = s1.getFileFolderName();
            String name2 = s2.getFileFolderName();
            return Collator.getInstance(java.util.Locale.CHINA).compare(name1, name2);
        });
    }

    private void sortByTime(Sortable[] array) {
        Arrays.sort(array, (s1, s2) -> {
            Date ldt1 = s1.getModifyTime();
            Date ldt2 = s2.getModifyTime();
            return ldt1.compareTo(ldt2);
        });
    }
}